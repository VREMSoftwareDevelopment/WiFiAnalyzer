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

import com.jjoe64.graphview.LegendRenderer;

public enum GraphLegendPosition {
    LEFT(new PositionLeft()),
    RIGHT(new PositionRight()),
    HIDE(new PositionNone());

    private Position position;

    GraphLegendPosition(Position position) {
        this.position = position;
    }

    public static GraphLegendPosition find(String value) {
        try {
            return valueOf(value.toUpperCase());
        } catch (Exception e) {
            return GraphLegendPosition.LEFT;
        }
    }

    public void setPosition(LegendRenderer legendRenderer) {
        position.set(legendRenderer);
    }

    private interface Position {
        void set(LegendRenderer legendRenderer);
    }

    private static class PositionNone implements Position {
        @Override
        public void set(LegendRenderer legendRenderer) {
            // nothing to do
        }
    }

    private static class PositionLeft implements Position {
        @Override
        public void set(LegendRenderer legendRenderer) {
            legendRenderer.setFixedPosition(0, 0);
        }
    }

    private static class PositionRight implements Position {
        @Override
        public void set(LegendRenderer legendRenderer) {
            legendRenderer.setAlign(LegendRenderer.LegendAlign.TOP);
        }
    }

}
