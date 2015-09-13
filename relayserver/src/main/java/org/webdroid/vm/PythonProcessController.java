package org.webdroid.vm;

import java.io.*;

/**
 * Created by Administrator on 2015-09-13.
 */
public class PythonProcessController {
    ProcessBuilder builder;
    BufferedReader input;
    BufferedReader error;
    BufferedWriter out;
    Process process;

    public boolean startProcess() {
        try {
            // builder = new ProcessBuilder("cmd", "/c", "cd \"C:\\공개소프트웨어\\pyhon_server\" && python", "main.py");
            builder = new ProcessBuilder("python", "C:\\gongmo\\pyhon_server\\main.py");
            //Runtime runtime = Runtime.getRuntime();
            builder.redirectErrorStream(true);
            //process = runtime.exec("python C:\\공개소프트웨어\\pyhon_server\\main.py");
            process = builder.start();
            printOutput();
            return true;
        } catch (IOException e) {
            System.out.println(e.toString());
            return false;
        }
    }

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
}
