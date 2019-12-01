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
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author chris
 */
public class AppMain {

    private Logger logger = LogManager.getLogger(AppMain.class.getName());
    private TaskQueue tasks;
    private FileSystemWatcher fileWatcher;
    FileRecordCollection files;
    FileSystemUtilities fileUtilities;

    /**
     * Constructor for main class
     *
     * @throws java.io.IOException
     */
    public AppMain() throws IOException {
        logger.info("Starting up");
    }

    /**
     * This method starts up the application and turns it over to the main event
     * loop after initializing all the needed objects and other structures.
     *
     * @author Chris Lamke <https://chris.lamke.org>
     * @throws java.io.IOException
     */
    private void start() throws IOException, InterruptedException {
        String defaultSettingsPath
                = "c:\\Users\\chris\\OneDrive\\InWork\\fileOrganizer\\";

        // Load settings into Config from file-organizer-settings.txt
        String settingsFilePath = defaultSettingsPath
                + "file-organizer-settings.txt";
        logger.debug("Creating Config");
        Config config = Config.getInstance();
        config.setConfigPath(settingsFilePath);
        config.loadConfig();

        fileUtilities = FileSystemUtilities.getInstance();
        tasks = new TaskQueue();
        fileWatcher = FileSystemWatcher.getInstance();
        files = FileRecordCollection.getInstance();
        
        config.logConfig();

        // register directory and process its events
        //fileWatcher.addWatchPath("c:\\crl\\dev\\test", true);
        ProcessEvents();
    }

    public static void main(String[] args) throws IOException,
            InterruptedException {

        AppMain app = new AppMain();
        app.start();

    }

    private void ProcessEvents() throws InterruptedException {

        for (;;) {

            // Check for file notifications
            processFileNotifications();

            // Process any file events
            // Check whether reminders are due
            // Check whether the user has indicated they want to quit
            // or has changed the app settings/configuration.
            //logger.info("Sleeping 1 second");
            Thread.sleep(1000);
        }

    }

    private void processFileNotifications() {
        FileNotificationCollection notifications
                = fileWatcher.getFileNotificationsPoll(10);
        if (notifications == null) {
            // No notifications to process right now
            logger.debug("No notifications found");
            return;
        }

        logger.debug("{} notifications returned",
                notifications.getNotificationCount());

        while (notifications.getNotificationCount() > 0) {
            FileNotification notification
                    = notifications.popNotification();
            logger.info("Notification: {}",
                    notification.getNotificationAsString());

            switch (notification.getFileNotificationType()) {
                case CREATE:
                    processFileCreation(notification);
                    break;
                case DELETE:
                    processFileDeletion(notification);
                    break;
                case MODIFY:
                    processFileModification(notification);
                    break;
                case NONE:
                    logger.error(
                            "ERROR: FileWatcher reported overflow condition");
                    break;

                default:
                    logger.error(
                            "ERROR: FileWatcher reported undefined event kind");
            }

        }
    }

    private void processFileCreation(FileNotification notification) {
        String filePath = notification.getFilePath();
        boolean fileExistsOnDisk = files.recordExists(filePath);
        if (fileExistsOnDisk) {
            logger.info(
                    "File Creation: File {} already exists in file store",
                    filePath);
            return;
        }

        FileRecord file = new FileRecord(filePath,
                notification.getFileNotificationType());
        if (file.buildFileRecord()) {
            //Test copy of files
            String destPath = "c:\\crl\\dev\\test\\dest\\" + file.getFileName();
            fileUtilities.copyFile(filePath, destPath);
            file.filePath = destPath;
            files.addFileRecord(destPath, file);
            logger.info(
                    "Added file to store. New store count is: {}",
                    files.getFileRecordCount());
        } else {
            logger.info(
                    "File validation failed. ",
                    "File not added file to store."
            );
        }
    }

    private void processFileDeletion(FileNotification notification) {
        logger.info("File Deletion: Removing {} from file store",
                notification.getFilePath());
        files.removeFileRecord(notification.getFilePath());
        logger.info(
                "Removed file from store. New store count is: {}",
                files.getFileRecordCount());
    }

    private void processFileModification(FileNotification notification) {
        // For now, just log this
        logger.info(
                "File Modification event for {}",
                notification.getFilePath());

    }

}
