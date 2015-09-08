import os
from controller.AdbController import AdbController
from controller.VirtualBoxController import VirtualBoxController
from PIL import Image

__author__ = 'Administrator'

import socket

__author__ = 'admin'

class ServerSocket :

    def __init__(self, tcpIp,tcpPort) :
        self.tcpIp = tcpIp
        self.tcpPort = tcpPort
        self.sock = socket.socket(socket.AF_INET,socket.SOCK_STREAM)

    def bindSocket(self) :
        self.sock.bind((self.tcpIp,self.tcpPort))
        print "bind complete"
    def recvData(self) :

        flag = True
        self.sock.listen(1)
        clientSock , address = self.sock.accept()
        print "accept client , address : " + address
        while True :
            data = clientSock.recv(1024)
            print data

            if "run_vm" in data :

                #run_vm starting VritualBox name from JavaServer
                split_data = data.split("#")
                #data example is run_vm@userEmail
                mVirtualBoxController = VirtualBoxController(split_data[1])
                #mVirtualBoxController.checkAllMachines()
                #data = clientSock.recv(1024)
                machine = mVirtualBoxController.findMachine()

                if not machine :
                    #Not Found User's Virtual Machine -> Create Virtual Machine
                    #mVirtualBoxController.createMachine(split_data[1])
                    clientSock.send("run_vm_fail#find machine fail")
                    continue
                print split_data[1] , machine

                mVirtualBoxController.getSession()
                mVirtualBoxController.makeProcess(machine)
                mVirtualBoxController.closeSession()
                """
              connect python sever and VmSocket
              wait when virtualbox's C socket send Power On Complete signal
              if Not receive complete signal clientSock.send("run_vm_fail#c_connect")
              """
                clientSock.send("run_vm_success")
                continue


            if "install_apk" in data :
                split_data = data.split("#")
                #install_apk
                #connect virtualbox and install apk that path is in database
                #data example is install_apk#c/project/path
                print split_data[1]
                mAdbController = AdbController("10.0.2.15/24","","5555")
                mAdbController.killServer()

                mAdbController.startServer()
                mAdbController.setTcpip()

                connectFlag = mAdbController.connectVirtualbox()
                if connectFlag == True :
                    #connect virtualbox success and install apk
                    mAdbController.checkConnectDevices()
                    #mAdbController.installApkInVirtualmachine(split_data[1])
                    mAdbController.installApkInVirtualmachine("app-debug.apk")
                    send_data = "install_apk_success"
                    clientSock.send(send_data)
                else :
                    print 'Connect Devices False!!' + '\n'
                    send_data = "install_apk_error#connect devices"
                    clientSock.send(send_data)
                    #break
                #mAdbController.checkConnectDevices()


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
                image = Image.open('example.jpg')
                (width,height) = image.size
                size = os.path.getsize('example.jpg')
                clientSock.send("frame_buffer_start#"+width+"#"+height+"#"+size)

                image = open('example.jpg','rb')
                n_roof = size/1024 + 1

                for i in range(n_roof) :
                    data = image.read(1024)
                    clientSock.send(data)
                image.close()

                clientSock.send("frame_buffer_end")

                continue


            if "close_sock" in data :
                flag = False
                break


    def closeSocket(self) :
        self.sock.close()


