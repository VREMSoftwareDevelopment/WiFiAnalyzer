/*
 * WiFiAnalyzer
 * Copyright (C) 2017  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.wifi.timegraph;

import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.graphutils.GraphConstants;
import com.vrem.wifianalyzer.wifi.graphutils.GraphLegend;
import com.vrem.wifianalyzer.wifi.graphutils.GraphViewWrapper;
import com.vrem.wifianalyzer.wifi.model.SortBy;
import com.vrem.wifianalyzer.wifi.model.WiFiConnection;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TimeGraphViewTest {
    private GraphViewWrapper graphViewWrapper;
    private DataManager dataManager;
    private Settings settings;
    private TimeGraphView fixture;

    @Before
    public void setUp() {
        RobolectricUtil.INSTANCE.getActivity();

        graphViewWrapper = mock(GraphViewWrapper.class);
        dataManager = mock(DataManager.class);

        settings = MainContextHelper.INSTANCE.getSettings();

        fixture = new TimeGraphView(WiFiBand.GHZ2);
        fixture.setGraphViewWrapper(graphViewWrapper);
        fixture.setDataManager(dataManager);
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testUpdate() throws Exception {
        // setup
        List<WiFiDetail> wiFiDetails = Collections.emptyList();
        Set<WiFiDetail> newSeries = Collections.emptySet();
        WiFiData wiFiData = new WiFiData(wiFiDetails, WiFiConnection.EMPTY, Collections.<String>emptyList());
        withSettings();
        when(dataManager.addSeriesData(graphViewWrapper, wiFiDetails, GraphConstants.MAX_Y)).thenReturn(newSeries);
        // execute
        fixture.update(wiFiData);
        // validate
        verify(graphViewWrapper).removeSeries(newSeries);
        verify(graphViewWrapper).updateLegend(GraphLegend.LEFT);
        verify(graphViewWrapper).setVisibility(View.VISIBLE);
        verifySettings();
    }

    private void verifySettings() {
        verify(settings).getSortBy();
        verify(settings, times(2)).getTimeGraphLegend();
        verify(settings, times(2)).getWiFiBand();
        verify(settings, times(2)).getGraphMaximumY();
    }

    private void withSettings() {
        when(settings.getSortBy()).thenReturn(SortBy.SSID);
        when(settings.getTimeGraphLegend()).thenReturn(GraphLegend.LEFT);
        when(settings.getWiFiBand()).thenReturn(WiFiBand.GHZ2);
        when(settings.getGraphMaximumY()).thenReturn(GraphConstants.MAX_Y);
    }

    @Test
    public void testGetGraphView() throws Exception {
        // setup
        GraphView expected = mock(GraphView.class);
        when(graphViewWrapper.getGraphView()).thenReturn(expected);
        // execute
        GraphView actual = fixture.getGraphView();
        // validate
        assertEquals(expected, actual);
        verify(graphViewWrapper).getGraphView();
    }
}