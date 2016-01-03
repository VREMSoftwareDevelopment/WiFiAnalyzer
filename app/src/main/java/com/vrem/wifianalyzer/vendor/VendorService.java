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

import android.os.AsyncTask;

import com.vrem.wifianalyzer.MainContext;

import org.apache.commons.lang3.StringUtils;

import java.util.Set;
import java.util.TreeSet;

public class VendorService {

    private MainContext mainContext = MainContext.INSTANCE;

    private final Set<String> cache = new TreeSet<>();
    private VendorRemoteCall vendorRemoteCall;

    public VendorService() {
    }

    public String getVendorName(String macAddress) {
        Database database = mainContext.getDatabase();
        String result = database.find(macAddress);
        if (result != null) {
            return result;
        }
        String key = MacAddress.clean(macAddress);
        if (!cache.contains(key)) {
            cache.add(key);
            VendorRemoteCall vendorRemoteCall= mainContext.getVendorRemoteCall();
            vendorRemoteCall.execute(macAddress);
        }
        return StringUtils.EMPTY;
    }

    void clear() {
        cache.clear();
    }

}
