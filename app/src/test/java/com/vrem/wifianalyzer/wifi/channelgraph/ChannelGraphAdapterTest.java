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

import com.jjoe64.graphview.GraphView;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.graphutils.GraphViewNotifier;
import com.vrem.wifianalyzer.wifi.model.WiFiConnection;
import com.vrem.wifianalyzer.wifi.model.WiFiData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import java.util.Collections;
import java.util.List;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.annotation.LooperMode.Mode.PAUSED;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
@LooperMode(PAUSED)
public class ChannelGraphAdapterTest {

    private ChannelGraphNavigation channelGraphNavigation;
    private ChannelGraphAdapter fixture;

    @Before
    public void setUp() {
        RobolectricUtil.INSTANCE.getActivity();

        channelGraphNavigation = mock(ChannelGraphNavigation.class);

        fixture = new ChannelGraphAdapter(channelGraphNavigation);
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testGetGraphViewNotifiers() {
        // setup
        int expected = expectedCount();
        // execute
        List<GraphViewNotifier> graphViewNotifiers = fixture.getGraphViewNotifiers();
        // validate
        assertEquals(expected, graphViewNotifiers.size());
    }

    private int expectedCount() {
        int expected = 0;
        for (WiFiBand wiFiBand : WiFiBand.values()) {
            expected += wiFiBand.getWiFiChannels().getWiFiChannelPairs().size();
        }
        return expected;
    }

    @Test
    public void testGetGraphViews() {
        // setup
        int expected = expectedCount();
        // execute
        List<GraphView> graphViews = fixture.getGraphViews();
        // validate
        assertEquals(expected, graphViews.size());
    }

    @Test
    public void testUpdate() {
        // setup
        WiFiData wiFiData = new WiFiData(Collections.emptyList(), WiFiConnection.EMPTY);
        // execute
        fixture.update(wiFiData);
        // validate
        verify(channelGraphNavigation).update(wiFiData);
    }

}