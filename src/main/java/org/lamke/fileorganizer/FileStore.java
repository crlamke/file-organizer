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
 * This class holds all the files being processed by the file organizer app. It
 * will keep a copy of the file information temporarily after processing to
 * avoid duplicate processing in cases where an action triggers a new
 * notification. This is a singleton class. Use the getInstance method to access
 * the object.
 *
 * @author Chris Lamke <https://chris.lamke.org>
 */
public class FileStore {

    private static FileStore fileStoreInstance = null;
    List<FileRecord> files;

    /**
     * Private FileStore constructor because this is a singleton class.
     *
     */
    private FileStore() {
        files = new ArrayList<>();
    }

    /**
     * Public static method to get instance of Config class.
     *
     * @author Chris Lamke <https://chris.lamke.org>
     */
    public static FileStore getInstance() {
        if (fileStoreInstance == null) {
            fileStoreInstance = new FileStore();
        }

        return fileStoreInstance;
    }

    /**
     * addNotification() Add a file notification to the notification collection.
     *
     * @param FileRecord FileRecord to add
     */
    public void addFileRecord(FileRecord fileRecord) {
        files.add(fileRecord);
    }

    /**
     * removeFileRecord() Remove a file Record from notification collection.
     *
     * @param FileRecord FileRecord to remove
     */
    public void removeFileRecord(FileRecord fileRecord) {
        files.remove(fileRecord);
    }

    /**
     * popNotification() Remove the next FileRecord from notification collection
     * and return it.
     *
     */
    public FileRecord popFileRecord() {
        FileRecord fileRecord = null;

        if (!files.isEmpty()) {
            fileRecord = files.get(0);
            files.remove(0);
        }
        return fileRecord;

    }
    
        /**
     * getNotificationCount() Get a count of the file notifications
     * in the collection.
     *
     * @return int number of notifications in this collection
     */
    public int getFileRecordCount() {
        return files.size();
    }

}
