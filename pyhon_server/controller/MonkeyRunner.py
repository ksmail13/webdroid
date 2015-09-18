import subprocess

__author__ = 'Administrator'

class MonkeyRunner :
    monkeyString = "monkeyrunner"

    def __init__(self,machineId) :
        self.machineId = machineId

    def startMonkey(self) :

        pipe = subprocess.Popen(self.monkeyString + " c:\\monkey.py",
                                shell = True,
                                stdin = subprocess.PIPE,
                                stdout = subprocess.PIPE,
                                stderr = subprocess.PIPE)
        pipe.stdin.close()
        stdout, stderr = pipe.communicate()

        print stdout
