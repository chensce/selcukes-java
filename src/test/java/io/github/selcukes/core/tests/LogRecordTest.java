/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.core.tests;

import io.github.selcukes.core.logging.LogRecordListener;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.stream.Collectors;

public class LogRecordTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private LogRecordListener logRecordListener;

    @BeforeTest
    public void setup() {
        logRecordListener = new LogRecordListener();
        LoggerFactory.addListener(logRecordListener);
    }

    @AfterTest
    public void tearDown(){
        LoggerFactory.removeListener(logRecordListener);
    }

    @Test
    public void testLogRecords()
    {
        logger.error(() -> "Error");
        logger.warn(() -> "Warn");
        logger.config(() -> "Config");
        logger.trace(() -> "Trace");
        logger.warn(() -> "This is sample warn");
        List<String> warnResult =logRecordListener.getLogStream(Level.SEVERE).map(LogRecord::getMessage).collect(Collectors.toList());
        warnResult.forEach(System.out::println);

    }
}
