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

import java.util.List;

public class ChannelRating {
    private List<DetailsInfo> details;

    public ChannelRating(List<DetailsInfo> details) {
        this.details = details;
    }

    public int getCount() {
        if (details == null) {
            return 0;
        }
        int count = details.size();
        for (DetailsInfo detailsInfo : details) {
            if (detailsInfo.isConnected()) {
                count--;
                break;
            }
        }
        return count;
    }

    public int getColor() {
        int count = getCount();
        int color = R.color.success_color;
        if (count > 3) {
            color = R.color.error_color;
        } else if (count > 1) {
            color = R.color.warning_color;
        }
        return color;
    }
}
