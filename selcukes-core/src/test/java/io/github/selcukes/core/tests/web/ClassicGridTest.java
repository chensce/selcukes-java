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

package io.github.selcukes.core.tests.web;

import io.github.selcukes.commons.annotation.Lifecycle;
import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.core.driver.GridRunner;
import io.github.selcukes.core.page.Pages;
import lombok.CustomLog;
import org.openqa.selenium.remote.Browser;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@CustomLog
@Lifecycle
public class ClassicGridTest {
    @BeforeSuite
    public static void beforeSuite() {
        GridRunner.startSelenium();
    }

    @DataProvider(parallel = true)
    public Object[][] driverTypes() {
        return new Object[][] { { Browser.CHROME }, { Browser.EDGE }
        };
    }

    @Test(dataProvider = "driverTypes")
    public void parallelBrowserTest(Browser driverType) {
        logger.debug(() -> "In Parallel Test for " + driverType.browserName());
        ConfigFactory.getConfig().getWeb().setBrowser(driverType.browserName());
        Pages.webPage().open("https://www.google.com/")
                .assertThat().title("Google");
    }

}
