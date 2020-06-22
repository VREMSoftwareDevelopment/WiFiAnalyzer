/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.os.Build;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.settings.ThemeStyle;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
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
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static com.vrem.wifianalyzer.wifi.graphutils.GraphConstantsKt.MAX_Y;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.annotation.LooperMode.Mode.PAUSED;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
@LooperMode(PAUSED)
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

        withSettings();

        fixture = new TimeGraphView(WiFiBand.GHZ2);
        fixture.setGraphViewWrapper(graphViewWrapper);
        fixture.setDataManager(dataManager);
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testUpdate() {
        // setup
        List<WiFiDetail> wiFiDetails = Collections.emptyList();
        Set<WiFiDetail> newSeries = Collections.emptySet();
        WiFiData wiFiData = new WiFiData(wiFiDetails, WiFiConnection.EMPTY);
        when(dataManager.addSeriesData(graphViewWrapper, wiFiDetails, MAX_Y)).thenReturn(newSeries);
        // execute
        fixture.update(wiFiData);
        // validate
        verify(graphViewWrapper).removeSeries(newSeries);
        verify(graphViewWrapper).updateLegend(GraphLegend.LEFT);
        verify(graphViewWrapper).visibility(View.VISIBLE);
        verifySettings();
    }

    private void verifySettings() {
        verify(settings).sortBy();
        verify(settings, times(2)).timeGraphLegend();
        verify(settings, times(2)).wiFiBand();
        verify(settings, times(2)).graphMaximumY();
        verify(settings).themeStyle();
    }

    private void withSettings() {
        when(settings.sortBy()).thenReturn(SortBy.SSID);
        when(settings.timeGraphLegend()).thenReturn(GraphLegend.LEFT);
        when(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2);
        when(settings.graphMaximumY()).thenReturn(MAX_Y);
        when(settings.themeStyle()).thenReturn(ThemeStyle.DARK);
    }

    @Test
    public void testGetGraphView() {
        // setup
        GraphView expected = mock(GraphView.class);
        when(graphViewWrapper.getGraphView()).thenReturn(expected);
        // execute
        GraphView actual = fixture.graphView();
        // validate
        assertEquals(expected, actual);
        verify(graphViewWrapper).getGraphView();
        verify(settings).timeGraphLegend();
        verify(settings).graphMaximumY();
        verify(settings).themeStyle();
    }
}