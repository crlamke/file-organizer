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
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;

/**
 * This class determines the type of a file based on the FileType definitions it
 * has. If it can't determine a file type, it sets the type to "UNK".
 *
 * @author Chris Lamke <https://chris.lamke.org>
 */
public class FileTypeDecider {

    // All supported file types are listed here.
    // public enum FileRecord {
    //    GIF, JPG, PNG, WORD, PPT, XLS, TXT, XML, PDF
    //}
    private static FileTypeDecider fileTypeDecider = null;
    private final Logger logger
            = LogManager.getLogger(FileTypeDecider.class.getName());
    Tika tika = null;

    Map fileTypeCodes;

    /**
     * Private FileTypeDecider constructor because this is a singleton class.
     *
     *
     */
    private FileTypeDecider() {
        tika = new Tika();
        fileTypeCodes = new HashMap();
        loadFileTypeCodes();
    }

    /**
     * Public static method to get instance of FileTypeDecider class.
     *
     * @return FileTypeDecider object instance
     */
    public static FileTypeDecider getInstance() {
        if (fileTypeDecider == null) {
            fileTypeDecider = new FileTypeDecider();
        }

        return fileTypeDecider;
    }

    /**
     * Load the translations from the Tika return value into a
     * user-understandable short code.
     *
     * For now, we'll hard code these. Ideally, we'll load these from an
     * external file.
     *
     */
    private void loadFileTypeCodes() {
        fileTypeCodes.put("image/jpeg", "jpg");
        fileTypeCodes.put("image/png", "png");
        fileTypeCodes.put("image/bmp", "bmp");
        fileTypeCodes.put("text/plain", "txt");
        fileTypeCodes.put("application/octet-stream", "UNK");
        fileTypeCodes.put("application/rtf", "rtf");
        fileTypeCodes.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "doc");
        fileTypeCodes.put("application/zip", "zip");
        fileTypeCodes.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xls");

    }

    /**
     * Use Tika to get the file type and return a common string used for the
     * file type, e.g.docx for Word and jpg for jpeg.
     *
     * @param filePath as String containing path on disk to the file to be ID'd
     * @return
     * @throws java.io.IOException
     */
    public String getFileType(String filePath) {
        try {
            String type = tika.detect(new java.io.File(filePath));
            logger.info("Tika: File {} type identified as {}. Returning {}.",
                    filePath, type, type);
            return type;
        } catch (IOException ex) {
            logger.info("Tika Exception: {}", ex);
            return "UNK";
        }
    }

}
