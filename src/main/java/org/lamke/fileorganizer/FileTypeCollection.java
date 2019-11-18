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

/**
 * This class holds all the FileTypeDefinition objects and provides a way to
 * retrieve a FileTypeDefinition based on its name and other properties. This is
 * a singleton class. Use the getInstance method to access the object.
 *
 * @author Chris Lamke <https://chris.lamke.org>
 */
public class FileTypeCollection {

    private static FileTypeCollection fileTypeCollectionInstance = null;

    List<FileTypeDefinition> fileTypes;

    /**
     * Private FileTypeCollection constructor because this is a singleton class.
     *
     */
    private FileTypeCollection() {
        fileTypes = new ArrayList<>();
    }

    /**
     * Public static method to get instance of FileTypeCollection class.
     *
     * @author Chris Lamke <https://chris.lamke.org>
     */
    public static FileTypeCollection getInstance() throws IOException {
        if (fileTypeCollectionInstance == null) {
            fileTypeCollectionInstance = new FileTypeCollection();
        }

        return fileTypeCollectionInstance;
    }

    /**
     * addFileType() Add a file type to the type collection.
     *
     * @param FileTypeDefinition file type to add
     */
    public void addFileType(FileTypeDefinition fileType) {
        fileTypes.add(fileType);
    }

    /**
     * removeNotification() Remove a file type from type collection.
     *
     * @param FileTypeDefinition file type to remove
     */
    public void removeFileType(FileTypeDefinition fileType) {
        fileTypes.remove(fileType);
    }

    /**
     * popNotification() Remove the next file notification from notification
     * collection and return it.
     *
     */
    public FileTypeDefinition getFileType() {
        FileTypeDefinition fileTypeDef = null;

        return fileTypeDef;

    }

    /**
     * getFileTypeCount() Get a count of the file types in the collection.
     *
     * @return int number of file types in this collection
     */
    public int getFileTypeCount() {
        return fileTypes.size();
    }

}
