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

package com.vrem.wifianalyzer.wifi.channelgraph;

import android.os.Build;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.TitleLineGraphSeries;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.band.WiFiWidth;
import com.vrem.wifianalyzer.wifi.graphutils.DataPointsEquals;
import com.vrem.wifianalyzer.wifi.graphutils.GraphConstants;
import com.vrem.wifianalyzer.wifi.graphutils.GraphViewWrapper;
import com.vrem.wifianalyzer.wifi.model.WiFiAdditional;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.annotation.LooperMode.Mode.PAUSED;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
@LooperMode(PAUSED)
public class DataManagerTest {
    private static final int LEVEL = -40;

    private DataManager fixture;

    @Before
    public void setUp() {
        RobolectricUtil.INSTANCE.getActivity();
        fixture = new DataManager();
    }

    @Test
    public void testGetNewSeries() {
        // setup
        Pair<WiFiChannel, WiFiChannel> wiFiChannelPair = WiFiBand.GHZ2.getWiFiChannels().getWiFiChannelPairs().get(0);
        List<WiFiDetail> expected = makeWiFiDetails(wiFiChannelPair.first.getFrequency());
        // execute
        Set<WiFiDetail> actual = fixture.getNewSeries(expected, wiFiChannelPair);
        // validate
        assertEquals(expected.size() - 1, actual.size());
        assertTrue(actual.contains(expected.get(0)));
        assertFalse(actual.contains(expected.get(1)));
        assertTrue(actual.contains(expected.get(2)));
    }

    @Test
    public void testGetDataPoints() {
        // setup
        WiFiDetail expected = makeWiFiDetail("SSID", 2455);
        // execute
        DataPoint[] actual = fixture.getDataPoints(expected, GraphConstants.MAX_Y);
        // validate
        assertEquals(5, actual.length);
        assertEquals(new DataPoint(2445, -100).toString(), actual[0].toString());
        assertEquals(new DataPoint(2450, LEVEL).toString(), actual[1].toString());
        assertEquals(new DataPoint(2455, LEVEL).toString(), actual[2].toString());
        assertEquals(new DataPoint(2460, LEVEL).toString(), actual[3].toString());
        assertEquals(new DataPoint(2465, -100).toString(), actual[4].toString());
    }

    @Test
    public void testGetDataPointsExpectLevelToEqualToLevelMax() {
        // setup
        int expectedLevel = LEVEL - 10;
        WiFiDetail expected = makeWiFiDetail("SSID", 2455);
        // execute
        DataPoint[] actual = fixture.getDataPoints(expected, expectedLevel);
        // validate
        assertEquals(5, actual.length);
        assertEquals(new DataPoint(2445, -100).toString(), actual[0].toString());
        assertEquals(new DataPoint(2450, expectedLevel).toString(), actual[1].toString());
        assertEquals(new DataPoint(2455, expectedLevel).toString(), actual[2].toString());
        assertEquals(new DataPoint(2460, expectedLevel).toString(), actual[3].toString());
        assertEquals(new DataPoint(2465, -100).toString(), actual[4].toString());
    }

    @Test
    public void testAddSeriesDataWithExistingWiFiDetails() {
        // setup
        GraphViewWrapper graphViewWrapper = mock(GraphViewWrapper.class);
        WiFiDetail wiFiDetail = makeWiFiDetail("SSID", 2455);
        Set<WiFiDetail> wiFiDetails = Collections.singleton(wiFiDetail);
        DataPoint[] dataPoints = fixture.getDataPoints(wiFiDetail, GraphConstants.MAX_Y);
        when(graphViewWrapper.isNewSeries(wiFiDetail)).thenReturn(false);
        // execute
        fixture.addSeriesData(graphViewWrapper, wiFiDetails, GraphConstants.MAX_Y);
        // validate
        verify(graphViewWrapper).isNewSeries(wiFiDetail);
        verify(graphViewWrapper).updateSeries(
            eq(wiFiDetail),
            argThat(new DataPointsEquals(dataPoints)),
            eq(Boolean.TRUE));
    }

    @Test
    public void testAddSeriesDataNewWiFiDetails() {
        // setup
        GraphViewWrapper graphViewWrapper = mock(GraphViewWrapper.class);
        WiFiDetail wiFiDetail = makeWiFiDetail("SSID", 2455);
        Set<WiFiDetail> wiFiDetails = Collections.singleton(wiFiDetail);
        when(graphViewWrapper.isNewSeries(wiFiDetail)).thenReturn(true);
        // execute
        fixture.addSeriesData(graphViewWrapper, wiFiDetails, GraphConstants.MAX_Y);
        // validate
        verify(graphViewWrapper).isNewSeries(wiFiDetail);
        verify(graphViewWrapper).addSeries(
            eq(wiFiDetail),
            ArgumentMatchers.<TitleLineGraphSeries<DataPoint>>any(),
            eq(Boolean.TRUE));
    }

    private WiFiDetail makeWiFiDetail(@NonNull String SSID, int frequency) {
        WiFiSignal wiFiSignal = new WiFiSignal(frequency, frequency, WiFiWidth.MHZ_20, LEVEL, true);
        return new WiFiDetail(SSID, "BSSID", StringUtils.EMPTY, wiFiSignal, WiFiAdditional.EMPTY);
    }

    private List<WiFiDetail> makeWiFiDetails(int frequency) {
        return Arrays.asList(makeWiFiDetail("SSID1", frequency), makeWiFiDetail("SSID2", -frequency), makeWiFiDetail("SSID3", frequency));
    }
}
