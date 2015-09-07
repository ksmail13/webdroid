import os

from controller.AdbController import AdbController
from controller.ServerSocket import ServerSocket
from controller.VirtualBoxController import VirtualBoxController

from controller.VmSocket import VmSocket

__author__ = 'admin'


def main():
    mServerSocket = ServerSocket("211.243.108.156",1113)
    mServerSocket.bindSocket()
    mServerSocket.recvData()
    mServerSocket.closeSocket()
    """
    mVirtualBoxController = VirtualBoxController()
    #mVirtualBoxController.checkAllMachines()
    machine = mVirtualBoxController.findMachine("user1")
    #mVirtualBoxController.createMachine("user1")
    mVirtualBoxController.getSession()
    mVirtualBoxController.makeProcess(machine)
    mVirtualBoxController.closeSession()


    mAdbController = AdbController("10.0.2.15/24","","5555")

    mAdbController.killServer()

    mAdbController.startServer()
    mAdbController.setTcpip()

    connectFlag = mAdbController.connectVirtualbox()
    if connectFlag == True :
        mAdbController.checkConnectDevices()
        mAdbController.installApkInVirtualmachine("C:/Users/admin/AndroidStudioProjects/MyApplication/app/build/outputs/apk/app-debug.apk")
    else :
        print 'Connect Devices False!!' + '\n'
    mAdbController.checkConnectDevices()


    mVmSocket = VmSocket("211.243.108.156",5554)
    mVmSocket.bindSocket()
    mVmSocket.recvData()
    mVmSocket.closeSocket()
    """

if __name__ == "__main__" :
    try :
        main()
    except os.error, err :
        print str(err)
