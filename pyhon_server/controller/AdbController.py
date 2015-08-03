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
        """
        pipe = subprocess.Popen("ipconfig",
                                shell = True,
                                stdin = subprocess.PIPE,
                                stdout = subprocess.PIPE,
                                stderr = subprocess.PIPE)
        pipe.stdin.close()
        
        retOutputList = []
        retCode = None

        while True :
            pipe.poll()
            out = pipe.stdout.readline()
            
            if(out) :
                retOutputList.append(out)

                retCode = pipe.returncode
                if(out == "" and retCode != None) :
                    break

        return (retCode,retOutputList)
        """
        os.system(self.adbString + " kill-server")

    def startServer(self) :
        os.system(self.adbString + " start-server")

    def setTcpip(self) :
        os.system(self.adbString + " tcpip " + self.port)

    def connectVirtualbox(self) :
        result = os.popen(self.adbString + " connect " + self.virtualmachinIp).read()

        if "unable" not in result :
            return True
        else :
            return False

    def checkConnectDevices(self) :
        os.system(self.adbString + " devices")

    def installApkInVirtualmachine(self,apkPath) :
        print self.adbString + ' install ' + apkPath
        os.system(self.adbString + " install " + apkPath)



