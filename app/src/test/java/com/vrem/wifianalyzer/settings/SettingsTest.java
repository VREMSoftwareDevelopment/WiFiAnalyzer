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

package com.vrem.wifianalyzer.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.graph.GraphLegend;
import com.vrem.wifianalyzer.wifi.model.GroupBy;
import com.vrem.wifianalyzer.wifi.model.SortBy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PreferenceManager.class)
public class SettingsTest {
    @Mock private Context context;
    @Mock private Resources resources;
    @Mock private SharedPreferences sharedPreferences;
    @Mock private SharedPreferences.Editor editor;

    private String keyValue;
    private Settings fixture;

    @Before
    public void setUp() throws Exception {
        mockStatic(PreferenceManager.class);

        MainContext.INSTANCE.setContext(context);

        keyValue = "xyz";
        fixture = new Settings();

        when(PreferenceManager.getDefaultSharedPreferences(context)).thenReturn(sharedPreferences);
        when(context.getResources()).thenReturn(resources);
    }

    @After
    public void tearDown() throws Exception {
        verifyStatic();
    }

    @Test
    public void testInitializeDefaultValues() throws Exception {
        fixture.initializeDefaultValues();
    }

    @Test
    public void testSharedPreferences() throws Exception {
        // execute
        SharedPreferences actual = fixture.getSharedPreferences();
        // validate
        assertEquals(sharedPreferences, actual);
    }

    @Test
    public void testScanInterval() throws Exception {
        // setup
        int defaultValue = 10;
        int expected = 11;
        when(context.getString(R.string.scan_interval_key)).thenReturn(keyValue);
        when(resources.getInteger(R.integer.scan_interval_default)).thenReturn(defaultValue);
        when(sharedPreferences.getInt(keyValue, defaultValue)).thenReturn(expected);
        // execute
        int actual = fixture.getScanInterval();
        // validate
        assertEquals(expected, actual);
        verify(context).getResources();
        verify(context).getString(R.string.scan_interval_key);
        verify(resources).getInteger(R.integer.scan_interval_default);
        verify(sharedPreferences).getInt(keyValue, defaultValue);
    }

    @Test
    public void testGroupBy() throws Exception {
        // setup
        withPreferences(R.string.group_by_key, GroupBy.NONE.ordinal(), GroupBy.CHANNEL.ordinal());
        // execute
        GroupBy actual = fixture.getGroupBy();
        // validate
        assertEquals(GroupBy.CHANNEL, actual);
        verifyPreferences(R.string.group_by_key, GroupBy.NONE.ordinal());
    }

    @Test
    public void testSortBy() throws Exception {
        // setup
        withPreferences(R.string.sort_by_key, SortBy.STRENGTH.ordinal(), SortBy.SSID.ordinal());
        // execute
        SortBy actual = fixture.getSortBy();
        // validate
        assertEquals(SortBy.SSID, actual);
        verifyPreferences(R.string.sort_by_key, SortBy.STRENGTH.ordinal());
    }

    @Test
    public void testThemeStyle() throws Exception {
        // setup
        withPreferences(R.string.theme_key, ThemeStyle.DARK.ordinal(), ThemeStyle.LIGHT.ordinal());
        // execute
        ThemeStyle actual = fixture.getThemeStyle();
        // validate
        assertEquals(ThemeStyle.LIGHT, actual);
        verifyPreferences(R.string.theme_key, ThemeStyle.DARK.ordinal());
    }

    @Test
    public void testChannelGraphLegend() throws Exception {
        // setup
        withPreferences(R.string.channel_graph_legend_key, GraphLegend.HIDE.ordinal(), GraphLegend.RIGHT.ordinal());
        // execute
        GraphLegend actual = fixture.getChannelGraphLegend();
        // validate
        assertEquals(GraphLegend.RIGHT, actual);
        verifyPreferences(R.string.channel_graph_legend_key, GraphLegend.HIDE.ordinal());
    }

    @Test
    public void testTimeGraphLegend() throws Exception {
        // setup
        withPreferences(R.string.time_graph_legend_key, GraphLegend.LEFT.ordinal(), GraphLegend.RIGHT.ordinal());
        // execute
        GraphLegend actual = fixture.getTimeGraphLegend();
        // validate
        assertEquals(GraphLegend.RIGHT, actual);
        verifyPreferences(R.string.time_graph_legend_key, GraphLegend.LEFT.ordinal());
    }

    @Test
    public void testWiFiBand() throws Exception {
        // setup
        withPreferences(R.string.wifi_band_key, WiFiBand.GHZ_2.ordinal(), WiFiBand.GHZ_5.ordinal());
        // execute
        WiFiBand actual = fixture.getWiFiBand();
        // validate
        assertEquals(WiFiBand.GHZ_5, actual);
        verifyPreferences(R.string.wifi_band_key, WiFiBand.GHZ_2.ordinal());
    }

    @Test
    public void testToggleWiFiBand() throws Exception {
        // setup
        withPreferences(R.string.wifi_band_key, WiFiBand.GHZ_2.ordinal(), WiFiBand.GHZ_5.ordinal());
        when(sharedPreferences.edit()).thenReturn(editor);
        // execute
        fixture.toggleWiFiBand();
        // validate
        verify(context, times(2)).getString(R.string.wifi_band_key);
        verify(sharedPreferences).getString(keyValue, "" + WiFiBand.GHZ_2.ordinal());
        verify(sharedPreferences).edit();
        verify(editor).putInt(keyValue, WiFiBand.GHZ_5.toggle().ordinal());
        verify(editor).apply();
    }

    void withPreferences(int keyIndex, int defaultValue, int returnValue) {
        when(context.getString(keyIndex)).thenReturn(keyValue);
        when(sharedPreferences.getString(keyValue, "" + defaultValue)).thenReturn("" + returnValue);
    }

    void verifyPreferences(int keyIndex, int defaultValue) {
        verify(context).getString(keyIndex);
        verify(sharedPreferences).getString(keyValue, "" + defaultValue);
    }

}