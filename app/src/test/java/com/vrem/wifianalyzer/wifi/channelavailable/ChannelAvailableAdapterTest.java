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

package com.vrem.wifianalyzer.wifi.channelavailable;

import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannelCountry;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import java.util.Collections;
import java.util.Locale;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.annotation.LooperMode.Mode.PAUSED;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
@LooperMode(PAUSED)
public class ChannelAvailableAdapterTest {
    private Settings settings;
    private MainActivity mainActivity;
    private WiFiChannelCountry wiFiChannelCountry;
    private Locale currentLocale;
    private ChannelAvailableAdapter fixture;

    @Before
    public void setUp() {
        currentLocale = Locale.getDefault();

        mainActivity = RobolectricUtil.INSTANCE.getActivity();

        settings = MainContextHelper.INSTANCE.getSettings();
        when(settings.getLanguageLocale()).thenReturn(currentLocale);

        wiFiChannelCountry = WiFiChannelCountry.get(currentLocale.getCountry());
        fixture = new ChannelAvailableAdapter(mainActivity, Collections.singletonList(wiFiChannelCountry));
    }

    @After
    public void tearDown() {
        verify(settings, atLeastOnce()).getLanguageLocale();
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testGetView() {
        // setup
        Resources resources = mainActivity.getResources();
        String wiFiBand2 = resources.getString(WiFiBand.GHZ2.getTextResource());
        String wiFiBand5 = resources.getString(WiFiBand.GHZ5.getTextResource());
        String expected = wiFiChannelCountry.getCountryCode() + " - " + wiFiChannelCountry.getCountryName(currentLocale);
        String expected_GHZ_2 = StringUtils.join(wiFiChannelCountry.getChannelsGHZ2().toArray(), ",");
        String expected_GHZ_5 = StringUtils.join(wiFiChannelCountry.getChannelsGHZ5().toArray(), ",");
        ViewGroup viewGroup = mainActivity.findViewById(android.R.id.content);
        // execute
        View actual = fixture.getView(0, null, viewGroup);
        // validate
        assertNotNull(actual);

        assertEquals(expected, actual.<TextView>findViewById(R.id.channel_available_country).getText());
        assertEquals(wiFiBand2 + " : ", actual.<TextView>findViewById(R.id.channel_available_title_ghz_2).getText());
        assertEquals(expected_GHZ_2, actual.<TextView>findViewById(R.id.channel_available_ghz_2).getText());
        assertEquals(wiFiBand5 + " : ", actual.<TextView>findViewById(R.id.channel_available_title_ghz_5).getText());
        assertEquals(expected_GHZ_5, actual.<TextView>findViewById(R.id.channel_available_ghz_5).getText());
    }

}