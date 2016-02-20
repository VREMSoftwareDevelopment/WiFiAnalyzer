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

import java.util.Random;

public enum GraphColors {
    RED(0xFFF44336, 0x33F44336),
    PINK(0xFFE91E63, 0x33E91E63),
    PURPLE(0xFF9C27B0, 0x339C27B0),
    DEEP_PURPLE(0xFF673AB7, 0x33673AB7),
    INDIGO(0xFF3F51B5, 0x333F51B5),
    LIGHT_BLUE(0xFF03A9F4, 0x3303A9F4),
    CYAN(0xFF00BCD4, 0x3300BCD4),
    TEAL(0xFF009688, 0x33009688),
    GREEN(0xFF4CAF50, 0x334CAF50),
    LIGHT_GREEN(0xFF8BC34A, 0x338BC34A),
    LIME(0xFFCDDC39, 0x33CDDC39),
    YELLOW(0xFFFFEB3B, 0x33FFEB3B),
    AMBER(0xFFFFC107, 0x33FFC107),
    ORANGE(0xFFFF9800, 0x33FF9800),
    DEEP_ORANGE(0xFFFF5722, 0x33FF5722),
    BROWN(0xFF795548, 0x33795548),
    GREY(0xFF9E9E9E, 0x339E9E9E),
    BLUE_GREY(0xFF607D8B, 0x33607D8B),
    // NOTE: Do NOT use the last two colors
    BLUE(0xFF2196F3, 0x332196F3),
    TRANSPARENT(0x009E9E9E, 0x009E9E9E);

    private final int primary;
    private final int background;

    GraphColors(int primary, int background) {
        this.primary = primary;
        this.background = background;
    }

    public static GraphColors findRandomColor() {
        return GraphColors.values()[new Random().nextInt(GraphColors.values().length - 3)];
    }

    public int getPrimary() {
        return primary;
    }

    public int getBackground() {
        return background;
    }

}
