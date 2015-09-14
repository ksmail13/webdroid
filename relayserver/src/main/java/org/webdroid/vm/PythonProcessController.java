package org.webdroid.vm;

import java.io.*;

/**
 * Created by Administrator on 2015-09-13.
 */
public class PythonProcessController {
    ProcessBuilder builder;
    InputStream input;
    OutputStreamWriter out;
    Process process;

    public boolean startProcess() {
        try {
            // builder = new ProcessBuilder("cmd", "/c", "cd \"C:\\공개소프트웨어\\pyhon_server\" && python", "main.py");
            builder = new ProcessBuilder("python", "C:\\gongmo\\pyhon_server\\main.py");
            //Runtime runtime = Runtime.getRuntime();
            builder.redirectErrorStream(true);
            //process = runtime.exec("python C:\\공개소프트웨어\\pyhon_server\\main.py");
            process = builder.start();
            input = process.getInputStream();
            out = new OutputStreamWriter(process.getOutputStream());
            new InputThread(input).run();
            return true;
        } catch (IOException e) {
            System.out.println(e.toString());
            return false;
        }
    }
    /*
    public boolean inputCommand(String cmd){
        out = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        try {
            out.write(cmd);
            System.out.println("java:" + cmd);
            out.flush();
            //printOutput();
            return true;
        }catch(IOException e){
            System.out.println("inputCommandError!");
            return false;
        }
    }

    public boolean printOutput(){
        BufferedReader reader;
        InputStreamReader inputReader = new InputStreamReader(process.getInputStream());
        InputStreamReader errorReader = new InputStreamReader(process.getErrorStream());
        String str;

        try {
            reader = new BufferedReader(errorReader);
            while (true) {
                str = reader.readLine();
                if(str == null){
                    break;
                }
                System.out.println(str);
            }
            reader = new BufferedReader(inputReader);
            str = reader.readLine();
            while (str!=null&&!str.contains("Wait Command")) {
                System.out.println(str);
                str = reader.readLine();
            }
            System.out.println("end read output stream");
            return true;
        } catch(IOException e){
            e.printStackTrace();
            System.out.println("printOutputError!");
            return false;
        }
    }
    */
    class OutputThread extends Thread{
        BufferedWriter writer;
        String cmd;
        public OutputThread(OutputStreamWriter outputStream,String cmd) {
            this.writer = new BufferedWriter(outputStream);
            this.cmd = cmd;
        }
        public void run() {
             try {
                this.writer.write(cmd);
                this.writer.flush();
            }catch(IOException e) {
                e.printStackTrace();
                System.out.println("OutputThreadError!");
            }
        }
    }
    class InputThread extends Thread{
        InputStreamReader inputReader;
        BufferedReader reader;
        public InputThread(InputStream inputStream) {
            this.inputReader = new InputStreamReader(inputStream);
            this.reader = new BufferedReader(this.inputReader);
        }
        public void run() {
            String str;
            try {
                str = reader.readLine();
                while (str!=null&&!str.contains("Wait Command")) {
                    if(str.contains("On")){
                        new OutputThread(out,"run_vm 1");
                    }
                    System.out.println(str);
                    str = reader.readLine();
                }
                System.out.println("end read output stream");
            }catch(IOException e) {
                e.printStackTrace();
                System.out.println("printOutputError!");
            }
        }
    }
}
