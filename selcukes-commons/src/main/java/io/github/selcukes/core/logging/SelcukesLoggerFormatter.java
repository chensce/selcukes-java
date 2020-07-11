/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.core.logging;

import io.github.selcukes.core.helper.DateHelper;
import io.github.selcukes.core.helper.ExceptionHelper;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;


public class SelcukesLoggerFormatter extends Formatter {

    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder(1000);
        builder.append(DateHelper.get().timeStamp(record.getMillis())).append(" - ");
        builder.append("[").append(getLevel(record)).append("] - ");
        builder.append("[").append(record.getSourceClassName()).append(".");
        builder.append(record.getSourceMethodName()).append(":").append(getLineNumber(record)).append("] - ");
        builder.append(formatMessage(record));
        Throwable throwable = record.getThrown();
        if (null != throwable) {
            builder.append('\n').append(ExceptionHelper.getStackTrace(throwable));
        }
        builder.append("\n");

        return builder.toString();
    }

    public String getLevel(LogRecord record) {
        String level = record.getLevel().getName();
        switch (level) {
            case "SEVERE":
                level = "ERROR";
                break;
            case "FINE":
                level = "DEBUG";
                break;
            case "FINER":
                level = "TRACE";
                break;
            default:
        }
        return level;
    }

    private StackTraceElement getCallerStackFrame(final String callerName) {
        StackTraceElement callerFrame = null;

        final StackTraceElement[] stack = new Throwable().getStackTrace();
        // Search the stack trace to find the calling class
        for (final StackTraceElement frame : stack) {
            if (callerName.equals(frame.getClassName())) {
                callerFrame = frame;
                break;
            }
        }
        return callerFrame;
    }

    private String getLineNumber(LogRecord record) {
        final StackTraceElement callerFrame = getCallerStackFrame(record.getSourceClassName());

        if (callerFrame != null) {

            final int lineNumber = callerFrame.getLineNumber();
            return lineNumber + "";
        }
        return "";
    }
}
