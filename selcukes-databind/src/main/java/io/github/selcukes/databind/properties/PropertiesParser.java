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

package io.github.selcukes.databind.properties;

import io.github.selcukes.collections.Resources;
import io.github.selcukes.databind.exception.DataMapperException;

import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.selcukes.collections.Reflections.newInstance;
import static io.github.selcukes.databind.converters.Converters.defaultConverters;
import static io.github.selcukes.databind.properties.PropertiesLoader.getProperties;
import static java.lang.String.format;

class PropertiesParser<T> {

    private final Class<T> entityClass;

    public PropertiesParser(final Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T parse(Path filePath) {
        var properties = getProperties(filePath);

        var fields = Stream.of(entityClass.getDeclaredFields())
                .map(field -> new PropertyField<T>(field, properties, defaultConverters()))
                .map(PropertyField::parse)
                .collect(Collectors.toList());
        return initEntity(fields);
    }

    public void write(Path filePath, Object object) {
        var properties = new Properties();
        Stream.of(object.getClass().getDeclaredFields())
                .map(field -> new PropertyField<>(field, properties, defaultConverters()))
                .forEach(field -> properties.setProperty(field.getKeyName(), field.getFormattedValue(object)));
        try (var output = Resources.newOutputStream(filePath)) {
            properties.store(output, "Generated from " + object.getClass().getSimpleName());
        } catch (Exception e) {
            throw new DataMapperException("Failed to write properties to file: " + filePath, e);
        }
    }

    public T initEntity(final List<PropertyField<T>> mappers) {
        var hasDefaultConstructor = Stream.of(entityClass.getDeclaredConstructors())
                .anyMatch(constructor -> constructor.getParameterCount() == 0);
        if (!hasDefaultConstructor) {
            throw new IllegalStateException(format("%s must have default constructor.", entityClass.getSimpleName()));
        }
        var entity = newInstance(entityClass);
        mappers.forEach(mapper -> mapper.assignValue(entity));
        return entity;
    }
}
