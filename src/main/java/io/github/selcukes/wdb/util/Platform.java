/*
 *
 *  * Copyright (c) Ramesh Babu Prudhvi.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package io.github.selcukes.wdb.util;

import io.github.selcukes.core.commons.os.OsType;

import java.util.Objects;

public final class Platform {

    private int archType;


    private int getPlatformArch() {

        return System.getProperty("os.arch").contains("64") ? 64 : 32;
    }

    public static Platform getPlatform() {
        return new Platform();
    }

    public OsType getOSType() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.startsWith("windows"))
            return OsType.WIN;
        else if (osName.startsWith("mac") || osName.startsWith("darwin")) {
            return OsType.MAC;
        } else if (osName.contains("linux"))
            return OsType.LINUX;

        return null;
    }

    public String getOS() {
        return Objects.requireNonNull(getOSType()).name();
    }

    public int getArchitecture() {
        return archType != 0 ? archType : getPlatformArch();
    }

    public Platform setArchitecture(int archType) {
        this.archType = archType;
        return this;
    }

    public String getOsNameAndArch() {
        return (getOS() + getArchitecture()).toLowerCase();
    }
}