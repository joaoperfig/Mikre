import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Compressor {
	public static String sentEnds = " \n.},;\t!?<>()\""; // 14 chars, 4 bits, 1111 means 1 gunk, 0000 means nothing
	private static String lowerCases = "abcdefghijklmnopqrstuvwxyz'-";
	private static String upperCases = "ABCDEFGHIJKLMNOPQRSTUVWXYZ'-";
	public LangData ld;
	public ArrayList<PreProcessed> preprocessed;
	public ArrayList<Boolean> bits;
	public String gunkString;

	
	public Compressor(LangData langd) {
		ld = langd;
	}
	
	public void preprocess(String text) {
		preprocessed = new ArrayList<PreProcessed>();
		String allCases = lowerCases.concat(upperCases);
		StringBuilder sb = new StringBuilder();
		boolean buildingword = false;
		//double prog = 0;
		//long total = text.length();
		System.out.println("Initial parse");
		for(char c : text.toCharArray()) {
			//prog++;
			if (allCases.indexOf(c)>=0) { // c is a letter
				if (buildingword) {
					sb.append(c); // add c to current word
				} else {
					String gunk = sb.toString();
					if (gunk.equals(" ")) {
						preprocessed.add(new PreProSpace(gunk)); // create space block
					} else {
						preprocessed.add(new PreProGunk(gunk)); // finish gunk block
					}
					sb = new StringBuilder(); // new word
					buildingword = true;
					sb.append(c); 
				}
			} else { // c is not a letter
				if (buildingword) {
					//System.out.println((prog*100)/total);
					//System.out.println(prog);
					//System.out.println(total);
					//System.out.println(sb.toString());
					preprocessed.add(new PreProWord(sb.toString())); // built word
					buildingword = false;
					sb = new StringBuilder();
					sb.append(c); // add first gunk character
				} else {
					sb.append(c);
				}
			}
		}
		// finish up what's left in stringbuilder
		if (buildingword) {
			preprocessed.add(new PreProWord(sb.toString()));
		} else {
			String gunk = sb.toString();
			if (gunk.equals(" ")) {
				preprocessed.add(new PreProSpace(gunk)); // create space block
			} else {
				preprocessed.add(new PreProGunk(gunk)); // finish gunk block
			}
		}
		System.out.println("Remove empties");
		// remove empties
		for (int i=preprocessed.size()-1; i>=0; i--) { 
			if (preprocessed.get(i).content.isEmpty()) {
				preprocessed.remove(i);
			}
		}
		System.out.println("Replace bad words");
		// replace bad words with gunk
		for (int i=0; i<preprocessed.size(); i++) { 
			if (preprocessed.get(i) instanceof PreProWord) {
				PreProWord w = (PreProWord) preprocessed.get(i);
				if (!w.isValid(ld)) {
					// Not valid, replace with gunk
					//System.out.println("Not valid: "+w.content);
					preprocessed.set(i, new PreProGunk(w.content));
				}
			}
		}
		System.out.println("Join gunks");
		// join consecutive gunks
		ArrayList<PreProcessed> carry = preprocessed;
		preprocessed = new ArrayList<PreProcessed>();
		PreProcessed last = null;
		for (PreProcessed pp : carry) {
			if ((last instanceof PreProGunk) && (pp instanceof PreProGunk)) {
				((PreProGunk)last).addGunk((PreProGunk)pp);
				//System.out.println("Joined gunk");
			} else {
				preprocessed.add(pp);
				last = pp;
			}
		}
		System.out.println("Build sentences");
		// build sentences
		carry = preprocessed;
		preprocessed = new ArrayList<PreProcessed>();
		last = null;
		for (PreProcessed pp : carry) {
			if (last instanceof PreProSentence) {
				PreProSentence sent = (PreProSentence) last;
				if (sent.canAdd(pp)) {
					sent.add(pp);
				} else {
					if (pp instanceof PreProWord) {
						PreProSentence newsent = new PreProSentence((PreProWord)pp);
						preprocessed.add(newsent);
						last = newsent;
					} else {
						preprocessed.add(pp);
						last = pp;
					}
				}
			} else { // last is not a sentence
				if (pp instanceof PreProWord) {
					PreProSentence newsent = new PreProSentence((PreProWord)pp);
					preprocessed.add(newsent);
					last = newsent;
				} else {
					preprocessed.add(pp);
					last = pp;
				}
			}
		}
		// Replace remaining spaces with gunk
		System.out.println("Replace remaining spaces with gunk");
		for (int i=0; i<preprocessed.size(); i++) { 
			if (preprocessed.get(i) instanceof PreProSpace) {
				PreProSpace w = (PreProSpace) preprocessed.get(i);
				preprocessed.set(i, new PreProGunk(" "));
			}
		}
		
		
		// join consecutive gunks
		System.out.println("Join gunks");
		carry = preprocessed;
		preprocessed = new ArrayList<PreProcessed>();
		last = null;
		for (PreProcessed pp : carry) {
			if ((last instanceof PreProGunk) && (pp instanceof PreProGunk)) {
				((PreProGunk)last).addGunk((PreProGunk)pp);
				//System.out.println("Joined gunk");
			} else {
				preprocessed.add(pp);
				last = pp;
			}
		}
		
		
		System.out.println("Expand back");
		// Backwards expand sentences
		last = null;
		for (PreProcessed pp : preprocessed) {
			if ((pp instanceof PreProSentence) && ((last instanceof PreProGunk) || (last instanceof PreProSpace))) {
				if (last.content.charAt(last.content.length() - 1) == ' ') {
					PreProSentence sent = (PreProSentence) pp;
					sent.addStartSpace();
					last.content = last.content.substring(0, last.content.length() - 1);
					//System.out.println("Back expanded");
				}
			}
			last = pp;
		}
		System.out.println("Remove empties");
		// remove empties
		for (int i=preprocessed.size()-1; i>=0; i--) { 
			if (preprocessed.get(i).content.isEmpty()) {
				preprocessed.remove(i);
			}
		}
		System.out.println("Expand forward");
		// Forward expand sentences
		last = null;
		for (int i=preprocessed.size()-1; i>=0; i--) { 
			PreProcessed pp = preprocessed.get(i);
			if (pp instanceof PreProSentence) {
				PreProSentence sent = (PreProSentence)pp;
				boolean finalized = sent.attemptSelfFinalize();
				if (!finalized && ((last instanceof PreProGunk) || (last instanceof PreProSpace))) {
					sent.addEndChar(last.content.charAt(0)); // CHANGED : can add any next char
					last.content = last.content.substring(1);
					//System.out.println("Forward expanded");
				}
			}
			last = pp;
		}
		System.out.println("Remove empties");
		// remove empties
		for (int i=preprocessed.size()-1; i>=0; i--) { 
			if (preprocessed.get(i).content.isEmpty()) {
				preprocessed.remove(i);
			}
		}	
		
		
	}
	
	public void compress(){
		bits = new ArrayList<Boolean>();
		StringBuilder gunkBuilder = new StringBuilder();
		String thisgunk;
		for (PreProcessed pp : preprocessed) {
			bits.addAll(pp.toBits(ld));
			//System.out.print(BitOperations.bitsToBinaryString(pp.toBits(w, g, ld)));
			//System.out.print(" ");
			thisgunk = pp.getGunk();
			if (!thisgunk.isEmpty()) {
				gunkBuilder.append(thisgunk);
				gunkBuilder.append(",");
			}
		}
		//System.out.println();
		gunkString = gunkBuilder.toString();
	}
	
	public String rebuildPrePro() {
		StringBuilder sb = new StringBuilder();
		for (PreProcessed pp : preprocessed) {
			sb.append(pp.content);
		}
		return sb.toString();
	}
	
	public String typesOrdered() {
		StringBuilder sb = new StringBuilder();
		for (PreProcessed pp : preprocessed) {
			sb.append(pp.pptype);
			sb.append(" ");
		}
		return sb.toString();
	}
	
	public void saveToFile(String filename) {
		ArrayList<Byte> bytes;
		System.out.println("Converting to bytes.");
		bytes = BitOperations.bitsToBytes(bits);
		System.out.println("Writing to 0.");
		try (FileOutputStream stream = new FileOutputStream("0")) {
			for (Byte b : bytes)
				stream.write(b);
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Writing to 1.");
		try {
			PrintWriter out = new PrintWriter("1");
			out.write(gunkString);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Zipping.");
		try {
			List<String> srcFiles = Arrays.asList("0", "1");
	        FileOutputStream fos = new FileOutputStream(filename);
	        ZipOutputStream zipOut = new ZipOutputStream(fos);
	        for (String srcFile : srcFiles) {
	            File fileToZip = new File(srcFile);
	            FileInputStream fis = new FileInputStream(fileToZip);
	            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
	            zipOut.putNextEntry(zipEntry);
	 
	            byte[] zipbytes = new byte[1024];
	            int length;
	            while((length = fis.read(zipbytes)) >= 0) {
	                zipOut.write(zipbytes, 0, length);
	            }
	            fis.close();
	        }
	        zipOut.close();
	        fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Deleting 0 and 1.");
		// delete 0 and 1
		File f0 = new File("0");  
		f0.delete();
		File f1 = new File("1");  
		f1.delete();
		
	}
	
	public void compressFile(String filename) {
		String content = "";
		System.out.println("Reading input file "+filename);
	    try
	    {
	        content = new String ( Files.readAllBytes( Paths.get(filename) ) );
	    } 
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	    int dotindex = filename.lastIndexOf(".");
	    String outfilename;
	    if (dotindex > 0) {
	    	outfilename = filename.substring(0, dotindex) + ".mkr";
	    } else {
	    	outfilename = filename + ".mkr";
	    }
	    
	    System.out.println("Preprocessing...");
	    preprocess(content);
	    System.out.println("Compressing...");
	    compress();
	    System.out.println("Writing to " + outfilename);
	    saveToFile(outfilename);
	    System.out.println("Done!");
	}
	
	public static void main(String[] args) {
		/*
		System.out.println(sentEnds.length());
		String test = "In computer science and information theory, a Huffman code is a particular type of optimal prefix code that is commonly used for lossless data compression. #123";
		System.out.println("Loading english...");
		LangData ld = LangData.fromFile("data/english.mkrd");
		Compressor c = new Compressor(ld);
		System.out.println("Preprocessing...");
		c.preprocess(test);
		System.out.println("Original:");
		System.out.println(test);
		System.out.println("Reprocessed:");
		System.out.println(c.rebuildPrePro());
		System.out.println("Types:");
		System.out.println(c.typesOrdered());
		System.out.println("Compressing...");
		c.compress();
		System.out.println(BitOperations.bitsToBinaryString(c.bits));
		System.out.println(c.gunkString);
		System.out.print("Split the original text of size ");
		System.out.println(test.length()*8);
		System.out.print("Into compressable part of size ");
		System.out.print((test.length()-c.gunkString.length())*8);
		System.out.print(" and gunk of size ");
		System.out.println(c.gunkString.length()*8);
		System.out.print("And compressed the compressable part into ");
		System.out.println(c.bits.size());
		System.out.println("Saving to file");
		System.out.println("Success!");*/
		System.out.println("Loading english...");
		LangData ld = LangData.fromFile("data/english.mkrd");
		Compressor c = new Compressor(ld);
		c.compressFile("english_bible.txt");
		
		
	}

}
