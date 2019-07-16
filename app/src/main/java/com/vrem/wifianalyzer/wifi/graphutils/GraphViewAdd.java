/*
 * WiFiAnalyzer
 * Copyright (C) 2019  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;

import org.apache.commons.collections4.Closure;

import androidx.annotation.NonNull;

public class GraphViewAdd implements Closure<GraphView> {
    private final ViewGroup viewGroup;

    public GraphViewAdd(@NonNull ViewGroup viewGroup) {
        this.viewGroup = viewGroup;
    }

    @Override
    public void execute(GraphView graphView) {
        viewGroup.addView(graphView);
    }
}

