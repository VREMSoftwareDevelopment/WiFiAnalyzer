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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WifiInformation {
    private final Connection connection;
    private final List<DetailsInfo> detailsInfoList = new ArrayList<>();
    private final List<Relationship> relationships = new ArrayList<>();

    public WifiInformation() {
        this(null, null);
    }

    public WifiInformation(List<ScanResult> scanResults, WifiInfo wifiInfo) {
        connection = new Connection(wifiInfo);
        if (scanResults != null) {
            for (ScanResult scanResult : scanResults) {
                DetailsInfo detailsInfo = new Details(scanResult);
                if (!connection.addDetailsInfo(detailsInfo)) {
                    detailsInfoList.add(detailsInfo);
                }
            }
            populateRelationship();
            sortRelationship();
        }
    }

    private void populateRelationship() {
        Collections.sort(detailsInfoList, new SSIDComparator());
        Relationship relationship = null;
        for (DetailsInfo detailsInfo : detailsInfoList) {
            if (relationship == null || !relationship.parent.getSSID().equals(detailsInfo.getSSID())) {
                relationship = new Relationship(detailsInfo);
                relationships.add(relationship);
            } else {
                relationship.children.add(detailsInfo);
            }
        }
    }

    private void sortRelationship() {
        Collections.sort(relationships);
        for (Relationship information : relationships) {
            Collections.sort(information.children, new LevelComparator());
        }
    }

    public int getParentsSize() {
        return relationships.size();
    }

    public DetailsInfo getParent(int index) {
        return relationships.get(index).parent;
    }

    public int getChildrenSize(int index) {
        return relationships.get(index).children.size();
    }

    public DetailsInfo getChild(int indexParent, int indexChild) {
        return relationships.get(indexParent).children.get(indexChild);
    }

    public Connection getConnection() {
        return connection;
    }

    class SSIDComparator implements Comparator<DetailsInfo> {
        @Override
        public int compare(DetailsInfo lhs, DetailsInfo rhs) {
            int result = lhs.getSSID().compareTo(rhs.getSSID());
            if (result == 0) {
                result = lhs.getLevel() - rhs.getLevel();
                if (result == 0) {
                    result = lhs.getBSSID().compareTo(rhs.getBSSID());
                }
            }
            return result;
        }
    }

    class LevelComparator implements Comparator<DetailsInfo> {
        @Override
        public int compare(DetailsInfo lhs, DetailsInfo rhs) {
            int result = lhs.getLevel() - rhs.getLevel();
            if (result == 0) {
                result = lhs.getSSID().compareTo(rhs.getSSID());
                if (result == 0) {
                    result = lhs.getBSSID().compareTo(rhs.getBSSID());
                }
            }
            return result;
        }
    }

    class Relationship implements Comparable<Relationship> {
        public final DetailsInfo parent;
        public final List<DetailsInfo> children = new ArrayList<>();

        public Relationship(@NonNull DetailsInfo parent) {
            this.parent = parent;
        }

        @Override
        public int compareTo(@NonNull Relationship other) {
            int result = parent.getLevel() - other.parent.getLevel();
            if (result == 0) {
                result = parent.getSSID().compareTo(other.parent.getSSID());
                if (result == 0) {
                    result = parent.getBSSID().compareTo(other.parent.getBSSID());
                }
            }
            return result;
        }
    }

}
