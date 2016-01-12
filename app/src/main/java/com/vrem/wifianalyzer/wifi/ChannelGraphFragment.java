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
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetails;

import java.util.List;

public class ChannelGraphFragment extends Fragment {

    private static final int MIN_X = -1;
    private static final int MAX_X = 167;
    private static final int CNT_X = MIN_X + MAX_X;

    private static final int MIN_Y = -100;
    private static final int MAX_Y = -30;
    private static final int CNT_Y = 8;

    private final MainContext mainContext = MainContext.INSTANCE;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.channel_graph_content, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.channelGraphRefresh);
        swipeRefreshLayout.setOnRefreshListener(new ListViewOnRefreshListener());

        GraphView graphView = makeGraphView(view);
        new ChannelGraphAdapter(graphView);

        return view;
    }

    private GraphView makeGraphView(View view) {
        GraphView graphView = (GraphView) view.findViewById(R.id.channelGraph);
        graphView.addSeries(makeDefaultSeries());
        graphView.setMinimumWidth(20);

        GridLabelRenderer gridLabelRenderer = graphView.getGridLabelRenderer();
        gridLabelRenderer.setHighlightZeroLines(false);

        gridLabelRenderer.setHorizontalAxisTitle("Channel");
//        gridLabelRenderer.setNumHorizontalLabels(CNT_X);
        gridLabelRenderer.setHorizontalLabelsVisible(false);

        gridLabelRenderer.setNumVerticalLabels(CNT_Y);

        Viewport viewport = graphView.getViewport();
        viewport.setScrollable(true);

/*
        viewport.setXAxisBoundsManual(true);
        viewport.setMinX(MIN_X);
        viewport.setMaxX(MAX_X);
*/

        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(MIN_Y);
        viewport.setMaxY(MAX_Y);

        return graphView;
    }

    private Series makeDefaultSeries() {
        LineGraphSeries<DataPoint> series =
                new LineGraphSeries<>(new DataPoint[]{new DataPoint(-1, MIN_Y)});
        series.setDrawDataPoints(false);
        return series;
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

    class ChannelGraphAdapter implements UpdateNotifier {
        private final MainContext mainContext = MainContext.INSTANCE;
        private final GraphView graphView;

        ChannelGraphAdapter(@NonNull GraphView graphView) {
            this.graphView = graphView;
            mainContext.getScanner().addUpdateNotifier(this);
        }

        @Override
        public void update(@NonNull WiFiData wifiData) {
            graphView.removeAllSeries();
            graphView.addSeries(makeDefaultSeries());

            List<WiFiDetails> wifiList = wifiData.getWiFiList();
            for (WiFiDetails wifiDetails : wifiList) {
                String ssid = wifiDetails.getSSID();
                int channel = wifiDetails.getChannel();
                int level = wifiDetails.getLevel();
                LineGraphSeries<DataPoint> series =
                        new LineGraphSeries<>(new DataPoint[]{
                                new DataPoint(channel - 2, MIN_Y),
                                new DataPoint(channel - 1, level),
                                new DataPoint(channel, level),
                                new DataPoint(channel + 1, level),
                                new DataPoint(channel + 2, MIN_Y),
                        });
                series.setTitle(ssid);
                graphView.addSeries(series);
            }
        }
    }
}
