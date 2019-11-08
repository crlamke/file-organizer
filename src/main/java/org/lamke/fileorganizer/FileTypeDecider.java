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
import java.util.ArrayList;
import java.util.List;
import org.apache.tika.Tika;

/**
 * This class determines the type of a file based on the FileType definitions it
 * has. If it can't determine a file type, it sets the type to "UNK".
 *
 * @author Chris Lamke <https://chris.lamke.org>
 */
public class FileTypeDecider {

    // Create FileTypes from rules loaded into Config
    Tika tika = null;

    /**
     * This class manages the application log file(s) and provides a generic
     * logging interface that will allow changing the underlying log engine
     * without refactoring lots of code.
     *
     */
    public FileTypeDecider(Config configuration) throws IOException {

        tika = new Tika();

    }

    /**
     * This class manages the application log file(s) and provides a generic
     * logging interface that will allow changing the underlying log engine
     * without refactoring lots of code.
     *
     * @param filePath as String containing path on disk to the file to be ID'd
     * @author Chris Lamke <https://chris.lamke.org>
     * @throws java.io.IOException
     */
    public String getFileType(String filePath) throws IOException {
        String type = tika.detect(new java.io.File(filePath));
        System.out.println(filePath + ": " + type);
        return filePath;
    }

}
