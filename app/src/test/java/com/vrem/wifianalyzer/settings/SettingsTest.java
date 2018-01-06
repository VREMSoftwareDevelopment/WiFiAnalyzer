/*
 * WiFiAnalyzer
 * Copyright (C) 2018  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.settings;

import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import com.vrem.util.EnumUtils;
import com.vrem.util.LocaleUtils;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.navigation.NavigationMenu;
import com.vrem.wifianalyzer.wifi.accesspoint.AccessPointViewType;
import com.vrem.wifianalyzer.wifi.accesspoint.ConnectionViewType;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.graphutils.GraphLegend;
import com.vrem.wifianalyzer.wifi.model.GroupBy;
import com.vrem.wifianalyzer.wifi.model.Security;
import com.vrem.wifianalyzer.wifi.model.SortBy;
import com.vrem.wifianalyzer.wifi.model.Strength;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SettingsTest {
    @Mock
    private Repository repository;
    @Mock
    private OnSharedPreferenceChangeListener onSharedPreferenceChangeListener;

    private Settings fixture;

    @Before
    public void setUp() {
        fixture = new Settings(repository);
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
    public void testGetScanInterval() throws Exception {
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
    public void testGetGroupBy() throws Exception {
        // setup
        when(repository.getStringAsInteger(R.string.group_by_key, GroupBy.NONE.ordinal())).thenReturn(GroupBy.CHANNEL.ordinal());
        // execute
        GroupBy actual = fixture.getGroupBy();
        // validate
        assertEquals(GroupBy.CHANNEL, actual);
        verify(repository).getStringAsInteger(R.string.group_by_key, GroupBy.NONE.ordinal());
    }

    @Test
    public void testGetSortBy() throws Exception {
        // setup
        when(repository.getStringAsInteger(R.string.sort_by_key, SortBy.STRENGTH.ordinal())).thenReturn(SortBy.SSID.ordinal());
        // execute
        SortBy actual = fixture.getSortBy();
        // validate
        assertEquals(SortBy.SSID, actual);
        verify(repository).getStringAsInteger(R.string.sort_by_key, SortBy.STRENGTH.ordinal());
    }

    @Test
    public void testGetAccessPointView() throws Exception {
        // setup
        when(repository.getStringAsInteger(R.string.ap_view_key, AccessPointViewType.COMPLETE.ordinal())).thenReturn(AccessPointViewType.COMPACT.ordinal());
        // execute
        AccessPointViewType actual = fixture.getAccessPointView();
        // validate
        assertEquals(AccessPointViewType.COMPACT, actual);
        verify(repository).getStringAsInteger(R.string.ap_view_key, AccessPointViewType.COMPLETE.ordinal());
    }

    @Test
    public void testGetConnectionViewType() throws Exception {
        // setup
        when(repository.getStringAsInteger(R.string.connection_view_key, ConnectionViewType.COMPLETE.ordinal())).thenReturn(ConnectionViewType.COMPACT.ordinal());
        // execute
        ConnectionViewType actual = fixture.getConnectionViewType();
        // validate
        assertEquals(ConnectionViewType.COMPACT, actual);
        verify(repository).getStringAsInteger(R.string.connection_view_key, ConnectionViewType.COMPLETE.ordinal());
    }

    @Test
    public void testGetThemeStyle() throws Exception {
        // setup
        when(repository.getStringAsInteger(R.string.theme_key, ThemeStyle.DARK.ordinal())).thenReturn(ThemeStyle.LIGHT.ordinal());
        // execute
        ThemeStyle actual = fixture.getThemeStyle();
        // validate
        assertEquals(ThemeStyle.LIGHT, actual);
        verify(repository).getStringAsInteger(R.string.theme_key, ThemeStyle.DARK.ordinal());
    }

    @Test
    public void testGetGraphMaximumY() throws Exception {
        // setup
        int defaultValue = 1;
        int value = 2;
        int expected = value * Settings.GRAPH_Y_MULTIPLIER;
        when(repository.getStringAsInteger(R.string.graph_maximum_y_default, Settings.GRAPH_Y_DEFAULT)).thenReturn(defaultValue);
        when(repository.getStringAsInteger(R.string.graph_maximum_y_key, defaultValue)).thenReturn(value);
        // execute
        int actual = fixture.getGraphMaximumY();
        // validate
        assertEquals(expected, actual);
        verify(repository).getStringAsInteger(R.string.graph_maximum_y_default, Settings.GRAPH_Y_DEFAULT);
        verify(repository).getStringAsInteger(R.string.graph_maximum_y_key, defaultValue);
    }

    @Test
    public void testGetChannelGraphLegend() throws Exception {
        // setup
        when(repository.getStringAsInteger(R.string.channel_graph_legend_key, GraphLegend.HIDE.ordinal())).thenReturn(GraphLegend.RIGHT.ordinal());
        // execute
        GraphLegend actual = fixture.getChannelGraphLegend();
        // validate
        assertEquals(GraphLegend.RIGHT, actual);
        verify(repository).getStringAsInteger(R.string.channel_graph_legend_key, GraphLegend.HIDE.ordinal());
    }

    @Test
    public void testGetTimeGraphLegend() throws Exception {
        // setup
        when(repository.getStringAsInteger(R.string.time_graph_legend_key, GraphLegend.LEFT.ordinal())).thenReturn(GraphLegend.RIGHT.ordinal());
        // execute
        GraphLegend actual = fixture.getTimeGraphLegend();
        // validate
        assertEquals(GraphLegend.RIGHT, actual);
        verify(repository).getStringAsInteger(R.string.time_graph_legend_key, GraphLegend.LEFT.ordinal());
    }

    @Test
    public void testGetWiFiBand() throws Exception {
        // setup
        when(repository.getStringAsInteger(R.string.wifi_band_key, WiFiBand.GHZ2.ordinal())).thenReturn(WiFiBand.GHZ5.ordinal());
        // execute
        WiFiBand actual = fixture.getWiFiBand();
        // validate
        assertEquals(WiFiBand.GHZ5, actual);
        verify(repository).getStringAsInteger(R.string.wifi_band_key, WiFiBand.GHZ2.ordinal());
    }

    @Test
    public void testGetSSIDFilter() throws Exception {
        // setup
        Set<String> expected = new HashSet<>(Arrays.asList("value1", "value2", "value3"));
        when(repository.getStringSet(R.string.filter_ssid_key, Collections.<String>emptySet())).thenReturn(expected);
        // execute
        Set<String> actual = fixture.getSSIDs();
        // validate
        assertEquals(expected, actual);
        verify(repository).getStringSet(R.string.filter_ssid_key, Collections.<String>emptySet());
    }

    @Test
    public void testSaveSSIDFilter() throws Exception {
        // setup
        Set<String> values = new HashSet<>(Arrays.asList("value1", "value2", "value3"));
        // execute
        fixture.saveSSIDs(values);
        // validate
        verify(repository).saveStringSet(R.string.filter_ssid_key, values);
    }

    @Test
    public void testGetWiFiBandFilter() throws Exception {
        // setup
        WiFiBand expected = WiFiBand.GHZ5;
        Set<String> values = Collections.singleton("" + expected.ordinal());
        Set<String> defaultValues = EnumUtils.ordinals(WiFiBand.class);
        when(repository.getStringSet(R.string.filter_wifi_band_key, defaultValues)).thenReturn(values);
        // execute
        Set<WiFiBand> actual = fixture.getWiFiBands();
        // validate
        assertEquals(1, actual.size());
        assertTrue(actual.contains(expected));
        verify(repository).getStringSet(R.string.filter_wifi_band_key, defaultValues);
    }

    @Test
    public void testSaveWiFiBandFilter() throws Exception {
        // setup
        Set<WiFiBand> values = Collections.singleton(WiFiBand.GHZ5);
        Set<String> expected = Collections.singleton("" + WiFiBand.GHZ5.ordinal());
        // execute
        fixture.saveWiFiBands(values);
        // validate
        verify(repository).saveStringSet(R.string.filter_wifi_band_key, expected);
    }

    @Test
    public void testGetStrengthFilter() throws Exception {
        // setup
        Strength expected = Strength.THREE;
        Set<String> values = Collections.singleton("" + expected.ordinal());
        Set<String> defaultValues = EnumUtils.ordinals(Strength.class);
        when(repository.getStringSet(R.string.filter_strength_key, defaultValues)).thenReturn(values);
        // execute
        Set<Strength> actual = fixture.getStrengths();
        // validate
        assertEquals(1, actual.size());
        assertTrue(actual.contains(expected));
        verify(repository).getStringSet(R.string.filter_strength_key, defaultValues);
    }

    @Test
    public void testSaveStrengthFilter() throws Exception {
        // setup
        Set<Strength> values = Collections.singleton(Strength.TWO);
        Set<String> expected = Collections.singleton("" + Strength.TWO.ordinal());
        // execute
        fixture.saveStrengths(values);
        // validate
        verify(repository).saveStringSet(R.string.filter_strength_key, expected);
    }

    @Test
    public void testGetSecurityFilter() throws Exception {
        // setup
        Security expected = Security.WPA;
        Set<String> values = Collections.singleton("" + expected.ordinal());
        Set<String> defaultValues = EnumUtils.ordinals(Security.class);
        when(repository.getStringSet(R.string.filter_security_key, defaultValues)).thenReturn(values);
        // execute
        Set<Security> actual = fixture.getSecurities();
        // validate
        assertEquals(1, actual.size());
        assertTrue(actual.contains(expected));
        verify(repository).getStringSet(R.string.filter_security_key, defaultValues);
    }

    @Test
    public void testSaveSecurityFilter() throws Exception {
        // setup
        Set<Security> values = Collections.singleton(Security.WEP);
        Set<String> expected = Collections.singleton("" + Security.WEP.ordinal());
        // execute
        fixture.saveSecurities(values);
        // validate
        verify(repository).saveStringSet(R.string.filter_security_key, expected);
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
        String defaultValue = LocaleUtils.getDefaultCountryCode();
        String expected = "WW";
        when(repository.getString(R.string.country_code_key, defaultValue)).thenReturn(expected);
        // execute
        String actual = fixture.getCountryCode();
        // validate
        assertEquals(expected, actual);
        verify(repository).getString(R.string.country_code_key, defaultValue);
    }

    @Test
    public void testGetLanguageLocale() throws Exception {
        // setup
        String defaultValue = LocaleUtils.getDefaultLanguageTag();
        Locale expected = Locale.FRENCH;
        when(repository.getString(R.string.language_key, defaultValue)).thenReturn(LocaleUtils.toLanguageTag(expected));
        // execute
        Locale actual = fixture.getLanguageLocale();
        // validate
        assertEquals(expected, actual);
        verify(repository).getString(R.string.language_key, defaultValue);
    }

    @Test
    public void testGetStartMenu() throws Exception {
        // setup
        when(repository.getStringAsInteger(R.string.start_menu_key, NavigationMenu.ACCESS_POINTS.ordinal())).thenReturn(NavigationMenu.CHANNEL_GRAPH.ordinal());
        // execute
        NavigationMenu actual = fixture.getStartMenu();
        // validate
        assertEquals(NavigationMenu.CHANNEL_GRAPH, actual);
        verify(repository).getStringAsInteger(R.string.start_menu_key, NavigationMenu.ACCESS_POINTS.ordinal());
    }

    @Test
    public void testIsWiFiOffOnExit() throws Exception {
        // setup
        when(repository.getResourceBoolean(R.bool.wifi_off_on_exit_default)).thenReturn(true);
        when(repository.getBoolean(R.string.wifi_off_on_exit_key, true)).thenReturn(true);
        // execute
        boolean actual = fixture.isWiFiOffOnExit();
        // validate
        assertTrue(actual);
        verify(repository).getBoolean(R.string.wifi_off_on_exit_key, true);
    }

    public boolean isWiFiOffOnExit() {
        return repository.getBoolean(R.string.wifi_off_on_exit_key, repository.getResourceBoolean(R.bool.wifi_off_on_exit_default));
    }

}