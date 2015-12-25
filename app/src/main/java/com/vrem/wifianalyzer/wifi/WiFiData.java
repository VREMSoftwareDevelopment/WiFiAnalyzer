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
package com.vrem.wifianalyzer.wifi;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.vendor.VendorService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WiFiData {
    private final Connection connection;
    private final List<DetailsInfo> detailsInfoList = new ArrayList<>();
    private final List<WiFiRelationship> wifiRelationships = new ArrayList<>();

    public WiFiData() {
        connection = new Connection();
    }

    public WiFiData(List<ScanResult> scanResults, WifiInfo wifiInfo, @NonNull VendorService vendorService, @NonNull GroupBy groupBy) {
        connection = new Connection(wifiInfo);
        if (scanResults != null) {
            for (ScanResult scanResult : scanResults) {
                String vendorName = vendorService.getVendorName(scanResult.BSSID);
                DetailsInfo detailsInfo = new Details(scanResult, vendorName);
                if (!connection.addDetailsInfo(detailsInfo)) {
                    detailsInfoList.add(detailsInfo);
                }
            }
            populateRelationship(groupBy);
        }
    }

    private void populateRelationship(@NonNull GroupBy groupBy) {
        Collections.sort(detailsInfoList, groupBy.getSortOrder());
        WiFiRelationship wifiRelationship = null;
        for (DetailsInfo detailsInfo : detailsInfoList) {
            if (wifiRelationship == null || groupBy.getGroupBy().compare(wifiRelationship.getParent(), detailsInfo) != 0) {
                wifiRelationship = new WiFiRelationship(detailsInfo);
                wifiRelationships.add(wifiRelationship);
            } else {
                wifiRelationship.addChild(detailsInfo);
            }
        }
        Collections.sort(wifiRelationships);
    }

    public int getParentsSize() {
        return wifiRelationships.size();
    }

    private boolean isInParentRange(int index) {
        return index >= 0 && index < getParentsSize();
    }

    private boolean isInChildrenRange(int indexParent, int indexChild) {
        return isInParentRange(indexParent) && indexChild >= 0 && indexChild < getChildrenSize(indexParent);
    }

    public DetailsInfo getParent(int index) {
        return isInParentRange(index) ? wifiRelationships.get(index).getParent() : null;
    }

    public int getChildrenSize(int index) {
        return isInParentRange(index) ? wifiRelationships.get(index).getChildrenSize() : 0;
    }

    public DetailsInfo getChild(int indexParent, int indexChild) {
        return isInChildrenRange(indexParent, indexChild) ? wifiRelationships.get(indexParent).getChild(indexChild) : null;
    }

    public Connection getConnection() {
        return connection;
    }
}
