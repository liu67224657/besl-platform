package com.enjoyf.platform.util.htmlparse;

import java.io.*;

/**
 * @author Garrison Please use Google Guava com.google.common.io.Files if
 *         possible
 */
class FileUtil {

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

}
