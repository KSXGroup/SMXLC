import os,shutil
for i in range(0, 275):
    fname = 'testcase_' + str(i) + '.txt'
    if os.path.exists(fname):
        f = open(fname)
    else:
        continue
    lst = f.read()
    if lst.find("codegen") != -1:
        shutil.move(""+ fname,"../codegen" + fname)
