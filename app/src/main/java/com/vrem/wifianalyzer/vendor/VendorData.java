/*
 *    Copyright (C) 2010 - 2015 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.vendor;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

class VendorData implements Comparable<VendorData> {
    public static final VendorData EMPTY = new VendorData(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);

    private final String macAddressStart;
    private final String macAddressEnd;
    private final String companyName;

    VendorData(@NonNull String macAddressStart, @NonNull String macAddressEnd, @NonNull String companyName) {
        this.macAddressStart = macAddressStart;
        this.macAddressEnd = macAddressEnd;
        this.companyName = companyName;
    }

    String getMacAddressStart() {
        return macAddressStart;
    }

    String getMacAddressEnd() {
        return macAddressEnd;
    }

    String getCompanyName() {
        return companyName;
    }

    boolean inRange(String macAddress) {
        String request = macAddress.replace(":", "").replace("-", "");
        return request.compareTo(getMacAddressStart()) >= 0 && request.compareTo(getMacAddressEnd()) <= 0;
    }

    @Override
    public int compareTo(@NonNull VendorData other) {
        return new CompareToBuilder()
                .append(getMacAddressStart(), other.getMacAddressStart())
                .append(getMacAddressEnd(), other.getMacAddressEnd())
                .append(getCompanyName(), other.getCompanyName())
                .toComparison();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;

        if (other == null || getClass() != other.getClass()) return false;

        return new EqualsBuilder()
                .append(getMacAddressStart(), ((VendorData) other).getMacAddressStart())
                .append(getMacAddressEnd(), ((VendorData) other).getMacAddressEnd())
                .append(getCompanyName(), ((VendorData) other).getCompanyName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getMacAddressStart())
                .append(getMacAddressEnd())
                .append(getCompanyName())
                .toHashCode();
    }

}
