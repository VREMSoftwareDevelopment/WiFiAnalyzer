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

package com.vrem.wifianalyzer.vendor.model;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.builder.ToStringBuilder;

class VendorData {
    private final String name;
    private final String mac;
    private final long id;

    VendorData(long id, @NonNull String name, @NonNull String mac) {
        this.id = id;
        this.name = name;
        this.mac = mac;
    }

    protected long getId() {
        return id;
    }

    protected String getName() {
        return name;
    }

    protected String getMac() {
        return mac;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}