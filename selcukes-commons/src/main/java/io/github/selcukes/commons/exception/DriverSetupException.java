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

package io.github.selcukes.commons.exception;

public class DriverSetupException extends SelcukesException {
    private static final long serialVersionUID = 1L;

    public DriverSetupException() {
        super();
    }

    public DriverSetupException(String message, Throwable cause) {
        super(message, cause);
    }

    public DriverSetupException(String message) {
        super(message);
    }

    public DriverSetupException(Throwable cause) {
        super(cause);
    }

}
