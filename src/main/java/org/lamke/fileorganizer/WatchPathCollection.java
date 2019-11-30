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

/**
 * This class holds all the currently active watch paths, along with their
 * status.
 *
 * @author Chris Lamke <https://chris.lamke.org>
 */
public class WatchPathCollection {

    private static WatchPathCollection watchPathCollectionInstance = null;
    ArrayList<WatchPath> watchPaths;

    /**
     * Private WatchPathCollection constructor because this is a singleton
     * class.
     *
     */
    private WatchPathCollection() {
        watchPaths = new ArrayList<>();
    }

    /**
     * Public static method to get instance of Config class.
     *
     * @author Chris Lamke <https://chris.lamke.org>
     */
    public static WatchPathCollection getInstance() {
        if (watchPathCollectionInstance == null) {
            watchPathCollectionInstance = new WatchPathCollection();
        }

        return watchPathCollectionInstance;
    }

    public void addWatchPath(WatchPath watchPath) {
        watchPaths.add(watchPath);
    }

    public void removeWatchPath(WatchPath watchPath) {
        watchPaths.remove(watchPath);
    }

    public int getCount() {
        return watchPaths.size();
    }

    public void logWatchPaths() {
        for (WatchPath path : watchPaths) {

        }
    }
}
