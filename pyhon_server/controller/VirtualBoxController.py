from vboxapi import VirtualBoxManager
#from virtualbox.library import VBoxErrorObjectNotFound
import pywintypes
#import win32com

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

    def findMachine(self,userName) :
        try :
            mach = self.vbox.findMachine(self.machineName)
            #mach = self.vbox.findMachine(self.machineName + "_" + userName)
            return mach
        except pywintypes.com_error :
            print "WRONG MACHINE NAME"

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