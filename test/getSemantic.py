import os,shutil
for i in range(0, 275):
    fname = 'testcase_' + str(i) + '.txt'
    if os.path.exists(fname):
        f = open(fname)
    else:
        continue
    lst = f.read()
    flg1 = False
    flg2 = False
    if lst.find("semantic") != -1:
        flg1 = True
    if lst.find("success_compile")!= -1 or lst.find("success compile") != -1:
        flg2 = True
    if flg1 and flg2:
       # print("found!")
        shutil.move(""+ fname,"semantic/success/" + fname)
