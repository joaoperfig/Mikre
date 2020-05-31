from os import listdir
from os.path import isfile, join



datadir = "../data/test/"
datafiles = [f for f in listdir(datadir) if isfile(join(datadir, f))]

def openfile(filename):
    f = open(filename, "r", encoding="utf8")
    t = f.read()
    f.close()
    return t

normals = "abcdefghijklmnopqrstuvwxyz"
normals = normals + normals.upper()
normals = normals + "0123456789"

alltext = ""
for file in datafiles:
    alltext = alltext + openfile(datadir + "/" + file)

dic = {}

for c in alltext:
    if c not in normals:
        if c in dic:
            dic[c] = dic[c]+1
        else:
            dic[c] = 1
        
strs = []

for c in list(dic):
    strs = strs + ["0"*(10-len(str(dic[c])))+str(dic[c])+"    "+c]

strs = sorted(strs)

for s in strs:
    print(s)