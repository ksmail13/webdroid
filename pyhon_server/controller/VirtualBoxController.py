from vboxapi import VirtualBoxManager
import win32com

__author__ = 'admin'

class VirtualBoxController :
    machineName = ""
    userName = ""

    def __init__(self,machineName,userName) :
        self.machineName = machineName
        self.userName = userName
        self.mgr = VirtualBoxManager(None,None)
        self.vbox = self.mgr.vbox

    def findMachine(self) :
        self.mach = self.vbox.findMachine(self.machineName)

    def getSession(self) :
        self.session = self.mgr.mgr.getSessionObject(self.vbox)

    def makeProcess(self) :
        self.process = self.mach.launchVMProcess(self.session,"gui","")
        self.process.waitForCompletion(-1)

    def closeSession(self) :
         self.mgr.closeMachineSession(self.session)