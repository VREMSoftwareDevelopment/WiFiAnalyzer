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

import com.jjoe64.graphview.GraphView;
import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.graphutils.GraphViewNotifier;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TimeGraphAdapterTest {

    private TimeGraphAdapter fixture;

    @Before
    public void setUp() {
        RobolectricUtil.INSTANCE.getActivity();

        fixture = new TimeGraphAdapter();
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testGetGraphViewNotifiers() throws Exception {
        // execute
        List<GraphViewNotifier> graphViewNotifiers = fixture.getGraphViewNotifiers();
        // validate
        assertEquals(WiFiBand.values().length, graphViewNotifiers.size());
    }

    @Test
    public void testGetGraphViews() throws Exception {
        // execute
        List<GraphView> graphViews = fixture.getGraphViews();
        // validate
        assertEquals(WiFiBand.values().length, graphViews.size());
    }

}