package com.vrem.wifianalyzer.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.model.GroupBy;
import com.vrem.wifianalyzer.wifi.model.SortBy;
import com.vrem.wifianalyzer.wifi.model.WiFiBand;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
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

    private Settings fixture;

    @Before
    public void setUp() throws Exception {
        mockStatic(PreferenceManager.class);

        MainContext.INSTANCE.setContext(context);

        fixture = new Settings();

        when(PreferenceManager.getDefaultSharedPreferences(context)).thenReturn(sharedPreferences);
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
        String key = "xyz";
        int expected = 11;
        withResourceKey(R.string.scan_interval_key, key);
        when(resources.getInteger(R.integer.scan_interval_default)).thenReturn(defaultValue);
        when(sharedPreferences.getInt(key, defaultValue)).thenReturn(expected);
        // execute
        int actual = fixture.getScanInterval();
        // validate
        assertEquals(expected, actual);
        verifyResourceKey(R.string.scan_interval_key);
        verify(resources).getInteger(R.integer.scan_interval_default);
        verify(sharedPreferences).getInt(key, defaultValue);
    }

    private void withResourceKey(int key, String returnValue) {
        when(context.getResources()).thenReturn(resources);
        when(context.getString(key)).thenReturn(returnValue);
    }

    private void verifyResourceKey(int key) {
        verify(context).getResources();
        verify(context).getString(key);
    }

    @Test
    public void testGroupBy() throws Exception {
        // setup
        String defaultValue = "some";
        String key = "xyz";
        GroupBy expected = GroupBy.CHANNEL;
        withResourceKey(R.string.group_by_key, key);
        when(resources.getString(R.string.group_by_default)).thenReturn(defaultValue);
        when(sharedPreferences.getString(key, defaultValue)).thenReturn(expected.name());
        // execute
        GroupBy actual = fixture.getGroupBy();
        // validate
        assertEquals(expected, actual);
        verifyResourceKey(R.string.group_by_key);
        verify(resources).getString(R.string.group_by_default);
        verify(sharedPreferences).getString(key, defaultValue);
    }

    @Test
    public void testSortBy() throws Exception {
        // setup
        String defaultValue = "some";
        String key = "xyz";
        SortBy expected = SortBy.STRENGTH;
        withResourceKey(R.string.sort_by_key, key);
        when(resources.getString(R.string.sort_by_default)).thenReturn(defaultValue);
        when(sharedPreferences.getString(key, defaultValue)).thenReturn(expected.name());
        // execute
        SortBy actual = fixture.getSortBy();
        // validate
        assertEquals(expected, actual);
        verifyResourceKey(R.string.sort_by_key);
        verify(resources).getString(R.string.sort_by_default);
        verify(sharedPreferences).getString(key, defaultValue);
    }

    @Test
    public void testWiFiBand() throws Exception {
        // setup
        String defaultValue = "some";
        String key = "xyz";
        WiFiBand expected = WiFiBand.TWO;
        withResourceKey(R.string.wifi_band_key, key);
        when(resources.getString(R.string.wifi_band_default)).thenReturn(defaultValue);
        when(sharedPreferences.getString(key, defaultValue)).thenReturn(expected.name());
        // execute
        WiFiBand actual = fixture.getWiFiBand();
        // validate
        assertEquals(expected, actual);
        verifyResourceKey(R.string.wifi_band_key);
        verify(resources).getString(R.string.wifi_band_default);
        verify(sharedPreferences).getString(key, defaultValue);
    }

    @Test
    public void testThemeStyle() throws Exception {
        // setup
        String defaultValue = "some";
        String key = "xyz";
        ThemeStyle expected = ThemeStyle.DARK;
        withResourceKey(R.string.theme_key, key);
        when(resources.getString(R.string.theme_default)).thenReturn(defaultValue);
        when(sharedPreferences.getString(key, defaultValue)).thenReturn(expected.name());
        // execute
        ThemeStyle actual = fixture.getThemeStyle();
        // validate
        assertEquals(expected, actual);
        verifyResourceKey(R.string.theme_key);
        verify(resources).getString(R.string.theme_default);
        verify(sharedPreferences).getString(key, defaultValue);
    }
}