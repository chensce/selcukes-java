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

package io.github.selcukes.core.tests.grid;

import io.github.selcukes.core.driver.BrowserOptions;
import io.github.selcukes.core.enums.DeviceType;
import io.github.selcukes.core.tests.GridBaseTest;
import io.github.selcukes.wdb.enums.DriverType;
import lombok.CustomLog;
import lombok.SneakyThrows;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.github.selcukes.core.driver.DriverManager.*;

@CustomLog

public class ClassicGridTest extends GridBaseTest {

    @DataProvider(parallel = true)
    public Object[][] driverTypes() {
        return new Object[][]{{DriverType.CHROME}, {DriverType.EDGE}};
    }

    @SneakyThrows
    @Test(dataProvider = "driverTypes")
    public void parallelBrowserTest(DriverType driverType) {
        BrowserOptions browserOptions = new BrowserOptions();
        createDriver(DeviceType.BROWSER, browserOptions.getBrowserOptions(driverType, true));
        getDriver().get("https://www.google.com/");
        Assert.assertEquals(getDriver().getTitle(), "Google");
    }

    @AfterMethod
    void afterTest() {
        removeDriver();
    }
}
