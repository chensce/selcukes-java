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

package io.github.selcukes.core.tests;

import io.github.selcukes.commons.helper.DateHelper;
import io.github.selcukes.commons.helper.FileHelper;
import lombok.CustomLog;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Paths;

@CustomLog
public class EventDriverTest extends BaseTest {

    @Test
    public void eventDriverTest() {
        page.open("https://techyworks.blogspot.com/");
        page.assertThat().title("Techy Works - Free Online Training Tutorials");

        By search = By.xpath("//span[@class='show-search' or @class='show-mobile-search']");
        page.click(search);
        page.enter(By.xpath("//input[@class='search-input' or @class='mobile-search-input']"), "selenium");
    }

    @SneakyThrows
    @AfterMethod
    public void afterTest() {
        File srcFile = page.screenshotAs(OutputType.FILE);
        File reportDirectory = new File("target/screenshots");
        FileHelper.createDirectory(reportDirectory);
        String filePath = reportDirectory + File.separator + "screenshot_" + DateHelper.get().dateTime() + ".png";
        FileUtils.copyFile(srcFile, Paths.get(filePath).toFile());
    }
}
