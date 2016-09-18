/*
 * WiFi Analyzer
 * Copyright (C) 2016  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.vrem.wifianalyzer.vendor.model;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.json.JSONException;
import org.json.JSONObject;

class RemoteResult {
    private static final String MAC_ADDRESS = "mac_address";
    private static final String VENDOR_NAME = "vendor_name";

    private final String macAddress;
    private final String vendorName;

    RemoteResult(@NonNull String macAddress, @NonNull String vendorName) {
        this.macAddress = macAddress;
        this.vendorName = vendorName;
    }

    RemoteResult(@NonNull String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        macAddress = getValue(jsonObject, MAC_ADDRESS);
        vendorName = getValue(jsonObject, VENDOR_NAME);
    }

    String getMacAddress() {
        return macAddress;
    }

    String getVendorName() {
        return vendorName;
    }

    String toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(MAC_ADDRESS, macAddress);
        jsonObject.put(VENDOR_NAME, vendorName);
        return jsonObject.toString();
    }

    private String getValue(@NonNull JSONObject jsonObject, @NonNull String key) {
        try {
            String result = jsonObject.getString(key);
            return result == null ? StringUtils.EMPTY : result;
        } catch (JSONException e) {
            return StringUtils.EMPTY;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
