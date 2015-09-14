from vboxapi import VirtualBoxManager
import sys
#from virtualbox.library import VBoxErrorObjectNotFound
import pywintypes
#import win32com
from controller.VmSocket import VmSocket
from controller.MachineCreator import MachineCreator
__author__ = 'admin'

class VirtualBoxController :
    machineName = "android_x86"

    def __init__(self) :
        self.mgr = VirtualBoxManager(None,None)
        self.vbox = self.mgr.vbox

    def createMachine(self,userName) :
        exMachine = self.vbox.findMachine(self.machineName)
        result = self.vbox.createMachine("",self.machineName + "_" + userName,None,'Linux',"")
        #result.setSettingsFilePath(exMachine.settingsFilePath)
        print "'%s" %(result)

    def findMachine(self,machineNum) :
        try :
            mach = self.vbox.findMachine(self.machineName + "_" + machineNum)
            #mach = self.vbox.findMachine(self.machineName + "_" + userName)
            return mach
        except pywintypes.com_error :
            nMachine = MachineCreator()
            nMachine.createNewMachine(int(machineNum))
            mach = self.vbox.findMachine(self.machineName + "_" + machineNum)

            #print "WRONG MACHINE NAME"
            return mach

    def checkAllMachines(self) :
        for machines in self.mgr.getArray(self.vbox,'machines') :
            print "Machine '%s' " %(machines.name)

    def getSession(self) :
        self.session = self.mgr.getSessionObject(self.vbox)

    def makeProcess(self,mach) :
        self.process = mach.launchVMProcess(self.session,"gui","")
        self.process.waitForCompletion(-1)

    def closeSession(self) :
         self.mgr.closeMachineSession(self.session)

    def connectVmSocket(self,vmIp,vmPort) :
        mVmSocket = VmSocket(vmIp,vmPort)
        mVmSocket.bindSocket()
        mVmSocket.aceeptVm()
        vmReturn = mVmSocket.waitVm()
        print "VmReturn " + vmReturn
        if "Vm_Boot_Complete" in vmReturn :
            return True
        else :
            return False
