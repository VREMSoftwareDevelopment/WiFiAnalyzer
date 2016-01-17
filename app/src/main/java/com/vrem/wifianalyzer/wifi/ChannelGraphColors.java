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
package com.vrem.wifianalyzer.wifi;

public enum ChannelGraphColors {
    RED(0xFFFF0000, 0x33FF0000),
    GREEN(0xFF00FF00, 0x3300FF00),
    BLUE(0xFF0000FF, 0x330000FF),
    YELLOW(0xFFFFFF00, 0x33FFFF00),
    CYAN(0xFF00FFFF, 0x3300FFFF),
    MAGENTA(0xFFFF00FF, 0x33FF00FF),
    AQUA(0xFF00FFFF, 0x3300FFFF),
    FUCHSIA(0xFFFF00FF, 0x33FF00FF),
    LIME(0xFF00FF00, 0x3300FF00),
    MAROON(0xFF800000, 0x33800000),
    NAVY(0xFF000080, 0x33000080),
    OLIVE(0xFF808000, 0x33808000),
    PURPLE(0xFF800080, 0x33800080),
    SILVER(0xFFC0C0C0, 0x33C0C0C0),
    TEAL(0xFF008080, 0x33008080);

    private int primary;
    private int background;

    ChannelGraphColors(int primary, int background) {
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
