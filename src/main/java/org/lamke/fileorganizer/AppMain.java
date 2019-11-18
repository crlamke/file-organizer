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
    private WatchPathCollection watchPaths;
    private TaskQueue tasks;
    private FileSystemWatcher fileWatcher;

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
        String defaultSettingsPath = "c:\\Users\\chris\\OneDrive\\InWork\\fileOrganizer\\";

        // Load settings into Config from file-organizer-settings.txt
        String settingsFilePath = defaultSettingsPath
                + "file-organizer-settings.txt";
        logger.info("Creating Config");
        Config config = Config.getInstance();
        config.setConfigPath(settingsFilePath);
        config.loadConfig();

        watchPaths = new WatchPathCollection();
        tasks = new TaskQueue();
        fileWatcher = FileSystemWatcher.getInstance();

        // register directory and process its events
        //fileWatcher.addWatchPath("c:\\crl\\dev\\test", true);
        ProcessEvents();
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        AppMain app = new AppMain();
        app.start();


        /*if (validateArgs(args) == false) {
            usage();
            System.exit(-1);
        }
         */
    }

    private static void usage() {
        System.err.println("usage: java file-organizer [-r] dir0 dir1 ...");
    }

    private static boolean validateArgs(String[] args) {
        // parse arguments
        if (args.length == 0 || args.length > 2) {
            usage();
        }
        return true;
    }

    private void ProcessEvents() throws InterruptedException {

        FileStore files = FileStore.getInstance();

        for (;;) {

            // Check for file notifications
            FileNotificationCollection notifications
                    = fileWatcher.getFileNotificationsPoll(10);
            if (notifications != null) {
                logger.info("{} notifications returned",
                        notifications.getNotificationCount());
                while (notifications.getNotificationCount() > 0) {
                    FileNotification notification
                            = notifications.popNotification();
                    logger.info("Notification: {}",
                            notification.getNotificationAsString());
                    FileRecord file = new FileRecord();
                    files.addFileRecord(file);
                    logger.info("Added file to store. New store count is: {}",
                            files.getFileRecordCount());
                }
            }

            // For each file change notification, we look up the file 
            // type to get the file type object, then look up the actions
            // defined in the file type object and perform those actions.
            // Check whether reminders are due
            //logger.info("Sleeping 1 second");
            Thread.sleep(1000);
        }

    }
}
