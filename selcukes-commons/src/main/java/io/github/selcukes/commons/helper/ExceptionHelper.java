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

package io.github.selcukes.commons.helper;

import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.databind.DataMapper;
import io.github.selcukes.databind.utils.StringHelper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

@UtilityClass
public class ExceptionHelper {

    private final Logger logger = LoggerFactory.getLogger(ExceptionHelper.class);
    private static ErrorCodes errorCodes;

    /**
     * Rethrows the given throwable as a RuntimeException.
     *
     * @param throwable The throwable to be rethrown.
     */
    @SneakyThrows
    public void rethrow(final Throwable throwable) {
        var multiCause = multiCause(throwable);
        throw multiCause.get(multiCause.size() - 1);
    }

    /**
     * It creates a StringWriter, which is a character stream that collects its
     * output in a string buffer, which can then be used to construct a string
     *
     * @param  throwable The throwable to get the stack trace from.
     * @return           A string representation of the stack trace of the
     *                   throwable.
     */
    public String getStackTrace(Throwable throwable) {
        return ofNullable(throwable)
                .map(ex -> {
                    var stringWriter = new StringWriter();
                    ex.printStackTrace(new PrintWriter(stringWriter));
                    return stringWriter.toString();
                })
                .orElse(null);
    }

    /**
     * Returns the first word of the first line of the stack trace of the given
     * throwable
     *
     * @param  throwable The exception that was thrown.
     * @return           The title of the exception.
     */
    public String getExceptionTitle(final Throwable throwable) {
        return StringHelper.findPattern("([\\w.]+)(:.*)?", getStackTrace(throwable))
                .orElse(null);
    }

    /**
     * If the errorCodes object is null, then parse the ErrorCodes.json file and
     * store the result in the errorCodes object
     *
     * @return The errorCodes object.
     */
    private static ErrorCodes getErrorCodes() {
        if (errorCodes == null) {
            errorCodes = DataMapper.parse(ErrorCodes.class);
        }
        return errorCodes;
    }

    /**
     * Logs an error message and suggests a solution based on the type of the
     * provided Throwable object.
     *
     * @param throwable The Throwable object to log and suggest a solution for.
     */
    public void logError(Throwable throwable) {
        String errorMessage = throwable.getMessage();
        String solution = getErrorCodes().findSolution(throwable.getClass().getSimpleName());
        logger.error(throwable, () -> errorMessage + "\nHow to fix: \n" + solution);
    }

    /**
     * Returns a list of all the causes of a given Throwable, starting from the
     * original Throwable and going down to the root cause.
     *
     * @param  throwable            the Throwable to retrieve the causes from
     * @return                      a LinkedList of all the causes of the given
     *                              Throwable
     * @throws NullPointerException if the given Throwable is null
     */
    public static List<Throwable> multiCause(final Throwable throwable) {
        return Stream.iterate(throwable, t -> t.getCause() != null, Throwable::getCause)
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
