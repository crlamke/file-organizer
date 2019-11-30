/*
 * The MIT License
 *
 * Copyright 2019 Chris Lamke <https://chris.lamke.org>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.lamke.fileorganizer;

import java.io.IOException;
import java.nio.file.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class handles moving of files and directories. It will submit move ops
 * to a temporary store that will be referenced by the notifier to determine
 * whether this app caused the notification by moving a file.
 *
 * @author Chris Lamke <https://chris.lamke.org>
 */
public class FileSystemUtilities {

    private static FileSystemUtilities fileUtilitiesInstance = null;

    private final Logger logger = LogManager.getLogger(FileSystemUtilities.class.getName());

    /**
     * Private FileUtilities constructor because this is a singleton class.
     *
     */
    private FileSystemUtilities() {

    }

    /**
     * Public static method to get instance of FileSystemUtilities class.
     *
     * @return FileSystemUtilities object instance
     */
    public static FileSystemUtilities getInstance() {
        if (fileUtilitiesInstance == null) {
            fileUtilitiesInstance = new FileSystemUtilities();
        }

        return fileUtilitiesInstance;
    }

    /**
     * Move a file from one location to another on the local file system.
     *
     * @param source source path of file to be moved
     * @param dest destination path for file move
     * @return boolean true if move succeeded, false otherwise
     */
    public boolean moveFile(String source, String dest) {
        boolean moveStatus = false;
        Path result = null;
        try {
            result = Files.move(Paths.get(source), Paths.get(dest));
        } catch (IOException e) {
            logger.error("Exception during attempt to move: " + e.getMessage());
        }
        if (result != null) {
            moveStatus = true;
            logger.debug("File {} moved from {} to {}", "file", source, dest);
        } else {
            logger.error("File {} not moved from {} to {}",
                    "file", source, dest);
        }
        return moveStatus;

    }

        /**
     * Copy a file from one location to another on the local file system.
     *
     * @param source source path of file to be moved
     * @param dest destination path for file move
     * @return boolean true if move succeeded, false otherwise
     */
    public boolean copyFile(String source, String dest) {
        boolean copyStatus = false;
        Path result = null;
        try {
            result = Files.copy(Paths.get(source), Paths.get(dest));
        } catch (IOException e) {
            logger.error("Exception during attempt to copy: " + e.getMessage());
        }
        if (result != null) {
            copyStatus = true;
            logger.debug("File {} copied from {} to {}", "file", source, dest);
        } else {
            logger.error("File {} not copied from {} to {}",
                    "file", source, dest);
        }
        return copyStatus;

    }
    
    /**
     * Get the file name from a file path.
     *
     * @param path Path to extract file name from
     * @return String file name
     */
    public String getFileNameFromPath(String path) {
        String fileName = null;

        if (path != null) {
            Path pathToTest = Paths.get(path);
            fileName = pathToTest.getFileName().toString();
        }
        return fileName;
    }

    /**
     * Test whether a file/path exists.
     *
     * @param path Path to test
     * @return boolean true if path exists, false otherwise
     */
    public boolean filePathExists(String path) {
        boolean pathExists = false;

        if (path != null) {
            Path pathToTest = Paths.get(path);
            pathExists = Files.exists(pathToTest);
        }
        return pathExists;
    }

    /**
     * Test whether a file/path is a directory.
     *
     * @param path Path to test
     * @return boolean true if path is directory, false otherwise
     */
    public boolean isDirectory(String path) {
        boolean isDir = false;

        if (path != null) {
            Path pathToTest = Paths.get(path);
            isDir = Files.isDirectory(pathToTest);
        }
        return isDir;
    }

}
