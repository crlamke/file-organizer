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

 /* 
 * Based on 
 * https://docs.oracle.com/javase/tutorial/essential/io/examples/WatchDir.java 
 * 
 */
package org.lamke.fileorganizer;

import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;
import java.nio.file.attribute.*;
import java.io.*;
import java.util.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class contains the algorithm for registering and unregistering
 * directories to be watched for changes. My goal is for this class to be easily
 * used by other programs/projects, so I'll minimize dependency on external
 * resources. This is a singleton class. Use the getInstance method to access
 * the object.
 *
 * @author Chris Lamke <https://chris.lamke.org>
 * @throws java.io.IOException
 */
public class FileSystemWatcher {

    private static FileSystemWatcher watcherInstance = null;

    private final Logger logger = LogManager.getLogger(FileSystemWatcher.class.getName());
    private WatchService watchService;
    private Map<WatchKey, Path> watchKeys;

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    /**
     * Private Config constructor because this is a singleton class.
     *
     * @author Chris Lamke <https://chris.lamke.org>
     */
    private FileSystemWatcher() {
        try {
            watchService = FileSystems.getDefault().newWatchService();
            watchKeys = new HashMap<>();
        } catch (IOException e) {
            logger.log(Level.ERROR, e.toString());
        }
    }

    /**
     * Public static method to get instance of Config class.
     *
     * @return FileSystemWatcher instance
     */
    public static FileSystemWatcher getInstance() throws IOException {
        if (watcherInstance == null) {
            watcherInstance = new FileSystemWatcher();
        }

        return watcherInstance;
    }


    /**
     * Register the given directory with the WatchService
     *
     * @param watchPath as String - String containing path to watch for changes
     * @param isRecursive as boolean - true if you want to watch all subdirs
     * @return true if successful, false otherwise
     * @throws java.io.IOException
     */
    public boolean addWatchPath(String watchPath, boolean isRecursive) throws IOException {
        Path path = Paths.get(watchPath);
        if (isRecursive == true) {
            return registerWatchPathWithRecursion(path);
        } else {
            return registerWatchPathSingle(path);
        }
    }

    /**
     * Unregister the given directory with the WatchService
     *
     * @param watchPath as String - String containing path to watch for changes
     * @param isRecursive as boolean - true if you want to watch all subdirs
     * @return true if successful, false otherwise
     * @throws java.io.IOException
     */
    public boolean removeWatchPath(String watchPath, boolean isRecursive) throws IOException {
        Path path = Paths.get(watchPath);
        if (isRecursive == true) {
            return unregisterWatchPathSingle(path);
        } else {
            return unregisterWatchPathWithRecursion(path);
        }
    }

    /**
     * Register the given directory with the WatchService
     */
    private boolean registerWatchPathSingle(Path path) throws IOException {

        WatchKey watchKey = path.register(
                watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

        // See if the path was already registered and this is an update.
        Path existingPath = watchKeys.get(watchKey);
        if (existingPath == null) {
            logger.debug("Registering path {}", path);
        } else {
            if (!path.equals(existingPath)) {
                logger.debug("Updating path {} -> ", existingPath, path);
            }
        }

        watchKeys.put(watchKey, path);

        // TODO need to determine success vs failure and return approp. value.
        return true;

    }

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private boolean registerWatchPathWithRecursion(Path startPath) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attrs)
                    throws IOException {
                registerWatchPathSingle(path);
                return FileVisitResult.CONTINUE;
            }
        });

        // TODO need to determine success vs failure and return approp. value.
        return true;
    }

    /**
     * Unregister the given directory with the WatchService
     */
    private boolean unregisterWatchPathSingle(Path path) throws IOException {

        WatchKey watchKey = path.register(
                watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

        // See if the path was already registered and this is an update.
        Path existingPath = watchKeys.get(watchKey);
        if (existingPath == null) {
            logger.debug("Registering path {}", path);
        } else {
            if (!path.equals(existingPath)) {
                logger.debug("Updating path {} -> ", existingPath, path);
            }
        }

        watchKeys.put(watchKey, path);

        // TODO need to determine success vs failure and return approp. value.
        return true;

    }

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private boolean unregisterWatchPathWithRecursion(Path startPath) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attrs)
                    throws IOException {
                registerWatchPathSingle(path);
                return FileVisitResult.CONTINUE;
            }
        });

        // TODO need to determine success vs failure and return approp. value.
        return true;
    }

    /**
     * This method checks to see if any files have changed in the paths being
     * monitored and returns a list of notifications.
     *
     * @author Chris Lamke <https://chris.lamke.org>
     * @param millisecs as poll timeout in millisecs
     * @return FileNotificationCollection
     */
    public FileNotificationCollection getFileNotificationsPoll(int millisecs) {

        logger.debug("getFileNotificationsPoll with timeout = {} ms", millisecs);
        FileNotificationCollection notifications = null;

        // wait for key to be signalled
        WatchKey key;
        key = watchService.poll();
        // Need to support WatchKey watchKey = watchService.poll(long timeout, TimeUnit units);
        if (key == null) {
            logger.debug("No notification available");
            return notifications;
        }

        Path dir = watchKeys.get(key);
        if (dir == null) {
            logger.error("WatchKey not recognized");
            return notifications;
        }
        notifications = new FileNotificationCollection();

        for (WatchEvent<?> event : key.pollEvents()) {
            WatchEvent.Kind kind = event.kind();

            // TBD - provide example of how OVERFLOW event is handled
            if (kind == OVERFLOW) {
                logger.error("OVERFLOW event reported by FileWatcher");
                return null;
            }

            // Context for directory entry event is the file name of entry
            WatchEvent<Path> ev = cast(event);
            Path name = ev.context();
            Path child = dir.resolve(name);

            // print out event
            logger.debug("Notification {}: {}\n", event.kind().name(), child);
            FileNotification notification = new FileNotification();
            notification.addFilePath(child.toString());

            switch (event.kind().name()) {
                case "ENTRY_CREATE":
                    notification.addNotificationType(
                            FileNotification.NotificationType.CREATE);
                    break;
                case "ENTRY_DELETE":
                    notification.addNotificationType(
                            FileNotification.NotificationType.DELETE);
                    break;
                case "ENTRY_MODIFY":
                    notification.addNotificationType(
                            FileNotification.NotificationType.MODIFY);

                    break;
                case "OVERFLOW":
                    notification.addNotificationType(
                            FileNotification.NotificationType.NONE);
                    logger.error(
                            "ERROR: FileWatcher reported overflow condition");
                    break;

                default:
                    logger.error(
                            "ERROR: FileWatcher reported undefined event kind");
            }

            notifications.addNotification(notification);

            // if directory is created, and watching recursively, then
            // register it and its sub-directories
            // TODO need to handle checking for recursive flag when 
            // new directory is created to determine what kind of
            // registration to do here.
            boolean recursive = false;
            if (recursive && (kind == ENTRY_CREATE)) {
                try {
                    if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                        registerWatchPathWithRecursion(child);
                    }
                } catch (IOException x) {
                    // ignore to keep sample readbale
                }
            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                watchKeys.remove(key);

                // all directories are inaccessible
                if (watchKeys.isEmpty()) {
                    break;
                }
            }
        }

        return notifications;
    }

    /**
     * NOT IMPLEMENTED - This method checks to see if any files have changed in the paths being
     * monitored and blocks until a notification is available, then returns a
     * list of notifications.
     *
     * @return FileNotificationCollection
     */
    public FileNotificationCollection getFileNotificationsBlock() {
        // Not implemented

        // FileNotification test = new FileNotification();
        //  return test;
        return null;
    }

}
