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

import java.util.ArrayList;
import java.util.List;

/**
 * This class will hold a collection of FileNotification objects. Its purpose is
 * to serve as a container to hold File Notifications for processing.
 *
 * @author Chris Lamke <https://chris.lamke.org>
 */
public class FileNotificationCollection {

    List<FileNotification> notifications;

    public FileNotificationCollection() {
        notifications = new ArrayList<>();
    }

    /**
     * addNotification() Add a file notification to the notification collection.
     *
     * @param FileNotification file notification to add
     */
    public void addNotification(FileNotification notification) {
        notifications.add(notification);
    }

    /**
     * removeNotification() Remove a file notification from notification
     * collection.
     *
     * @param FileNotification file notification to remove
     */
    public void removeNotification(FileNotification notification) {
        notifications.remove(notification);
    }

    /**
     * popNotification() Remove the next file notification from notification
     * collection and return it.
     *
     */
    public FileNotification popNotification() {
        FileNotification notification = null;

        if (!notifications.isEmpty()) {
            notification = notifications.get(0);
            notifications.remove(0);
        }
        return notification;

    }

    /**
     * getNotificationCount() Get a count of the file notifications
     * in the collection.
     *
     * @return int number of notifications in this collection
     */
    public int getNotificationCount() {
        return notifications.size();
    }

}
