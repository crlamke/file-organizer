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
import java.util.StringTokenizer;
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

    private FileSystemWatcher watcher;
    private FileTypeCollection fileTypes;

    private Logger logger = LogManager.getLogger(AppMain.class.getName());
    private String configFile = null;
    WatchPathCollection watchPaths; // Store watch paths from the config file.

    /**
     * Private Config constructor because this is a singleton class.
     *
     * @author Chris Lamke <https://chris.lamke.org>
     */
    private Config() throws IOException {
        watcher = FileSystemWatcher.getInstance();
        fileTypes = FileTypeCollection.getInstance();
    }

    /**
     * Public static method to get instance of Config class.
     *
     * @author Chris Lamke <https://chris.lamke.org>
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
     * @author Chris Lamke <https://chris.lamke.org>
     * @param String path to app configuration file
     */
    void setConfigPath(String configFilePath) {
        this.configFile = configFilePath;
    }

    /**
     * Load the app configuration from the config file on disk, initializing
     * WatchPaths and other relevant objects to prepare for operation.
     *
     * @author Chris Lamke <https://chris.lamke.org>
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
                StringTokenizer tokenizer = new StringTokenizer(currentLine, "\t");
                String firstToken = tokenizer.nextToken();
                switch (firstToken) {
                    case "WATCHPATH":
                        // register directory and process its events
                        addWatch(currentLine, tokenizer);
                        break;
                    case "ACTION":
                        addAction(currentLine, tokenizer);
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
     * @author Chris Lamke <https://chris.lamke.org>
     */
    void reloadConfig() {

    }

    private void addWatch(String watchLine, StringTokenizer tokenizer) throws IOException {
        // Should be two more tokens, a path and a recursion choice
        String path = tokenizer.nextToken();
        String recursion = tokenizer.nextToken();
        boolean isRecursive = false;        
        if (recursion.equals("Y") || recursion.equals("y")) {
            isRecursive = true;
        }
        
        logger.info("Adding Watch Path: {}. Recursion = {}", path, isRecursive);
        path = path.replace("\"", "");
        watcher.addWatchPath(path, isRecursive);
    }

    private void addAction(String actionLine, StringTokenizer tokenizer) {
        //ACTION	GIF	MOVE	"c:\crl\dev\test\dest"
        String fileType = tokenizer.nextToken();
        String changeType = tokenizer.nextToken();
        String action = tokenizer.nextToken();
        String path = tokenizer.nextToken();
        
        logger.info(
                "Adding action: {} for file type {}, change type {}, path {}",
                action, fileType, changeType, path);
        path = path.replace("\"", "");
        FileTypeDefinition fileTypeDefinition = new FileTypeDefinition();
        fileTypes.addFileType(fileTypeDefinition);
        
    }

}
