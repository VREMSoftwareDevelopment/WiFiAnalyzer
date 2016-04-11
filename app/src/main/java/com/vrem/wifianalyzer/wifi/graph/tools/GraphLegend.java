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

import com.jjoe64.graphview.LegendRenderer;

public enum GraphLegend {
    LEFT(new DisplayLeft()),
    RIGHT(new DisplayRight()),
    HIDE(new DisplayNone());

    private final Display display;

    GraphLegend(Display display) {
        this.display = display;
    }

    public static GraphLegend find(int index, @NonNull GraphLegend defaultValue) {
        try {
            return values()[index];
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public void display(LegendRenderer legendRenderer) {
        display.display(legendRenderer);
    }

    protected Display getDisplay() {
        return display;
    }

    private interface Display {
        void display(LegendRenderer legendRenderer);
    }

    protected static class DisplayNone implements Display {
        @Override
        public void display(LegendRenderer legendRenderer) {
            legendRenderer.setVisible(false);
        }
    }

    protected static class DisplayLeft implements Display {
        @Override
        public void display(LegendRenderer legendRenderer) {
            legendRenderer.setVisible(true);
            legendRenderer.setFixedPosition(0, 0);
        }
    }

    protected static class DisplayRight implements Display {
        @Override
        public void display(LegendRenderer legendRenderer) {
            legendRenderer.setVisible(true);
            legendRenderer.setAlign(LegendRenderer.LegendAlign.TOP);
        }
    }

}
