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

package com.vrem.wifianalyzer.wifi.graph.tools;

import android.support.annotation.NonNull;

import com.jjoe64.graphview.GraphView;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.scanner.UpdateNotifier;

import java.util.ArrayList;
import java.util.List;

public class GraphAdapter implements UpdateNotifier {
    private final List<GraphViewNotifier> graphViewNotifiers;

    public GraphAdapter(@NonNull List<GraphViewNotifier> graphViewNotifiers) {
        this.graphViewNotifiers = graphViewNotifiers;
        MainContext.INSTANCE.getScanner().addUpdateNotifier(this);
    }

    public List<GraphView> getGraphViews() {
        List<GraphView> graphViews = new ArrayList<>();
        for (GraphViewNotifier graphViewNotifier : graphViewNotifiers) {
            graphViews.add(graphViewNotifier.getGraphView());
        }
        return graphViews;
    }

    @Override
    public void update(@NonNull WiFiData wiFiData) {
        for (GraphViewNotifier graphViewNotifier : graphViewNotifiers) {
            graphViewNotifier.update(wiFiData);
        }
    }

    public List<GraphViewNotifier> getGraphViewNotifiers() {
        return graphViewNotifiers;
    }
}
