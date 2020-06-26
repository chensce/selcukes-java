/*
 *
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.github.selcukes.reports.cucumber;

import io.github.selcukes.core.logging.LogRecordListener;
import io.github.selcukes.core.logging.LoggerFactory;
import io.github.selcukes.reports.enums.RecorderType;
import io.github.selcukes.reports.video.Recorder;
import io.github.selcukes.reports.video.RecorderFactory;

public class CucumberAdapter implements CucumberService {
    private Recorder recorder;
    private LogRecordListener logRecordListener;

    @Override
    public void beforeTest() {
        recorder = RecorderFactory.getRecorder(RecorderType.MONTE);
    }

    @Override
    public void beforeScenario() {
        logRecordListener = new LogRecordListener();
        LoggerFactory.addListener(logRecordListener);
        recorder.start();
    }

    @Override
    public void beforeStep() {

    }

    @Override
    public void afterStep(String step, boolean status) {

    }

    @Override
    public void afterScenario(String scenarioName, boolean status) {
        recorder.stopAndSave(scenarioName);
        LoggerFactory.removeListener(logRecordListener);
    }

    @Override
    public void afterTest() {

    }
}

