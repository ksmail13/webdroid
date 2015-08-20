import os
import subprocess

__author__ = 'admin'

class AdbController :
    virtualmachinIp = ""
    userId = ""
    port = ""
    adbString = "adb"

    def __init__(self, virtualmachinIp, userId,port) :
        self.virtualmachinIp = virtualmachinIp
        self.userId = userId
        self.port = port

    def killServer(self) :
        pipe = subprocess.Popen(self.adbString + " kill-server",
                                shell = True,
                                stdin = subprocess.PIPE,
                                stdout = subprocess.PIPE,
                                stderr = subprocess.PIPE)
        pipe.stdin.close()
        stdout, stderr = pipe.communicate()

        print stdout

        #return stdout, stderr


    def startServer(self) :
        pipe = subprocess.Popen(self.adbString + " start-server",
                                shell = True,
                                stdin = subprocess.PIPE,
                                stdout = subprocess.PIPE,
                                stderr = subprocess.PIPE)
        pipe.stdin.close()
        stdout, stderr = pipe.communicate()

        print stdout
        #return stdout, stderr

    def setTcpip(self) :
        pipe = subprocess.Popen(self.adbString + " tcpip " + self.port,
                                shell = True,
                                stdin = subprocess.PIPE,
                                stdout = subprocess.PIPE,
                                stderr = subprocess.PIPE)
        pipe.stdin.close()
        stdout, stderr = pipe.communicate()

        if stderr is None :
            print stdout
        else :
            print stderr

        #return stdout, stderr

    def connectVirtualbox(self) :
        pipe = subprocess.Popen(self.adbString + " connect " + self.virtualmachinIp,
                                shell = True,
                                stdin = subprocess.PIPE,
                                stdout = subprocess.PIPE,
                                stderr = subprocess.PIPE)
        pipe.stdin.close()
        stdout, stderr = pipe.communicate()

        print stdout

        if "unable" not in stdout :
            return True
        else :
            return False

    def checkConnectDevices(self) :
        pipe = subprocess.Popen(self.adbString + " devices",
                                shell = True,
                                stdin = subprocess.PIPE,
                                stdout = subprocess.PIPE,
                                stderr = subprocess.PIPE)
        pipe.stdin.close()

        stdout, stderr = pipe.communicate()

        print stdout

        #return stdout, stderr

    def installApkInVirtualmachine(self,apkPath) :
        print self.adbString + ' install ' + apkPath

        pipe = subprocess.Popen(self.adbString + " install " + apkPath,
                                shell = True,
                                stdin = subprocess.PIPE,
                                stdout = subprocess.PIPE,
                                stderr = subprocess.PIPE)
        pipe.stdin.close()

        stdout, stderr = pipe.communicate()

        print stdout
        #return stdout, stderr



