/*
 *
 *  Copyright (c) Ramesh Babu Prudhvi.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.github.selcukes.extent.report;

import io.github.selcukes.commons.logging.LogRecordListener;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.snapshot.Snapshot;
import io.github.selcukes.snapshot.SnapshotImpl;
import org.openqa.selenium.WebDriver;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.stream.Collectors;

import static io.github.selcukes.databind.utils.StringHelper.nullOrEmpty;

public class Reporter {
    private static Snapshot snapshot;
    private LogRecordListener logRecordListener;
    private static final ThreadLocal<Reporter> reporterThreadLocal = new InheritableThreadLocal<>();

    public static void log(String message) {
        if (message != null) {
            SelcukesExtentAdapter.addTestStepLog(message);
        }
    }

    public static Reporter getReporter() {
        if (reporterThreadLocal.get() == null) {
            reporterThreadLocal.set(new Reporter());
        }
        return reporterThreadLocal.get();
    }

    Reporter start() {
        logRecordListener = new LogRecordListener();
        LoggerFactory.addListener(logRecordListener);
        return this;
    }

    public void initSnapshot(WebDriver driver) {
        snapshot = new SnapshotImpl(driver);
    }


    private Reporter attach() {
        if (logRecordListener != null) {
            String infoLogs = logRecordListener.getLogRecords(Level.INFO)
                .map(LogRecord::getMessage)
                .filter(nullOrEmpty.negate())
                .collect(Collectors.joining("</li><li>", "<ul><li> ",
                    "</li></ul><br/>"));
            if (!infoLogs.equalsIgnoreCase("<ul><li> </li></ul><br/>"))
                Reporter.log(infoLogs);
        }
        return this;
    }

    private Reporter stop() {
        if (logRecordListener != null)
            LoggerFactory.removeListener(logRecordListener);
        return this;
    }

    void removeReporter() {
        reporterThreadLocal.remove();
    }

    void attachAndClear() {
        attach().stop();

    }

    void attachAndRestart() {
        attach().stop().start();
    }

    public void attachScreenshot() {
        SelcukesExtentAdapter.attachScreenshot(snapshot.shootPageAsBytes());
    }
}
