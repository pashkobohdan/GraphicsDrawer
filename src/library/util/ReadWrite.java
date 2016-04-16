package library.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Bohdan Pashko on 08.04.2016.
 */
public class ReadWrite {
    public static String readMainLanguage(String path) {
        if(!new File(path).exists()){
            return null;
        }
        int length = (int) (new File(path).length());
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            byte[] bytes = new byte[length];
            fileInputStream.read(bytes);

            return new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeMainLanguage(String text, String path) {

        Path logFile = Paths.get(path);
        try (BufferedWriter writer = Files.newBufferedWriter(logFile, StandardCharsets.UTF_16)) {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try (FileOutputStream fileOutputStream = new FileOutputStream(path)) {
//            fileOutputStream.write(text.getBytes());
//            fileOutputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
