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

import java.util.logging.Logger;

/**
 * This class represents a file (including a directory) on the file system.
 *
 * @author Chris Lamke <https://chris.lamke.org>
 */
public class FileRecord {

    boolean isDir = false;
    // All supported file types are listed here.
    //public enum FileRecord {
    //    GIF, JPG, PNG, WORD, PPT, XLS, TXT, XML, PDF
    //}
    String fileTypeName = "Unknown";
    String fileTypeDesc = "Unknown";
    String createFileAction = "Move file to new location";
    String modifyFileAction = "Log that file was modified";
    String deleteFileAction = "Log that file was deleted";

    public FileRecord() {

    }

}
