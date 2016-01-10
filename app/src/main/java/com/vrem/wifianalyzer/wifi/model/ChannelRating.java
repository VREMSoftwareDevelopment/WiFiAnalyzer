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

import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ChannelRating {
    final static int STRENGTH_SIZE = Strength.values().length;

    private Map<Integer, List<DetailsInfo>> wiFiChannels = new TreeMap<>();

    public ChannelRating() {
    }

    public int getAPCount(int channel) {
        int count = 0;
        int[] counts = calculateChannelDistribution(channel);
        for (int value : counts) {
            count += value;
        }
        return count;
    }

    public int getCount(int channel) {
        int[] counts = calculateChannelDistribution(channel);
        int count = 0;
        for (int i = STRENGTH_SIZE - 1; i > STRENGTH_SIZE / 2 - 1 && count < STRENGTH_SIZE; i--) {
            if (counts[i] >= 2) {
                count = STRENGTH_SIZE;
            } else {
                count += counts[i];
            }
        }
        return count;
    }

    public int getColor(int channel) {
        int count = getCount(channel);
        int color = R.color.success_color;
        if (count >= STRENGTH_SIZE) {
            color = R.color.error_color;
        } else if (count >= 1) {
            color = R.color.warning_color;
        }
        return color;
    }

    public void setWiFiChannels(@NonNull Map<Integer, List<DetailsInfo>> wiFiChannels) {
        this.wiFiChannels = wiFiChannels;
    }


    private int[] calculateChannelDistribution(int channel) {
        List<DetailsInfo> details = collectOverlappingChannels(channel);

        int[] count = new int[STRENGTH_SIZE];
        for (DetailsInfo detailsInfo : details) {
            if (!detailsInfo.isConnected()) {
                count[detailsInfo.getStrength().ordinal()]++;
            }
        }
        return count;
    }

    private List<DetailsInfo> collectOverlappingChannels(int channel) {
        List<DetailsInfo> details = new ArrayList<>();
        for (int i = channel - 2; i <= channel + 2; i++) {
            List<DetailsInfo> detailsInfos = wiFiChannels.get(i);
            if (detailsInfos != null) {
                details.addAll(detailsInfos);
            }
        }
        return details;
    }
}
