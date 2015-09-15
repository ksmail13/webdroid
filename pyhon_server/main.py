import os

from controller.ServerSocket import ServerSocket

__author__ = 'admin'

def main() :
    mServerSocket = ServerSocket("211.243.108.156",1113)
    mServerSocket.initSocket()
    mServerSocket.bindSocket()
    mServerSocket.recvData()
    mServerSocket.closeSocket()
    """
    mVirtualBoxController = VirtualBoxController()
    flag = True
    machineDic = {}
    print "On"
    while flag :
        #print "Wait Command"
        sys.stdout.flush()

        command = raw_input()
        split_input = command.split()
        #print command
        sys.stdout.flush()

        if "exit" in command :
            flag = False

        if "run_vm" in command :
            if len(split_input) == 1:
                print "run_vm command needs machineNumber"
                continue
            mach = mVirtualBoxController.findMachine(split_input[1])
            mVirtualBoxController.getSession()
            mVirtualBoxController.makeProcess(mach)
            mVirtualBoxController.closeSession()
            machineDic[split_input[1]] = mach

        if "check_vm" in command :
            mVirtualBoxController.checkAllMachines()

        if "connect_vm" in command :
            if len(split_input) == 1:
                print "connect_vm command needs machineNumber"
                continue
            portNum = 1100 + int(split_input[1])
            if mVirtualBoxController.connectVmSocket("211.243.108.156",5555) :
                print "Vm_Boot_Complete " + str(portNum)
            else :
                print "Vm_Boot_Fail"


    mServerSocket = ServerSocket("211.243.108.156",1112)
    mServerSocket.initSocket()
    mServerSocket.bindSocket()
    mServerSocket.recvData()
    mServerSocket.closeSocket()


    mVirtualBoxController = VirtualBoxController()
    #mVirtualBoxController.checkAllMachines()
    machine = mVirtualBoxController.findMachine("1")

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
