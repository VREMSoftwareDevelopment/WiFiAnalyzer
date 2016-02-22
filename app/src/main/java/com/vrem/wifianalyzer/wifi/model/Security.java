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

package com.vrem.wifianalyzer.wifi.model;

import com.vrem.wifianalyzer.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public enum Security {
    // weak getSecurity first - keep this order
    NONE(R.drawable.ic_lock_open_black_18dp),
    WPS(R.drawable.ic_lock_outline_black_18dp),
    WEP(R.drawable.ic_lock_outline_black_18dp),
    WPA(R.drawable.ic_lock_black_18dp),
    WPA2(R.drawable.ic_lock_black_18dp);

    private final int imageResource;

    Security(int imageResource) {
        this.imageResource = imageResource;
    }

    public static List<Security> findAll(String capabilities) {
        Set<Security> results = new TreeSet<>();
        if (capabilities != null) {
            String[] values = capabilities.toUpperCase()
                    .replace("][", "-").replace("]", "").replace("[", "").split("-");
            for (String value : values) {
                try {
                    results.add(Security.valueOf(value));
                } catch (Exception e) {
                    // skip getCapabilities that are not getSecurity
                }
            }
        }
        return new ArrayList<>(results);
    }

    public static Security findOne(String capabilities) {
        List<Security> securities = findAll(capabilities);
        for (Security security : Security.values()) {
            if (securities.contains(security)) {
                return security;
            }
        }
        return Security.NONE;
    }

    public int imageResource() {
        return imageResource;
    }

}
