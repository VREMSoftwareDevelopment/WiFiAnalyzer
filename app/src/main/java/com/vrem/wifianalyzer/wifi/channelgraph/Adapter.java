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
package com.vrem.wifianalyzer.wifi.channelgraph;

import android.support.annotation.NonNull;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.wifi.UpdateNotifier;
import com.vrem.wifianalyzer.wifi.WiFiConstants;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetails;

class Adapter implements UpdateNotifier {
    private final MainContext mainContext = MainContext.INSTANCE;
    private final GraphView graphView;
    private final Constraints constraints;

    Adapter(@NonNull GraphView graphView, @NonNull Constraints constraints) {
        this.graphView = graphView;
        this.constraints = constraints;
        mainContext.getScanner().addUpdateNotifier(this);
    }

    @Override
    public void update(@NonNull WiFiData wifiData) {
        graphView.removeAllSeries();
        new Utils().updateLegendRenderer(graphView);

        int colorIndex = 0;
        for (WiFiDetails wifiDetails : wifiData.getWiFiList(mainContext.getSettings().getWiFiBand())) {
            int channel = wifiDetails.getChannel();
            if (!constraints.contains(channel)) {
                continue;
            }
            int level = wifiDetails.getLevel();
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                    new DataPoint(channel - WiFiConstants.CHANNEL_OFFSET, WiFiConstants.MIN_Y),
                    new DataPoint(channel - WiFiConstants.CHANNEL_OFFSET / 2, level),
                    new DataPoint(channel, level),
                    new DataPoint(channel + WiFiConstants.CHANNEL_OFFSET / 2, level),
                    new DataPoint(channel + WiFiConstants.CHANNEL_OFFSET, WiFiConstants.MIN_Y)
            });
            if (colorIndex == Colors.BLUE.ordinal()) {
                colorIndex++;
            }
            if (wifiDetails.isConnected()) {
                colorIndex = Colors.BLUE.ordinal();
            } else {
                if (colorIndex >= Colors.values().length - 1) {
                    colorIndex = 0;
                }
            }
            series.setColor(Colors.values()[colorIndex].getPrimary());
            series.setBackgroundColor(Colors.values()[colorIndex].getBackground());
            series.setDrawBackground(true);
            series.setTitle(wifiDetails.getTitle() + " " + channel);
            colorIndex++;
            graphView.addSeries(series);
        }
    }

}
