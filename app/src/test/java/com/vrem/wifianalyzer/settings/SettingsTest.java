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
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.graph.tools.GraphLegend;
import com.vrem.wifianalyzer.wifi.model.GroupBy;
import com.vrem.wifianalyzer.wifi.model.SortBy;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SettingsTest {
    @Mock
    private Repository repository;
    @Mock
    private Context context;
    @Mock
    private Resources resources;
    @Mock
    private Configuration configuration;
    @Mock
    private OnSharedPreferenceChangeListener onSharedPreferenceChangeListener;

    private Settings fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new Settings(context);
        fixture.setRepository(repository);
    }

    @Test
    public void testInitializeDefaultValues() throws Exception {
        fixture.initializeDefaultValues();
        verify(repository).initializeDefaultValues();
    }

    @Test
    public void testRegisterOnSharedPreferenceChangeListener() throws Exception {
        // execute
        fixture.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        // validate
        verify(repository).registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Test
    public void testScanInterval() throws Exception {
        // setup
        int defaultValue = 10;
        int expected = 11;
        when(repository.getResourceInteger(R.integer.scan_interval_default)).thenReturn(defaultValue);
        when(repository.getInteger(R.string.scan_interval_key, defaultValue)).thenReturn(expected);
        // execute
        int actual = fixture.getScanInterval();
        // validate
        assertEquals(expected, actual);
        verify(repository).getResourceInteger(R.integer.scan_interval_default);
        verify(repository).getInteger(R.string.scan_interval_key, defaultValue);
    }

    @Test
    public void testGroupBy() throws Exception {
        // setup
        when(repository.getStringAsInteger(R.string.group_by_key, GroupBy.NONE.ordinal())).thenReturn(GroupBy.CHANNEL.ordinal());
        // execute
        GroupBy actual = fixture.getGroupBy();
        // validate
        assertEquals(GroupBy.CHANNEL, actual);
        verify(repository).getStringAsInteger(R.string.group_by_key, GroupBy.NONE.ordinal());
    }

    @Test
    public void testSortBy() throws Exception {
        // setup
        when(repository.getStringAsInteger(R.string.sort_by_key, SortBy.STRENGTH.ordinal())).thenReturn(SortBy.SSID.ordinal());
        // execute
        SortBy actual = fixture.getSortBy();
        // validate
        assertEquals(SortBy.SSID, actual);
        verify(repository).getStringAsInteger(R.string.sort_by_key, SortBy.STRENGTH.ordinal());
    }

    @Test
    public void testThemeStyle() throws Exception {
        // setup
        when(repository.getStringAsInteger(R.string.theme_key, ThemeStyle.DARK.ordinal())).thenReturn(ThemeStyle.LIGHT.ordinal());
        // execute
        ThemeStyle actual = fixture.getThemeStyle();
        // validate
        assertEquals(ThemeStyle.LIGHT, actual);
        verify(repository).getStringAsInteger(R.string.theme_key, ThemeStyle.DARK.ordinal());
    }

    @Test
    public void testChannelGraphLegend() throws Exception {
        // setup
        when(repository.getStringAsInteger(R.string.channel_graph_legend_key, GraphLegend.HIDE.ordinal())).thenReturn(GraphLegend.RIGHT.ordinal());
        // execute
        GraphLegend actual = fixture.getChannelGraphLegend();
        // validate
        assertEquals(GraphLegend.RIGHT, actual);
        verify(repository).getStringAsInteger(R.string.channel_graph_legend_key, GraphLegend.HIDE.ordinal());
    }

    @Test
    public void testTimeGraphLegend() throws Exception {
        // setup
        when(repository.getStringAsInteger(R.string.time_graph_legend_key, GraphLegend.LEFT.ordinal())).thenReturn(GraphLegend.RIGHT.ordinal());
        // execute
        GraphLegend actual = fixture.getTimeGraphLegend();
        // validate
        assertEquals(GraphLegend.RIGHT, actual);
        verify(repository).getStringAsInteger(R.string.time_graph_legend_key, GraphLegend.LEFT.ordinal());
    }

    @Test
    public void testWiFiBand() throws Exception {
        // setup
        when(repository.getStringAsInteger(R.string.wifi_band_key, WiFiBand.GHZ2.ordinal())).thenReturn(WiFiBand.GHZ5.ordinal());
        // execute
        WiFiBand actual = fixture.getWiFiBand();
        // validate
        assertEquals(WiFiBand.GHZ5, actual);
        verify(repository).getStringAsInteger(R.string.wifi_band_key, WiFiBand.GHZ2.ordinal());
    }

    @Test
    public void testToggleWiFiBand() throws Exception {
        // setup
        when(repository.getStringAsInteger(R.string.wifi_band_key, WiFiBand.GHZ2.ordinal())).thenReturn(WiFiBand.GHZ5.ordinal());
        // execute
        fixture.toggleWiFiBand();
        // validate
        verify(repository).getStringAsInteger(R.string.wifi_band_key, WiFiBand.GHZ2.ordinal());
        verify(repository).save(R.string.wifi_band_key, WiFiBand.GHZ5.toggle().ordinal());
    }

    @Test
    public void testGetCountryCode() throws Exception {
        // setup
        when(context.getResources()).thenReturn(resources);
        when(resources.getConfiguration()).thenReturn(configuration);
        configuration.locale = Locale.UK;

        when(repository.getString(R.string.country_code_key, Locale.UK.getCountry())).thenReturn(Locale.US.getCountry());
        // execute
        String actual = fixture.getCountryCode();
        // validate
        assertEquals(Locale.US.getCountry(), actual);

        verify(repository).getString(R.string.country_code_key, Locale.UK.getCountry());
        verify(context).getResources();
        verify(resources).getConfiguration();
    }
}