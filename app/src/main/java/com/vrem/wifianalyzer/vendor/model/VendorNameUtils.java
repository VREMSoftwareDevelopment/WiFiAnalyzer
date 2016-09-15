/*
 * Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.vrem.wifianalyzer.vendor.model;

import org.apache.commons.lang3.StringUtils;

class VendorNameUtils {
    private static final int VENDOR_NAME_MAX = 50;

    static String cleanVendorName(String name) {
        if (StringUtils.isNotBlank(name)) {
            String result = name
                .replaceAll("[._+:~^#$%!@`&*;,?|-]", " ")
                .replaceAll(" +", " ")
                .trim()
                .toUpperCase();

            if (StringUtils.isAlphanumericSpace(result)) {
                return result.substring(0, Math.min(result.length(), VENDOR_NAME_MAX));
            }
        }
        return StringUtils.EMPTY;
    }
}
