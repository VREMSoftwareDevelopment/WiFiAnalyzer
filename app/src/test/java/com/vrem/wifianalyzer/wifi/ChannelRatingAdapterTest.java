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
import android.widget.RatingBar;
import android.widget.TextView;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.model.ChannelRating;
import com.vrem.wifianalyzer.wifi.model.SortBy;
import com.vrem.wifianalyzer.wifi.model.Strength;
import com.vrem.wifianalyzer.wifi.model.WiFiConnection;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class ChannelRatingAdapterTest {

    private ChannelRatingAdapter fixture;
    private TextView bestChannels;
    private Scanner scanner;
    private Settings settings;
    private ChannelRating channelRating;

    @Before
    public void setUp() throws Exception {
        MainActivity mainActivity = RobolectricUtil.INSTANCE.getMainActivity();

        channelRating = mock(ChannelRating.class);
        bestChannels = mock(TextView.class);

        scanner = MainContextHelper.INSTANCE.getScanner();
        settings = MainContextHelper.INSTANCE.getSettings();

        fixture = new ChannelRatingAdapter(mainActivity, bestChannels);
        fixture.setChannelRating(channelRating);
    }

    @After
    public void tearDown() throws Exception {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testChannelRatingAdapter() throws Exception {
        verify(scanner).addUpdateNotifier(fixture);
    }

    @Test
    public void testGetView() throws Exception {
        // setup
        int expectedSize = Strength.values().length;
        Strength expectedStrength = Strength.reverse(Strength.FOUR);
        WiFiChannel wiFiChannel = new WiFiChannel(1, 2);
        fixture.add(wiFiChannel);
        when(channelRating.getCount(wiFiChannel)).thenReturn(5);
        when(channelRating.getStrength(wiFiChannel)).thenReturn(Strength.FOUR);
        // execute
        View actual = fixture.getView(0, null, null);
        // validate
        assertNotNull(actual);

        assertEquals("1", ((TextView) actual.findViewById(R.id.channelNumber)).getText());
        assertEquals("5", ((TextView) actual.findViewById(R.id.accessPointCount)).getText());

        RatingBar ratingBar = (RatingBar) actual.findViewById(R.id.channelRating);
        assertEquals(expectedSize, ratingBar.getMax());
        assertEquals(expectedSize, ratingBar.getNumStars());
        assertEquals(expectedStrength.ordinal() + 1, (int) ratingBar.getRating());

        verify(channelRating).getCount(wiFiChannel);
        verify(channelRating).getStrength(wiFiChannel);
    }

    @Test
    public void testUpdate() throws Exception {
        // setup
        WiFiData wiFiData = new WiFiData(new ArrayList<WiFiDetail>(), WiFiConnection.EMPTY, new ArrayList<String>());
        List<WiFiDetail> wiFiDetails = wiFiData.getWiFiDetails(WiFiBand.GHZ5, SortBy.STRENGTH);
        when(settings.getWiFiBand()).thenReturn(WiFiBand.GHZ5);
        when(settings.getCountryCode()).thenReturn(Locale.US.getCountry());
        // execute
        fixture.update(wiFiData);
        // validate
        verify(channelRating).setWiFiDetails(wiFiDetails);
        verify(settings).getWiFiBand();
        verify(settings).getCountryCode();
    }

    @Test
    public void testBestChannelsGHZ2() throws Exception {
        // setup
        List<WiFiChannel> wiFiChannels = new ArrayList<>();
        List<ChannelRating.ChannelAPCount> channelAPCounts = new ArrayList<>();
        when(channelRating.getBestChannels(wiFiChannels)).thenReturn(channelAPCounts);
        // execute
        fixture.bestChannels(WiFiBand.GHZ2, wiFiChannels);
        // validate
        verify(channelRating).getBestChannels(wiFiChannels);
    }

    @Test
    public void testBestChannelsGHZ5() throws Exception {
        // setup
        List<WiFiChannel> wiFiChannels = new ArrayList<>();
        List<ChannelRating.ChannelAPCount> channelAPCounts = new ArrayList<>();
        when(channelRating.getBestChannels(wiFiChannels)).thenReturn(channelAPCounts);
        // execute
        fixture.bestChannels(WiFiBand.GHZ5, wiFiChannels);
        // validate
        verify(channelRating).getBestChannels(wiFiChannels);
    }

}