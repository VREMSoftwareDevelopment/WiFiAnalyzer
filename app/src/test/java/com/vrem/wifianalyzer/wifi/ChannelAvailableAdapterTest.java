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

package com.vrem.wifianalyzer.wifi;

import android.view.View;
import android.widget.TextView;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannelCountry;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class ChannelAvailableAdapterTest {
    private ChannelAvailableAdapter fixture;
    private WiFiChannelCountry wiFiChannelCountry;

    @Before
    public void setUp() throws Exception {
        MainActivity mainActivity = RobolectricUtil.INSTANCE.getMainActivity();

        wiFiChannelCountry = new WiFiChannelCountry("WFCC");
        fixture = new ChannelAvailableAdapter(mainActivity, Arrays.asList(wiFiChannelCountry));
    }

    @Test
    public void testGetView() throws Exception {
        // setup
        String expected = wiFiChannelCountry.getCountryCode() + " - " + wiFiChannelCountry.getCountryName();
        String expected_GHZ_2 = StringUtils.join(wiFiChannelCountry.getChannelsGHZ2().toArray(), ",");
        String expected_GHZ_5 = StringUtils.join(wiFiChannelCountry.getChannelsGHZ5().toArray(), ",");
        // execute
        View actual = fixture.getView(0, null, null);
        // validate
        assertNotNull(actual);

        assertEquals(expected, ((TextView) actual.findViewById(R.id.channel_available_country)).getText());
        assertEquals(WiFiBand.GHZ2.getBand() + " : ", ((TextView) actual.findViewById(R.id.channel_available_title_ghz_2)).getText());
        assertEquals(expected_GHZ_2, ((TextView) actual.findViewById(R.id.channel_available_ghz_2)).getText());
        assertEquals(WiFiBand.GHZ5.getBand() + " : ", ((TextView) actual.findViewById(R.id.channel_available_title_ghz_5)).getText());
        assertEquals(expected_GHZ_5, ((TextView) actual.findViewById(R.id.channel_available_ghz_5)).getText());
    }


}