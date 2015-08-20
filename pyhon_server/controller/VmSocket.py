from collections import namedtuple
import socket
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

    def recvData(self) :

        flag = True
        self.sock.listen(1)
        #f = open('recv.jpg','wb')

        while flag :
            clientSock , address = self.sock.accept()
            while True :
                data = clientSock.recv(1400000)
                print data
                if "End_File" in data :
                    flag = False
                    break
                #f.write(data)
        #f.close()

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

    def closeSocket(self) :
        self.sock.close()


