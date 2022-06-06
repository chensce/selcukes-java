/*
 *
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
 *
 */

package io.github.selcukes.databind.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.selcukes.databind.exception.DataMapperException;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * The type String helper.
 */
@UtilityClass
public class StringHelper {
    /**
     * The constant nullOrEmpty.
     */
    public static final Predicate<String> nullOrEmpty = StringHelper::isNullOrEmpty;

    /**
     * Is null or empty boolean.
     *
     * @param text the text
     * @return the boolean
     */
    public boolean isNullOrEmpty(String text) {
        return text == null || text.isEmpty() || text.isBlank();
    }

    /**
     * To snake case string.
     *
     * @param text the text
     * @return the string
     */
    public String toSnakeCase(String text) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        return text.replaceAll(regex, replacement).toLowerCase();
    }

    /**
     * To camel case string.
     *
     * @param text the text
     * @return the string
     */
    public String toCamelCase(String text) {
        String regex = "[^a-zA-Z0-9]";
        final StringBuilder stringBuilder = new StringBuilder(text.length());

        for (final String word : text.replaceAll(regex, "_").split("_")) {
            if (!word.isEmpty()) {
                stringBuilder.append(Character.toUpperCase(word.charAt(0)));
                stringBuilder.append(word.substring(1).toLowerCase());
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Interpolate string.
     *
     * @param text     the text
     * @param replacer the replacer
     * @return the string
     */
    public String interpolate(String text, Function<MatchResult, String> replacer) {
        String regex = "\\$\\{(.+?)\\}";
        return Pattern.compile(regex)
            .matcher(text)
            .replaceAll(replacer);
    }

    /**
     * Extract version number string.
     *
     * @param text the text
     * @return the string
     */
    public String extractVersionNumber(String text) {
        String regex = "[^0-9_.]";
        return text.replaceAll(regex, "");
    }

    /**
     * To json string.
     *
     * @param object the object
     * @return the string
     */
    public String toJson(@NonNull Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new DataMapperException("Failed parsing JSON string from POJO[%s]" + object.getClass().getName(), e);
        }
    }

    /**
     * To pretty json string.
     *
     * @param object the object
     * @return the string
     */
    public String toPrettyJson(@NonNull Object object) {
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            throw new DataMapperException("Failed parsing JSON string from POJO[%s]" + object.getClass().getName(), e);
        }
    }

    public JsonNode toJson(@NonNull String content) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonFactory factory = mapper.getFactory();
            JsonParser parser = factory.createParser(content);
            return mapper.readTree(parser);
        } catch (Exception e) {
            throw new DataMapperException("Failed parsing string to JsonNode:\n" + content, e);
        }
    }
}
