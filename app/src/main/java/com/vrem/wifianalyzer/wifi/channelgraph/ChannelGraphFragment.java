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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.WiFiConstants;
import com.vrem.wifianalyzer.wifi.model.WiFiBand;

public class ChannelGraphFragment extends Fragment {
    private final MainContext mainContext = MainContext.INSTANCE;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.channel_graph_content, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.channelGraphRefresh);
        swipeRefreshLayout.setOnRefreshListener(new ListViewOnRefreshListener());

        Constraints constraints = new Constraints(WiFiBand.TWO_POINT_FOUR);
        AxisLabel axisLabel = new AxisLabel(constraints);
        GraphView graphView = makeGraphView(view, constraints);
        new Adapter(graphView, constraints);

        return view;
    }

    private GraphView makeGraphView(@NonNull View view, @NonNull Constraints constraints) {
        GraphView graphView = (GraphView) view.findViewById(R.id.channelGraph);
        graphView.setMinimumWidth(20);

        GridLabelRenderer gridLabelRenderer = graphView.getGridLabelRenderer();
        gridLabelRenderer.setHighlightZeroLines(false);
        gridLabelRenderer.setLabelFormatter(new AxisLabel(constraints));
        gridLabelRenderer.setNumHorizontalLabels(constraints.cntX());
        gridLabelRenderer.setNumVerticalLabels(WiFiConstants.CNT_Y);

        gridLabelRenderer.setHorizontalAxisTitle(getResources().getString(R.string.graph_wifi_channels));
        gridLabelRenderer.setHorizontalLabelsVisible(true);

        gridLabelRenderer.setVerticalAxisTitle(getResources().getString(R.string.graph_signal_strength));
        gridLabelRenderer.setVerticalLabelsVisible(true);

        new Utils().updateLegendRenderer(graphView);

        Viewport viewport = graphView.getViewport();
        viewport.setScrollable(constraints.isScrollable());
        viewport.setScalable(false);

        viewport.setXAxisBoundsManual(true);
        viewport.setMinX(constraints.minXBounds());
        viewport.setMaxX(constraints.maxXBounds());

        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(WiFiConstants.MIN_Y);
        viewport.setMaxY(WiFiConstants.MAX_Y);

        return graphView;
    }

    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        mainContext.getScanner().update();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private class ListViewOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            refresh();
        }
    }
}
