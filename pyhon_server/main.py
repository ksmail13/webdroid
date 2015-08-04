import os
from controller.AdbController import AdbController
from controller.VirtualBoxController import VirtualBoxController

__author__ = 'admin'


def main():
    mVirtualBoxController = VirtualBoxController("Android-x86","")
    mVirtualBoxController.findMachine()
    mVirtualBoxController.getSession()
    mVirtualBoxController.makeProcess()
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
    #mAdbController.checkConnectDevices()





if __name__ == "__main__" :
    try :
        main()
    except os.error, err :
        print str(err)
