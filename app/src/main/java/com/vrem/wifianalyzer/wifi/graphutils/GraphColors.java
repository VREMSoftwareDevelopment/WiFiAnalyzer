/*
 * WiFiAnalyzer
 * Copyright (C) 2017  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.wifi.graphutils;

import android.content.res.Resources;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class GraphColors {
    private final List<GraphColor> availableGraphColors;
    private final Stack<GraphColor> graphColors;

    GraphColors() {
        graphColors = new Stack<>();
        availableGraphColors = new ArrayList<>();
    }

    private List<GraphColor> getAvailableGraphColors() {
        if (availableGraphColors.isEmpty()) {
            Resources resources = MainContext.INSTANCE.getMainActivity().getResources();
            String[] colorsAsStrings = resources.getStringArray(R.array.graph_colors);
            for (int i = 0; i < colorsAsStrings.length; i += 2) {
                GraphColor graphColor = new GraphColor(Long.parseLong(colorsAsStrings[i].substring(1), 16), Long.parseLong(colorsAsStrings[i + 1].substring(1), 16));
                availableGraphColors.add(graphColor);
            }
        }
        return availableGraphColors;
    }

    GraphColor getColor() {
        if (graphColors.isEmpty()) {
            graphColors.addAll(getAvailableGraphColors());
        }
        return graphColors.pop();
    }

    void addColor(long primaryColor) {
        GraphColor graphColor = findColor(primaryColor);
        if (graphColor == null || graphColors.contains(graphColor)) {
            return;
        }
        graphColors.push(graphColor);
    }

    private GraphColor findColor(long primaryColor) {
        return IterableUtils.find(getAvailableGraphColors(), new ColorPredicate(primaryColor));
    }

    private class ColorPredicate implements Predicate<GraphColor> {
        private final long primaryColor;

        private ColorPredicate(long primaryColor) {
            this.primaryColor = primaryColor;
        }

        @Override
        public boolean evaluate(GraphColor graphColor) {
            return primaryColor == graphColor.getPrimary();
        }
    }

}
