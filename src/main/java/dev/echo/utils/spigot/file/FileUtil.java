package dev.echo.utils.spigot.file;

import java.io.*;

public class FileUtil {

    public static void copy(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            if (!destination.exists())
                destination.mkdir();

            String[] files = source.list();
            if (files == null) return;

            for (String file : files) {
                File newSource = new File(source, file);
                File newDestination = new File(destination, file);
                copy(newSource, newDestination);
            }
        } else {
            InputStream in = new FileInputStream(source);
            OutputStream out = new FileOutputStream(source);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        }
    }
    public static void delete(File file){
        if(file.isDirectory()){
            if(file.listFiles() == null) return;
            for(File child : file.listFiles()){
                delete(child);
            }
        }
        file.delete();
    }

}
