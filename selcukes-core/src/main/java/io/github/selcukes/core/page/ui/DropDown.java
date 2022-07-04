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

package io.github.selcukes.core.page.ui;

import io.github.selcukes.commons.helper.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.support.ui.Select;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static java.lang.Integer.parseInt;

@UtilityClass
public class DropDown {
    private static final String ATTRIBUTE = "value";

    @AllArgsConstructor
    enum SelectionType {
        label(Select::selectByVisibleText, select -> select.getFirstSelectedOption().getText()),
        value(Select::selectByValue, select -> select.getFirstSelectedOption().getAttribute(ATTRIBUTE)),
        index((select, value) -> select.selectByIndex(parseInt(value)), select -> select.getOptions().
            indexOf(select.getFirstSelectedOption()));
        @Getter
        private final BiConsumer<Select, String> selector;
        @Getter
        private final Function<Select, Object> retriever;

    }

    public void select(Select select, String optionLocator) {
        Preconditions.checkArgument(optionLocator.contains(":"), "Invalid Locator");
        String locatorType = optionLocator.split(":")[0];
        String locatorValue = optionLocator.split(":")[1];
        SelectionType type = SelectionType.valueOf(locatorType);
        type.getSelector().accept(select, locatorValue);
    }

    public Object selected(Select select, String optionLocator) {
        SelectionType type = SelectionType.valueOf(optionLocator);
        return type.getRetriever().apply(select);
    }
}
