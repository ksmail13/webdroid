package org.webdroid.util;

import java.io.*;
import java.sql.ResultSet;
import java.util.Arrays;

/**
 * Created by Seho on 2015-08-30.
 */
public class JqueryFileTree {
    public static String createHtmlRes(String dir, String root){
        if (dir == null) {
            return "";
        }

        String htmlRes = "";
        String subRoot = dir.substring(root.length());

        if (dir.charAt(dir.length()-1) == '\\') {
            dir = dir.substring(0, dir.length()-1) + "/";
        } else if (dir.charAt(dir.length()-1) != '/') {
            dir += "/";
        }

        //dir = java.net.URLDecoder.decode(dir, "UTF-8");

        if (new File(dir).exists()) {
            String[] files = new File(dir).list(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.charAt(0) != '.';
                }
            });
            Arrays.sort(files, String.CASE_INSENSITIVE_ORDER);
            htmlRes += "<ul class=\"jqueryFileTree\" style=\"display: none;\">";
            // All dirs
            for (String file : files) {
                if (new File(dir, file).isDirectory()) {
                    htmlRes += "<li class=\"directory collapsed\"><a href=\"#\" rel=\"" + subRoot + file + "/\">"
                            + file + "</a></li>";
                }
            }
            // All files
            for (String file : files) {
                if (!new File(dir, file).isDirectory()) {
                    int dotIndex = file.lastIndexOf('.');
                    String ext = dotIndex > 0 ? file.substring(dotIndex + 1) : "";
                    htmlRes += "<li class=\"file ext_" + ext + "\"><a href=\"#\" rel=\"" + subRoot + file + "\">"
                            + file + "</a></li>";
                }
            }
            htmlRes += "</ul>";
        }

        return htmlRes;
    }

    public static String openFileFromTree(String filepath, String path){

        String innerText = "";
        String p_path = path;//"/Users/Y/Documents/webdroid/";

            try {
                BufferedReader in = new BufferedReader(new FileReader(p_path + filepath));
                String s;

                while ((s = in.readLine()) != null) {
                    innerText += s + "\n";
                }
                in.close();

            } catch (IOException e) {
                System.err.println(e);
                System.exit(1);
            }

            return innerText;
    }

    public static String fileExt(String filepath){
        String filename = filepath.substring(filepath.lastIndexOf("/")+1);
        String ext = filename.substring(filename.lastIndexOf(".")+1);

        return ext;
    }
}
