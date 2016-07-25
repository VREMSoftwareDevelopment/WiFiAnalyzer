/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.vrem.wifianalyzer.settings;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.builder.CompareToBuilder;

class Data implements Comparable<Data> {
    private final String code;
    private final String name;

    Data(String code, String name) {
        this.code = code;
        this.name = name;
    }

    protected String getCode() {
        return code;
    }

    protected String getName() {
        return name;
    }

    @Override
    public int compareTo(@NonNull Data another) {
        return new CompareToBuilder()
            .append(getName(), another.getName())
            .append(getCode(), another.getCode())
            .toComparison();
    }
}
