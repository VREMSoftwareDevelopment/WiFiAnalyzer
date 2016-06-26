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

import android.net.wifi.WifiInfo;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.wifi.band.WiFiWidth;
import com.vrem.wifianalyzer.wifi.model.WiFiAdditional;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class AccessPointsDetailTest {
    private MainActivity mainActivity;

    private View view;
    private AccessPointsDetail fixture;

    @Before
    public void setUp() throws Exception {
        mainActivity = RobolectricUtil.INSTANCE.getMainActivity();
        view = MainContext.INSTANCE.getLayoutInflater().inflate(R.layout.access_points_details, null);
        assertNotNull(view);
        fixture = new AccessPointsDetail();
    }

    @Test
    public void testSetViewWithWiFiDetailAsConnection() throws Exception {
        // setup
        WiFiDetail wiFiDetail = new WiFiDetail("SSID", "BSSID", "capabilities",
                new WiFiSignal(1, WiFiWidth.MHZ_20, 2),
                new WiFiAdditional("VendorName", "IPAddress", 22));
        // execute
        fixture.setView(mainActivity.getResources(), view, wiFiDetail, false, false);
        // validate
        validateTextViewValues(wiFiDetail, "SSID");

        validateTextViewValue(wiFiDetail.getWiFiAdditional().getIPAddress(), R.id.ipAddress);
        assertEquals(View.VISIBLE, view.findViewById(R.id.ipAddress).getVisibility());

        assertEquals(View.VISIBLE, view.findViewById(R.id.configuredImage).getVisibility());

        validateTextViewValue(String.format("%d%s", wiFiDetail.getWiFiAdditional().getLinkSpeed(), WifiInfo.LINK_SPEED_UNITS), R.id.linkSpeed);
        assertEquals(View.VISIBLE, view.findViewById(R.id.linkSpeed).getVisibility());

        validateTextViewValue(wiFiDetail.getWiFiAdditional().getVendorName(), R.id.vendor);
        assertEquals(View.VISIBLE, view.findViewById(R.id.vendor).getVisibility());

        assertEquals(View.GONE, view.findViewById(R.id.tab).getVisibility());
        assertEquals(View.GONE, view.findViewById(R.id.groupIndicator).getVisibility());

        assertEquals(View.GONE, view.findViewById(R.id.channel_frequency_range_row).getVisibility());
    }

    @Test
    public void testSetViewWithWiFiDetailAsScanResult() throws Exception {
        // setup
        WiFiDetail wiFiDetail = new WiFiDetail(StringUtils.EMPTY, "BSSID", "capabilities",
            new WiFiSignal(1, WiFiWidth.MHZ_40, 2),
                new WiFiAdditional(StringUtils.EMPTY, false));
        // execute
        fixture.setView(mainActivity.getResources(), view, wiFiDetail, true, true);
        // validate
        validateTextViewValues(wiFiDetail, "***");
        WiFiSignal wiFiSignal = wiFiDetail.getWiFiSignal();
        validateTextViewValue(String.format("%d - %d %s", wiFiSignal.getFrequencyStart(), wiFiSignal.getFrequencyEnd(), WifiInfo.FREQUENCY_UNITS), R.id.channel_frequency_range);

        assertEquals(View.GONE, view.findViewById(R.id.ipAddress).getVisibility());
        assertEquals(View.GONE, view.findViewById(R.id.configuredImage).getVisibility());
        assertEquals(View.GONE, view.findViewById(R.id.vendor).getVisibility());
        assertEquals(View.VISIBLE, view.findViewById(R.id.tab).getVisibility());
        assertEquals(View.GONE, view.findViewById(R.id.groupIndicator).getVisibility());
        assertEquals(View.VISIBLE, view.findViewById(R.id.channel_frequency_range_row).getVisibility());
    }

    private void validateTextViewValues(@NonNull WiFiDetail wiFiDetail, @NonNull String ssid) {
        WiFiSignal wiFiSignal = wiFiDetail.getWiFiSignal();
        validateTextViewValue(ssid + " (" + wiFiDetail.getBSSID() + ")", R.id.ssid);
        validateTextViewValue(String.format("%ddBm", wiFiSignal.getLevel()), R.id.level);
        validateTextViewValue(String.format("%d", wiFiSignal.getWiFiChannel().getChannel()), R.id.channel);
        validateTextViewValue(String.format("%d%s", wiFiSignal.getFrequency(), WifiInfo.FREQUENCY_UNITS), R.id.frequency);
        validateTextViewValue(String.format("(%d%s)", wiFiSignal.getWiFiWidth().getFrequencyWidth(), WifiInfo.FREQUENCY_UNITS), R.id.width);
        validateTextViewValue(String.format("%.1fm", wiFiSignal.getDistance()), R.id.distance);
        validateTextViewValue(wiFiDetail.getCapabilities(), R.id.capabilities);
    }

    private void validateTextViewValue(@NonNull String expected, int id) {
        assertEquals(expected, ((TextView) view.findViewById(id)).getText().toString());
    }
}