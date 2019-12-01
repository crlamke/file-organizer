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
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Config class loads and stores the app configuration, including lists of
 * directories to watch, actions to take, and rules used to determine file type.
 * This is a singleton class. Use the getInstance method to access the object.
 *
 * @author Chris Lamke <https://chris.lamke.org>
 *
 */
public class Config {

    private static Config configInstance = null;

    private FileSystemWatcher watcher = null;
    private final Logger logger = LogManager.getLogger(Config.class.getName());
    private String configFile = null;
    ArrayList<WatchPath> watchPaths;
    ArrayList<FileTypeActionDef> fileTypes;

    /**
     * Private Config constructor because this is a singleton class.
     *
     */
    private Config() {
        try {
            watcher = FileSystemWatcher.getInstance();
            fileTypes = new ArrayList<>();
            watchPaths = new ArrayList<>();
        } catch (IOException e) {
            logger.log(Level.ERROR, e.toString());
        }
    }

    /**
     * Public static method to get instance of Config class.
     *
     * @return Config object instance
     */
    public static Config getInstance() throws IOException {
        if (configInstance == null) {
            configInstance = new Config();
        }

        return configInstance;
    }

    /**
     * setConfigPath() allows you to set the config file path. This must be
     * called before you call loadConfig().
     *
     * @param String path to app configuration file
     */
    void setConfigPath(String configFilePath) {
        this.configFile = configFilePath;
    }

    /**
     * Load the app configuration from the config file on disk, initializing
     * WatchPaths and other relevant objects to prepare for operation.
     *
     */
    public void loadConfig() {
        logger.debug("Starting Config load");
        try {
            String currentLine = null;
            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            while ((currentLine = reader.readLine()) != null) {
                // Skip comments and empty lines
                if ((currentLine.length() == 0) || (currentLine.charAt(0) == '#')) {
                    logger.debug("Skipping empty or comment line");
                    continue;
                }

                // Line is not a comment so tokenize it for parsing.
                String lineParts[] = currentLine.split("\\s+");
                switch (lineParts[0]) {
                    case "WATCHPATH":
                        // register directory and process its events
                        addWatch(lineParts);
                        break;
                    case "ACTION":
                        addAction(lineParts);
                        break;
                    default:
                        logger.error("ERROR: Bad Line Format - {}", currentLine);
                }
                logger.debug("Processed - {}", currentLine);

            }
        } catch (Exception e) {
            logger.error("FATAL ERROR: Unable to load config file - {}", configFile);
            e.printStackTrace();
        }
        logger.debug("End Config load");

    }

    /**
     * Reload the configuration from the config file on disk, first deleting all
     * WatchPaths and other relevant objects to reset app to default state.
     * After returning app to default state, we will call loadConfig().
     *
     */
    public void reloadConfig() {

    }

    private void addWatch(String[] lineParts) throws IOException {
        // Line Format: WATCHPATH	"c:\crl\down"	N
        // Should be two more tokens, a path and a recursion choice
        String path = lineParts[1];
        String recursion = lineParts[2];
        boolean isRecursive = false;
        if (recursion.equals("Y") || recursion.equals("y")) {
            isRecursive = true;
        }

        logger.debug("Adding Watch Path: {}. Recursion = {}", path, isRecursive);
        path = path.replace("\"", "");
        WatchPath watchPath = new WatchPath(path, isRecursive);
        watchPaths.add(watchPath);

        // Add to FileSystemWatcher
        watcher.addWatchPath(path, isRecursive);
    }

    private void addAction(String[] lineParts) {
        //Line Format: ACTION	GIF	CREATE  MOVE 1 "c:\crl\dev\test\dest"
        String fileType = lineParts[1];
        String changeType = lineParts[2];
        String action = lineParts[3];
        int priority = Integer.parseInt(lineParts[4]);
        String path = lineParts[5];

        logger.info("Adding File Type Definition: For {} with change {} do action "
                + "{} priority {} with path {}",
                fileType, changeType, action, priority, path);
        path = path.replace("\"", "");
        FileTypeActionDef fileTypeDefinition
                = new FileTypeActionDef(fileType, changeType, action,
                        path, priority);
        fileTypes.add(fileTypeDefinition);

    }

    public void logConfig() {
        logger.info("Begin Log of Config");
        logger.info("Watch Paths");
        for (WatchPath path : watchPaths) {
            logger.info("Path: {}. Recursive = {}",
                    path.getPathString(), path.isPathRecursive());
        }
        logger.info("File Type Definitions");
        fileTypes.forEach((_item) -> {
            logger.info("File Type: {}",
                    _item.getFileTypeActionDefAsString());
        });

    }

}
