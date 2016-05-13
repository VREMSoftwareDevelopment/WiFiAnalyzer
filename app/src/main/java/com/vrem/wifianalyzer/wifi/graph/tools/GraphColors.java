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

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class GraphColors {
    private final List<GraphColor> availableGraphColors;
    private final Stack<GraphColor> graphColors;

    protected GraphColors() {
        graphColors = new Stack<>();
        availableGraphColors = new ArrayList<>();
    }

    private List<GraphColor> getAvailableGraphColors() {
        if (availableGraphColors.isEmpty()) {
            String[] colorsAsStrings = MainContext.INSTANCE.getResources().getStringArray(R.array.graph_colors);
            for (int i = 0; i < colorsAsStrings.length; i += 2) {
                availableGraphColors.add(new GraphColor(Long.parseLong(colorsAsStrings[i].substring(1), 16), Long.parseLong(colorsAsStrings[i + 1].substring(1), 16)));
            }
        }
        return availableGraphColors;
    }

    protected GraphColor getColor() {
        if (graphColors.isEmpty()) {
            graphColors.addAll(getAvailableGraphColors());
        }
        return graphColors.pop();
    }

    protected boolean addColor(long primaryColor) {
        GraphColor graphColor = findColor(primaryColor);
        if (graphColor == null || graphColors.contains(graphColor)) {
            return false;
        }
        graphColors.push(graphColor);
        return true;
    }

    private GraphColor findColor(long primaryColor) {
        for (GraphColor graphColor : getAvailableGraphColors()) {
            if (primaryColor == graphColor.getPrimary()) {
                return graphColor;
            }
        }
        return null;
    }

}
