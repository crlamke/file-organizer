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

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * This class gets notifications and handles them according to their type and
 * other rules.
 *
 * @author Chris Lamke <https://chris.lamke.org>
 */
public class FileNotification {

    public enum NotificationType {
        CREATE, DELETE, MODIFY, NONE
    }

    public NotificationType notificationType;
    public String filePath;

    public FileNotification(NotificationType notificationType, String filePath) {
        this.notificationType = notificationType;
        this.filePath = filePath;
    }

    public FileNotification() {
        this.notificationType = NotificationType.NONE;
        this.filePath = null;
    }

    public void addFilePath(String path) {
        this.filePath = path;
    }

    public void addNotificationType(NotificationType type) {
        this.notificationType = type;
    }

}
