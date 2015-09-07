from controller.AdbController import AdbController
from controller.VirtualBoxController import VirtualBoxController

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

    def recvData(self) :

        flag = True
        self.sock.listen(1)
        clientSock , address = self.sock.accept()
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
                print split_data[1] , machine
                #mVirtualBoxController.createMachine("user1")
                mVirtualBoxController.getSession()
                mVirtualBoxController.makeProcess(machine)
                mVirtualBoxController.closeSession()
                """
                connect python sever and VmSocket
                wait when virtualbox's C socket send Power On Complete signal
             """
                clientSock.send("success")
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
                    send_data = "succes install_apk"
                else :
                    print 'Connect Devices False!!' + '\n'
                    send_data = "connect devices fail"
                mAdbController.checkConnectDevices()
                clientSock.send(send_data)
                continue


            if "close_sock" in data :
                flag = False
                break


    def closeSocket(self) :
        self.sock.close()


