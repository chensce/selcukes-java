/*
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
 */

package io.github.selcukes.core.driver;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.commons.exception.DriverConnectionException;
import io.github.selcukes.commons.helper.Singleton;
import lombok.CustomLog;

import java.net.URL;
import java.nio.file.Path;

import static java.util.Optional.ofNullable;

@CustomLog
class AppiumEngine {
    private AppiumDriverLocalService service;

    public static AppiumEngine getInstance() {
        return Singleton.instanceOf(AppiumEngine.class);
    }

    URL getServiceUrl() {
        if (service == null) {
            startLocalServer();
        }
        return service.getUrl();
    }

    void startLocalServer() {
        try {
            var reportsPath = ofNullable(ConfigFactory.getConfig().getReports())
                    .map(reports -> reports.get("reportsPath")).orElse("target");
            var logFilePath = Path.of(reportsPath, "appium-server.log");
            service = new AppiumServiceBuilder()
                    .withIPAddress("127.0.0.1")
                    .usingAnyFreePort()
                    .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                    .withArgument(GeneralServerFlag.BASEPATH, "/wd/")
                    .withLogFile(logFilePath.toFile())
                    .build();
            logger.info(() -> "Starting Appium server...");
            service.start();
        } catch (Exception e) {
            throw new DriverConnectionException("Failed starting Appium Server..", e);
        }
    }

    void stopServer() {
        if (service != null) {
            service.stop();
            logger.info(() -> "Stopped Appium server...");
        }
    }
}
