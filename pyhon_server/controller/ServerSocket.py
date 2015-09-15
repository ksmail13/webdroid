import os
import thread
from controller.VirtualBoxController import VirtualBoxController
from PIL import Image

__author__ = 'Administrator'

import socket

__author__ = 'admin'
def runVm(machineNum,clientSock) :
    #print "runVm : !!"
    mVirtualBoxController = VirtualBoxController()
    mach = mVirtualBoxController.findMachine(str(machineNum))
    mVirtualBoxController.getSession()
    mVirtualBoxController.makeProcess(mach)
    mVirtualBoxController.closeSession()
    clientSock.send("run_vm"+"#"+machineNum+"#"+machineNum)
    #self.clientDic[split_data[1]] = str(self.machineNum)
    #self.machineNum += 1
    #self.findVM(split_data[1])
    portNum = 1100 + int(machineNum)


class ServerSocket :
    def __init__(self, tcpIp,tcpPort) :
        self.tcpIp = tcpIp
        self.tcpPort = tcpPort
        #self.mVirtualBoxController = VirtualBoxController()

    def initSocket(self):
        self.sock = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
        print "init socket complete"

    def bindSocket(self) :
        self.sock.bind((self.tcpIp,self.tcpPort))
        print "bind complete"

    def recvData(self) :
        flag = True
        self.sock.listen(5)
        self.clientSock , address = self.sock.accept()
        print "accept client , address : " , address

        while True :

            print "Wait Command"
            data = self.clientSock.recv(1024)
            print data

            if "run_vm" in data :
                split_data = data.split("#")
                try :
                    thread.start_new_thread(runVm,(split_data[1],self.clientSock))
                except :
                    print "Thread Error!"
                continue


            if "install_apk" in data :
                split_data = data.split("#")
                #install_apk
                #connect virtualbox and install apk that path is in database
                #data example is install_apk#userKey#c/project/path
                print split_data[1] ,split_data[2]
                self.installApk(split_data[1],split_data[2])
                continue

            if "get_frame_buffer" in data :
                """
              Receive Framebuffer size and send data
              clientSock.send("frame_buffer_start#"+width+height+size)
              Receive FrameBuffer img Stream
              and for i = 0; i < size/buffer_size +1; i++
              clientSock.send(data)
              complete and clientSock.send("frame_buffer_end")

              Image Sending Example
              open example.jpg and send server width,height,size
              and send img
              """
                split_data = data.split("#")
                self.sendFramebuffer(split_data[1])


    def sendFramebuffer(self,userKey) :
        """
              Receive Framebuffer size and send data
              clientSock.send("frame_buffer_start#"+width+height+size)
              Receive FrameBuffer img Stream
              and for i = 0; i < size/buffer_size +1; i++
              clientSock.send(data)
              complete and clientSock.send("frame_buffer_end")

              Image Sending Example
              open example.jpg and send server width,height,size
              and send img
        """
        image = Image.open('example.jpg')
        (width,height) = image.size
        size = os.path.getsize('example.jpg')
        self.clientSock.send("frame_buffer_start#"+userKey+"#"+str(width)+"#"+str(height)+"#"+str(size))

        image = open('example.jpg','rb')
        n_roof = size/1024 + 1

        for i in range(n_roof) :
            data = "frame_buffer#"+userKey+"#"
            data += image.read(1024)
            print data
            send_data = data.__sizeof__()
            self.clientSock.send(data)
        image.close()

        self.clientSock.send("frame_buffer_end#"+userKey)
        print "send complete"


    def closeSocket(self) :
        self.sock.close()


