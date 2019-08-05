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

import com.jjoe64.graphview.GraphView;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.scanner.UpdateNotifier;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Transformer;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class GraphAdapter implements UpdateNotifier {
    private final List<GraphViewNotifier> graphViewNotifiers;

    public GraphAdapter(@NonNull List<GraphViewNotifier> graphViewNotifiers) {
        this.graphViewNotifiers = graphViewNotifiers;
    }

    @NonNull
    public List<GraphView> getGraphViews() {
        return new ArrayList<>(CollectionUtils.collect(graphViewNotifiers, new ToGraphView()));
    }

    @Override
    public void update(@NonNull WiFiData wiFiData) {
        IterableUtils.forEach(graphViewNotifiers, new UpdateClosure(wiFiData));
    }

    @NonNull
    public List<GraphViewNotifier> getGraphViewNotifiers() {
        return graphViewNotifiers;
    }

    private class UpdateClosure implements Closure<GraphViewNotifier> {
        private final WiFiData wiFiData;

        private UpdateClosure(@NonNull WiFiData wiFiData) {
            this.wiFiData = wiFiData;
        }

        @Override
        public void execute(GraphViewNotifier graphViewNotifier) {
            graphViewNotifier.update(wiFiData);
        }
    }

    private class ToGraphView implements Transformer<GraphViewNotifier, GraphView> {
        @Override
        public GraphView transform(GraphViewNotifier input) {
            return input.getGraphView();
        }
    }
}
