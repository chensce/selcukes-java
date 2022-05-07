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

package io.github.selcukes.core.page;

import io.appium.java_client.AppiumDriver;
import io.github.selcukes.commons.Await;
import io.github.selcukes.core.enums.SwipeDirection;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

public class MobilePage implements Page {
    private final AppiumDriver driver;

    public MobilePage(AppiumDriver driver) {
        this.driver = driver;
    }

    @Override
    public WebDriver getDriver() {
        return driver;
    }

    public MobilePage tap(By by) {
        click(by);
        return this;
    }

    public MobilePage doubleTap(By by) {
        doubleClick(by);
        return this;
    }


    public MobilePage swipe(By target, SwipeDirection swipeDirection) {
        swipe(null, target, swipeDirection);
        return this;
    }

    public MobilePage swipe(By source, By target, SwipeDirection swipeDirection) {
        boolean isElementFound = false;
        boolean isScrollable = true;
        do {
            if (!findAll(target).isEmpty() && find(target).isDisplayed()) {
                isElementFound = true;
            } else {
                isScrollable = attemptToScroll(swipeDirection, source);
            }
        } while (Boolean.FALSE.equals(isElementFound) && Boolean.TRUE.equals(isScrollable));

        return this;
    }

    private boolean attemptToScroll(SwipeDirection swipeDirection, By source) {

        Dimension screenSize = getDriver().manage().window().getSize();
        boolean isScrollable;

        var scrollParameters = new HashMap<>();

        if (source != null) {
            Rectangle elementRectangle = find(source).getRect();
            scrollParameters.put(
                "height", elementRectangle.getHeight() * 90 / 100
            );

            switch (swipeDirection) {
                case UP:
                    scrollParameters.putAll(Map.of("percent", 0.8, "height",
                        elementRectangle.getHeight() * 90 / 100, "width", elementRectangle.getWidth(),
                        "left", elementRectangle.getX(), "top", elementRectangle.getHeight() - 100));
                    break;
                case DOWN:
                    scrollParameters.putAll(Map.of("percent", 0.8, "height",
                        elementRectangle.getHeight() * 90 / 100, "width", elementRectangle.getWidth(),
                        "left", elementRectangle.getX(), "top", 100));
                    break;
                case RIGHT:
                    scrollParameters.putAll(Map.of("percent", 1, "height",
                        elementRectangle.getHeight(), "width", elementRectangle.getWidth() * 70 / 100,
                        "left", 100, "top", elementRectangle.getY()));
                    break;
                case LEFT:
                    scrollParameters.putAll(Map.of("percent", 1, "height",
                        elementRectangle.getHeight(), "width", elementRectangle.getWidth(),
                        "left", elementRectangle.getX() + (elementRectangle.getWidth() * 50 / 100),
                        "top", elementRectangle.getY()));
                    break;
            }
        } else {

            scrollParameters.putAll(Map.of(
                "width", screenSize.getWidth(), "height", screenSize.getHeight() * 90 / 100,
                "percent", 0.8
            ));
            switch (swipeDirection) {
                case UP:
                    scrollParameters.putAll(Map.of("left", 0, "top", screenSize.getHeight() - 100));
                    break;
                case DOWN:
                    scrollParameters.putAll(Map.of("left", 0, "top", 100));
                    break;
                case RIGHT:
                    scrollParameters.putAll(Map.of("left", 100, "top", 0));
                    break;
                case LEFT:
                    scrollParameters.putAll(Map.of("left", screenSize.getWidth() - 100, "top", 0));
                    break;
            }
        }


        scrollParameters.put(
            "direction", swipeDirection.toString()
        );
        isScrollable = (Boolean) executeScript("mobile: scrollGesture", scrollParameters);
        Await.until(1);
        return isScrollable;
    }

}
