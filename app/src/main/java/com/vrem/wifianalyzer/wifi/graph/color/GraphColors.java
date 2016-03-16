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

package com.vrem.wifianalyzer.wifi.graph.color;

import java.util.Arrays;
import java.util.Stack;

public class GraphColors {

    public static final GraphColor TRANSPARENT = new GraphColor(0x009E9E9E, 0x009E9E9E);

    static final GraphColor[] GRAPH_COLORS = new GraphColor[]{
            new GraphColor(0xFFFB1554, 0x33FB1554),
            new GraphColor(0xFF74FF89, 0x3374FF89),
            new GraphColor(0xFF8B1EFC, 0x338B1EFC),
            new GraphColor(0xFF3B6D9D, 0x333B6D9D),
            new GraphColor(0xFF886D03, 0x33886D03),
            new GraphColor(0xFFFDD3CD, 0x33FDD3CD),
            new GraphColor(0xFFE077EA, 0x33E077EA),
            new GraphColor(0xFF6EFFE6, 0x336EFFE6),
            new GraphColor(0xFF034713, 0x33034713),
            new GraphColor(0xFF411457, 0x33411457),
            new GraphColor(0xFF74230C, 0x3374230C),
            new GraphColor(0xFFA6D3F5, 0x33A6D3F5),
            new GraphColor(0xFFF98A96, 0x33F98A96),
            new GraphColor(0xFF2F944C, 0x332F944C),
            new GraphColor(0xFFEAA579, 0x33EAA579),
            new GraphColor(0xFF3C7172, 0x333C7172),
            new GraphColor(0xFFC55616, 0x33C55616),
            new GraphColor(0xFF94136D, 0x3394136D),
            new GraphColor(0xFF2ECF76, 0x332ECF76),
            new GraphColor(0xFF2196F3, 0x332196F3)
    };

    final Stack<GraphColor> colors = new Stack<>();

    public GraphColor getColor() {
        if (colors.isEmpty()) {
            colors.addAll(Arrays.asList(GRAPH_COLORS));
        }
        return colors.pop();
    }

    public boolean addColor(int primaryColor) {
        GraphColor graphColor = findColor(primaryColor);
        if (graphColor == null || colors.contains(graphColor)) {
            return false;
        }
        colors.push(graphColor);
        return true;
    }

    private GraphColor findColor(int primaryColor) {
        for (GraphColor graphColor : GRAPH_COLORS) {
            if (primaryColor == graphColor.getPrimary()) {
                return graphColor;
            }
        }
        return null;
    }

}
