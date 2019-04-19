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
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.wifi.model.WiFiData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GraphAdapterTest {
    @Mock
    private GraphViewNotifier graphViewNotifier;
    @Mock
    private GraphView graphView;
    @Mock
    private WiFiData wifiData;

    private GraphAdapter fixture;

    @Before
    public void setUp() {
        fixture = new GraphAdapter(Collections.singletonList(graphViewNotifier));
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testUpdate() {
        // execute
        fixture.update(wifiData);
        // validate
        verify(graphViewNotifier).update(wifiData);
    }

    @Test
    public void testGetGraphViews() {
        // setup
        when(graphViewNotifier.getGraphView()).thenReturn(graphView);
        // execute
        List<GraphView> actual = fixture.getGraphViews();
        // validate
        assertEquals(1, actual.size());
        assertEquals(graphView, actual.get(0));
        verify(graphViewNotifier).getGraphView();
    }

}