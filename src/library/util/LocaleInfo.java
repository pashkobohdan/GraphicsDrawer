package library.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Information about main language of program.
 *
 */
public class LocaleInfo {
//    public String localePath = System.getProperty("user.dir"); // "/src/bundles"
//    public String mainLanguageFilePath = System.getProperty("user.dir"); //"/src/settings/mainLanguage.txt"
//
//
//    /**
//     * Main constructor  (recommended)
//     *
//     * @param localePath path with .properties files(for example /src/bundles)
//     * @param mainLanguageFilePath file, what save main language (for example /src/settings/mainLanguage.txt)
//     */
//    public LocaleInfo(@NotNull String localePath, @NotNull String mainLanguageFilePath) {
//        this.localePath += localePath;
//        this.mainLanguageFilePath += mainLanguageFilePath;
//    }
//
//    /**
//     *
//     * @return count of file on localePath (path with .properties files)
//     */
//    public int countLanguages() {
//        return new File(localePath).listFiles().length;
//    }
//
//    /**
//     *
//     * @return list of file on localePath (path with .properties files)
//     */
//    public List<String> languagesList() {
//        return Arrays.stream(new File(localePath).listFiles())
//                .filter((a) -> a.getName().contains(".properties"))
//                .map((a) -> a.getName().substring(a.getName().indexOf("_") + 1, a.getName().lastIndexOf(".")))
//                .collect(Collectors.toList());
//    }
//
//    /**
//     *
//     * @return name of main language
//     */
//    public String readMainLanguage() {
//
//        int length = (int) (new File(mainLanguageFilePath).length());
//        try (FileInputStream fileInputStream = new FileInputStream(mainLanguageFilePath)) {
//            byte[] bytes = new byte[length];
//            fileInputStream.read(bytes);
//
//            return new String(bytes);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * Write the main language to the mainLanguageFile
//     *
//     * @param currentLanguage
//     */
//    public void writeMainLanguage(String currentLanguage) {
//        try (FileOutputStream fileOutputStream = new FileOutputStream(mainLanguageFilePath)) {
//            fileOutputStream.write(currentLanguage.getBytes());
//            fileOutputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static final String LOCALE_PATH = System.getProperty("user.dir") + "/src/bundles";
    public static final String MAIN_LANGUAGE_PATH = System.getProperty("user.dir") + "/src/settings/mainLanguage.txt";

    public static int countLanguages() {
        return new File(LOCALE_PATH).listFiles().length;
    }

    public static List<String> languagesList() {
        return Arrays.stream(new File(LOCALE_PATH).listFiles())
                .filter((a) -> a.getName().contains(".properties"))
                .map((a) -> a.getName().substring(a.getName().indexOf("_") + 1, a.getName().lastIndexOf(".")))
                .collect(Collectors.toList());
    }

    public static String readMainLanguage() {

        int length = (int) (new File(MAIN_LANGUAGE_PATH).length());
        try (FileInputStream fileInputStream = new FileInputStream(MAIN_LANGUAGE_PATH)) {
            byte[] bytes = new byte[length];
            fileInputStream.read(bytes);

            return new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeMainLanguage(String currentLanguage) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(MAIN_LANGUAGE_PATH)) {
            fileOutputStream.write(currentLanguage.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
