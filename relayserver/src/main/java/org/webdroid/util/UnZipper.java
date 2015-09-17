package org.webdroid.util;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Seho on 2015-09-15.
 */
public class UnZipper {

    protected ConsoleLogger logger = ConsoleLogger.createLogger(getClass());

    public static void unZipper(String INPUT_ZIP_FILE, String OUTPUT_FOLDER)
    {
        UnZipper unZip = new UnZipper();
        unZip.unZipIt(INPUT_ZIP_FILE,OUTPUT_FOLDER);
    }

    /**
     * Unzip it
     * @param zipFileName input zip file
     * @param outputFolderName zip file output folder
     */
    public void unZipIt(String zipFileName, String outputFolderName){

        String zipFile = "user-upload\\projects\\" + zipFileName;
        String outputFolder = "user-upload\\projects\\" + outputFolderName;
        byte[] buffer = new byte[1024];
        FileOutputStream fos = null;
        ZipInputStream zis = null;
        boolean fos_sw = false;
        boolean zis_sw = false;

        try{

            //create output directory is not exists
            File folder = new File(outputFolder);
            if(!folder.exists()){
                folder.mkdir();
            }

            //get the zip file content
            zis = new ZipInputStream(new FileInputStream(zipFile));
            zis_sw = true;
            logger.debug("unzip start");

            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();
            while(ze!=null){

                String fileName = ze.getName();
                File newFile = new File(outputFolder + File.separator + fileName);

                //System.out.println("file unzip : "+ newFile.getAbsoluteFile());

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                fos = new FileOutputStream(newFile);
                fos_sw = true;

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            logger.debug("unzip done");

            File zip_origin = new File(zipFile);
            zip_origin.delete();

        }catch(IOException e){
            logger.debug("unzip failed");
            e.printStackTrace();
            try {
                if(fos_sw) fos.close();
                if(zis_sw) {
                    zis.closeEntry();
                    zis.close();
                }
            }catch (IOException ex){}
        }
    }
}
