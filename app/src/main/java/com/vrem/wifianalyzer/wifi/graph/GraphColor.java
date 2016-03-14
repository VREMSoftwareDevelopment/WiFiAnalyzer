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

package com.vrem.wifianalyzer.wifi.graph;

import org.apache.commons.lang3.ArrayUtils;

class GraphColor {

    static final GraphColor TRANSPARENT = new GraphColor(0x009E9E9E, 0x009E9E9E);

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
            new GraphColor(0xFF43310B, 0x3343310B),
            new GraphColor(0xFFC55616, 0x33C55616),
            new GraphColor(0xFF94136D, 0x3394136D),
            new GraphColor(0xFF2ECF76, 0x332ECF76)
    };

    private final int primary;
    private final int background;

    private GraphColor(int primary, int background) {
        this.primary = primary;
        this.background = background;
    }

    static GraphColor findColor(GraphColor graphColor) {
        int colorIndex = 0;
        if (graphColor != null) {
            colorIndex = ArrayUtils.indexOf(GRAPH_COLORS, graphColor);
            if (colorIndex < 0) {
                colorIndex = 0;
            }
            colorIndex += 1;
            if (colorIndex >= GRAPH_COLORS.length) {
                colorIndex = 0;
            }
        }
        return GRAPH_COLORS[colorIndex];
    }

    int getPrimary() {
        return primary;
    }

    int getBackground() {
        return background;
    }

}
