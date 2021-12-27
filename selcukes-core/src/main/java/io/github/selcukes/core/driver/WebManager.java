/*
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
 */

package io.github.selcukes.core.driver;

import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.commons.exception.DriverSetupException;
import io.github.selcukes.wdb.driver.LocalDriver;
import io.github.selcukes.wdb.enums.DriverType;
import lombok.CustomLog;
import org.openqa.selenium.WebDriver;

@CustomLog
public class WebManager implements RemoteManager {

    private WebDriver driver;

    public synchronized WebDriver createDriver() {
        String browser = ConfigFactory.getConfig().getWeb().get("BrowserName");
        if (null == driver) {
            try {
                logger.info(() -> "Initiating New Browser Session...");
                if (ConfigFactory.getConfig().getWeb().get("remote").equalsIgnoreCase("true"))
                    driver = createRemoteDriver();
                else
                    driver = new LocalDriver().createWebDriver(DriverType.valueOf(browser));

            } catch (Exception e) {
                throw new DriverSetupException("Driver was not setup properly.", e);
            }
        }
        return driver;
    }

    @Override
    public void destroyDriver() {

    }

}
