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

import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.wdb.enums.DriverType;
import lombok.CustomLog;
import org.openqa.selenium.grid.Main;
import org.openqa.selenium.net.PortProber;

import java.util.Arrays;

@CustomLog
public class GridRunner {
    private static boolean isRunning = false;
    protected static int HUB_PORT;

    public static void startSeleniumServer(DriverType... driverType) {
        logger.info(() -> "Starting Selenium Server ...");
        Arrays.stream(driverType).distinct().forEach(BrowserOptions::setBinaries);
        HUB_PORT = PortProber.findFreePort();
        if (isGridNotRunning()) {
            logger.debug(() -> "Using Free Hub Port: " + HUB_PORT);
            Main.main(new String[]{"standalone", "--port", String.valueOf(HUB_PORT)});
            isRunning = true;
            logger.info(() -> "Selenium Server started...");
        }
    }

    static boolean isGrid() {
        return ConfigFactory.getConfig().getWeb().get("remote")
            .equalsIgnoreCase("true");
    }

    static boolean isGridNotRunning() {
        return isGrid() && !GridRunner.isRunning;
    }

    public static void startAppiumServer() {
        AppiumEngine.getInstance().startLocalServer();
    }

    public static void stopAppiumServer() {
        AppiumEngine.getInstance().stopServer();
    }

}
