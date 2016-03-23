/*
 * Copyright 2016 Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onosproject.yangutils.utils.io.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.onosproject.yangutils.utils.UtilConstants;

/**
 * Utility to handle file system operations.
 */
public final class FileSystemUtil {

    /**
     * Hiding constructor of a utility class.
     */
    private FileSystemUtil() {
    }

    /**
     * Check if the package directory structure created.
     *
     * @param pkg Package to check if it is created
     * @return existence status of package
     */
    public static boolean doesPackageExist(String pkg) {

        File pkgDir = new File(pkg.replace(UtilConstants.PERIOD, UtilConstants.SLASH));
        File pkgWithFile = new File(pkgDir + File.separator + "package-info.java");
        if (pkgDir.exists() && pkgWithFile.isFile()) {
            return true;
        }
        return false;
    }

    /**
     * Create a package structure with package info java file if not present.
     *
     * @param pkg java package string
     * @param pkgInfo description of package
     * @throws IOException any IO exception
     */
    public static void createPackage(String pkg, String pkgInfo) throws IOException {

        if (!doesPackageExist(pkg)) {
            try {
                File pack = YangIoUtils.createDirectories(pkg);
                YangIoUtils.addPackageInfo(pack, pkgInfo, pkg.replace(UtilConstants.SLASH, UtilConstants.PERIOD));
            } catch (IOException e) {
                throw new IOException("failed to create package-info file");
            }
        }
    }

    /**
     * Read the contents from source file and append its contents to append
     * file.
     *
     * @param toAppend destination file in which the contents of source file is
     *            appended
     * @param srcFile source file from which data is read and added to to append
     *            file
     * @throws IOException any IO errors
     */
    public static void appendFileContents(File toAppend, File srcFile) throws IOException {

        updateFileHandle(srcFile,
                UtilConstants.NEW_LINE + readAppendFile(toAppend.toString(), UtilConstants.FOUR_SPACE_INDENTATION),
                false);
        return;
    }

    /**
     * Reads file and convert it to string.
     *
     * @param toAppend file to be converted
     * @param spaces spaces to be appended
     * @return string of file
     * @throws IOException when fails to convert to string
     */
    public static String readAppendFile(String toAppend, String spaces) throws IOException {

        FileReader fileReader = new FileReader(toAppend);
        BufferedReader bufferReader = new BufferedReader(fileReader);
        try {
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferReader.readLine();

            while (line != null) {
                if (line.equals(UtilConstants.SPACE) | line.equals(UtilConstants.EMPTY_STRING)
                        | line.equals(UtilConstants.EIGHT_SPACE_INDENTATION)
                        | line.equals(UtilConstants.MULTIPLE_NEW_LINE)) {
                    stringBuilder.append(UtilConstants.NEW_LINE);
                } else if (line.equals(UtilConstants.FOUR_SPACE_INDENTATION)) {
                    stringBuilder.append(UtilConstants.EMPTY_STRING);
                } else {
                    stringBuilder.append(spaces + line);
                    stringBuilder.append(UtilConstants.NEW_LINE);
                }
                line = bufferReader.readLine();
            }
            return stringBuilder.toString();
        } finally {
            fileReader.close();
            bufferReader.close();
        }
    }

    /**
     * Update the generated file handle.
     *
     * @param inputFile input file
     * @param contentTobeAdded content to be appended to the file
     * @param isClose when close of file is called.
     * @throws IOException if the named file exists but is a directory rather
     *             than a regular file, does not exist but cannot be created, or
     *             cannot be opened for any other reason
     */
    public static void updateFileHandle(File inputFile, String contentTobeAdded, boolean isClose) throws IOException {

        FileWriter fileWriter = new FileWriter(inputFile, true);
        PrintWriter outputPrintWriter = new PrintWriter(fileWriter, true);
        if (!isClose) {
            outputPrintWriter.write(contentTobeAdded);
            outputPrintWriter.flush();
            outputPrintWriter.close();
        } else {
            fileWriter.flush();
            fileWriter.close();
        }
    }
}
