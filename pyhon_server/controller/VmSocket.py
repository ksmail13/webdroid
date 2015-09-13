from PIL import Image
from collections import namedtuple
import socket
import io
import sys

__author__ = 'admin'

class VmSocket :
    recvStruct = namedtuple("recvStruct", "file_num, buf")

    def __init__(self, udpIp,udpPort) :
        self.udpIp = udpIp
        self.udpPort = udpPort
        self.sock = socket.socket(socket.AF_INET,socket.SOCK_STREAM)

    def bindSocket(self) :
        self.sock.bind((self.udpIp,self.udpPort))

    def aceeptVm(self):
        self.sock.listen(1)
        clientSock , address = self.sock.accept()
        clientSock.settimeout(1)
    def recvData(self) :

        flag = True
        self.sock.listen(1)
        f = open('recv.jpg', 'wb')

        while flag :
            clientSock , address = self.sock.accept()
            i = 1
            while True :
                data = clientSock.recv(1024)
                print i

                if not data :
                    flag = False
                    break

                """
                if "End_File" in data :
                    print "Recieve End_File!!"
                    flag = False
                    break
                byte = bytearray(data)
                for writeData in data :
                    f.write(writeData)
                """
                f.write(data)
                i += 1
        f.close()

        """
        flag = True
        f = open('recv.jpg','wb')

        while flag :
            data, addr= self.sock.recvfrom(1400000)
            print "data : ", data, " addr : " ,addr
            if "End_File" in data :
                print "recv End"
                break
            while True :
                f.write(data)
                data  = self.sock.recvfrom(1400000)
                if "End_File" in data :
                    break
            flag = False
            f.close()
        """
    def waitVm(self) :
        data = self.closeSocket().recv()

        if "complete" in data :
            return "Vm_Boot_Complete"

    def closeSocket(self) :
        self.sock.close()


