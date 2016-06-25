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

    protected String getMacAddress() {
        return macAddress;
    }

    protected String getVendorName() {
        return vendorName;
    }

    protected String toJson() throws JSONException {
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
