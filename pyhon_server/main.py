import os

from PIL import Image
from controller.AdbController import AdbController
from controller.ServerSocket import ServerSocket
from controller.VirtualBoxController import VirtualBoxController

from controller.VmSocket import VmSocket

__author__ = 'admin'


def main():

    mServerSocket = ServerSocket("192.168.123.101",1111)
    mServerSocket.bindSocket()
    mServerSocket.recvData()
    mServerSocket.closeSocket()

    """
    mVirtualBoxController = VirtualBoxController("user1")
    #mVirtualBoxController.checkAllMachines()
    machine = mVirtualBoxController.findMachine()
    if not machine :
        print "Machine not found!"
        print machine
    else :
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

    image = Image.open('example.jpg')
    (width,height) = image.size
    size = os.path.getsize('example.jpg')

    print width,height,size

    image = open('example.jpg','rb')
    out = open('exam2.jpg','wb')
    n_roof = size/1024 +1
    for i in range(n_roof) :
        data = image.read(1024)
        print data
        out.write(data)

    image.close()
    out.close()
    """
if __name__ == "__main__" :
    try :
        main()
    except os.error, err :
        print str(err)
