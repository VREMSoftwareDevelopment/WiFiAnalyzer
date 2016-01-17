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

enum Colors {
    RED         (0xFFf44336, 0x33f44336),
    PINK        (0xFFe91e63, 0x33e91e63),
    PURPLE      (0xFF9c27b0, 0x339c27b0),
    DEEP_PURPLE (0xFF673ab7, 0x33673ab7),
    INDIGO      (0xFF3f51b5, 0x333f51b5),
    BLUE        (0xFF2196f3, 0x332196f3),
    LIGHT_BLUE  (0xFF03a9f4, 0x3303a9f4),
    CYAN        (0xFF00bcd4, 0x3300bcd4),
    TEAL        (0xFF009688, 0x33009688),
    GREEN       (0xFF4caf50, 0x334caf50),
    LIGHT_GREEN (0xFF8bc34a, 0x338bc34a),
    LIME        (0xFFcddc39, 0x33cddc39),
    YELLOW      (0xFFffeb3b, 0x33ffeb3b),
    AMBER       (0xFFffc107, 0x33ffc107),
    ORANGE      (0xFFff9800, 0x33ff9800),
    DEEP_ORANGE (0xFFff5722, 0x33ff5722),
    BROWN       (0xFF795548, 0x33795548),
    GREY        (0xFF9e9e9e, 0x339e9e9e),
    BLUE_GREY   (0xFF607d8b, 0x33607d8b);

    private int primary;
    private int background;

    Colors(int primary, int background) {
        this.primary = primary;
        this.background = background;
    }

    int getPrimary() {
        return primary;
    }

    int getBackground() {
        return background;
    }
}
