package com.enjoyf.platform.util;

import com.google.common.io.Files;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Garrison Please use Google Guava com.google.common.io.Files if
 *         possible
 */
public class FileUtil {

    public static void copyDirFiles(File srcDir, File destDir, boolean recursion)
            throws IOException {
        if (!srcDir.exists()) {
            return;
        }

        File[] srcFiles = srcDir.listFiles();
        for (File srcFile : srcFiles) {
            if (recursion && srcFile.isDirectory()) {
                copyDirFiles(srcFile, new File(destDir, srcFile.getName()), recursion);
            } else {
                Files.copy(srcFile, new File(destDir, srcFile.getName()));
            }
        }
    }

    public static String getFileExtName(String name) {
        int index = name.lastIndexOf(".");
        if (index >= 0) {
            return name.substring(index + 1);
        } else {
            return null;
        }
    }

    public static String SYSTEM_FILE_SEPARATOR = System.getProperty("file.separator");

    public static void createFile(String fileFullName, String fileBodyData)
            throws IOException {
        FileWriter fileWriter = new FileWriter(fileFullName);
        fileWriter.write(fileBodyData);
        fileWriter.close();
    }

    public static void createFile(String fileFullName, byte[] fileBodyData)
            throws FileNotFoundException, IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(fileFullName);
        fileOutputStream.write(fileBodyData);
        fileOutputStream.close();
    }

    public static void createFile(String fileFullName, char[] fileBodyData)
            throws IOException {
        FileWriter fileWriter = new FileWriter(fileFullName);
        fileWriter.write(fileBodyData);
        fileWriter.close();

    }

    public static File[] getDirFiles(String dirPath) {
        if (!isFileOrDirExist(dirPath)) {
            return null;
        }

        File[] returnValue = null;

        File tempDir = new File(dirPath);
        returnValue = tempDir.listFiles();

        return returnValue;
    }

    public static boolean isFileOrDirExist(String fileOrDirName) {
        boolean returnValue = false;

        File fileOrDir = new File(fileOrDirName);
        if (fileOrDir.exists()) {
            returnValue = true;
        }

        fileOrDir = null;

        return returnValue;
    }

    public static boolean createDirectory(String directoryName)
            throws FileNotFoundException {
        return createDirectory(directoryName, SYSTEM_FILE_SEPARATOR, false);
    }

    public static boolean createDirectory(String directoryName,
                                          String dirSeparator, boolean recursion)
            throws FileNotFoundException {
        File direct = null;

        if (recursion) {
            if (dirSeparator == null || "".equals(dirSeparator)) {
                dirSeparator = SYSTEM_FILE_SEPARATOR;
            }

            String[] tempDirs = StringUtil.replace(directoryName, dirSeparator, "/", 1).split("/");
            String curDirName = "";

            if (tempDirs == null || tempDirs.length < 1) {
                throw new FileNotFoundException(
                        "File or direction name is incorrect.");
            }

            for (int i = 0; i < tempDirs.length; i++) {
                curDirName += tempDirs[i] + dirSeparator;
                direct = new File(curDirName);

                if (direct.exists()) {
                    continue;
                }

                if (!direct.mkdir()) {
                    return false;
                }
            }

            return true;
        } else {
            direct = new File(directoryName);

            if (direct.exists()) {
                return true;
            }

            return direct.mkdirs();
        }
    }

    public static boolean deleteFileOrDir(String fileOrDirName)
            throws FileNotFoundException {
        File fileOrDir = new File(fileOrDirName);

        if (fileOrDir.exists()) {
            return fileOrDir.delete();
        } else {
            throw new FileNotFoundException("The file or direction:"
                    + fileOrDirName + " is not exist.");
        }
    }

    public static byte[] readFile(String filename) throws IOException {
        InputStream is = null;
        try {
            File file = new File(filename);
            is = new FileInputStream(file);

            // 获取文件大小
            long length = file.length();

            if (length > Integer.MAX_VALUE) {
                // 文件太大，无法读取
                throw new IOException("File is to large " + file.getName());
            }

            // 创建一个数据来保存文件数据
            byte[] bytes = new byte[(int) length];

            // 读取数据到byte数组中
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }

            // 确保所有数据均被读取
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }
            return bytes;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String readFile(InputStream in, String encoding) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(in, encoding);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\r\n");
            }
            return sb.toString();
        } finally {
            if (isr != null) {
                isr.close();
                isr = null;
            }
            if (br != null) {
                br.close();
                br = null;
            }
        }
    }

    public static void saveFileFromInputStream(InputStream stream, String path, String filename) throws IOException {
        FileOutputStream fs = null;
        int bytesum = 0;
        int byteread = 0;
        try {
            fs = new FileOutputStream(path + "/" + filename);
            byte[] buffer = new byte[1024 * 1024];
            while ((byteread = stream.read(buffer)) != -1) {
                bytesum += byteread;
                fs.write(buffer, 0, byteread);
                fs.flush();
            }
        } catch (IOException e) {
            throw e;
        } finally {
            fs.close();
            stream.close();
        }
    }

    /**
     * 在文件家中找文件，todo 递归未实现 等待你们谁有时间谁改吧
     *
     * @param dir
     * @param key
     * @param notCotainSubDir true不递归找子文件夹
     * @return
     */
    public static List<File> getFileList(String dir, String key, boolean notCotainSubDir) {
        File dirList = new File(dir);
        if (!dirList.isDirectory()) {
            return null;
        }

        List<File> linkedHashSet = new ArrayList<File>();
        for (File f : dirList.listFiles()) {
            if (f.getName().contains(key)) {
                linkedHashSet.add(f);
            }
        }

        return linkedHashSet;
    }

    public static void main(String[] args) {
        System.out.println(getFileList("/opt/servicelogs/pcaccess", "pcaccess.log.20150910", true));
    }

}
