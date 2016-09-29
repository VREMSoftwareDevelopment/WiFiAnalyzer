/*
 * WiFi Analyzer
 * Copyright (C) 2016  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AccessPointsDetailTest {
    private MainActivity mainActivity;

    private View view;
    private AccessPointsDetail fixture;

    @Before
    public void setUp() {
        mainActivity = RobolectricUtil.INSTANCE.getMainActivity();
        view = MainContext.INSTANCE.getLayoutInflater().inflate(R.layout.access_points_details, null);
        assertNotNull(view);
        fixture = new AccessPointsDetail();
    }

    @Test
    public void testSetViewWithWiFiDetailAsConnection() throws Exception {
        // setup
        WiFiDetail wiFiDetail = new WiFiDetail("SSID", "BSSID", "capabilities",
            new WiFiSignal(1, 1, WiFiWidth.MHZ_20, 2),
            new WiFiAdditional("VendorName", "IPAddress", 22));
        AccessPointsDetailOptions accessPointsDetailOptions = new AccessPointsDetailOptions(false, false);
        // execute
        fixture.setView(mainActivity.getResources(), view, wiFiDetail, accessPointsDetailOptions);
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
            new WiFiSignal(1, 1, WiFiWidth.MHZ_40, 2),
            new WiFiAdditional(StringUtils.EMPTY, false));
        AccessPointsDetailOptions accessPointsDetailOptions = new AccessPointsDetailOptions(true, true);
        // execute
        fixture.setView(mainActivity.getResources(), view, wiFiDetail, accessPointsDetailOptions);
        // validate
        validateTextViewValues(wiFiDetail, "***");
        WiFiSignal wiFiSignal = wiFiDetail.getWiFiSignal();
        validateTextViewValue(wiFiSignal.getFrequencyStart() + " - " + wiFiSignal.getFrequencyEnd() + " " + WifiInfo.FREQUENCY_UNITS,
            R.id.channel_frequency_range);

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
        validateTextViewValue(wiFiSignal.getLevel() + "dBm", R.id.level);
        validateTextViewValue("" + wiFiSignal.getPrimaryWiFiChannel().getChannel(), R.id.channel);
        validateTextViewValue(wiFiSignal.getPrimaryFrequency() + WifiInfo.FREQUENCY_UNITS, R.id.primaryFrequency);
        validateTextViewValue("(" + wiFiSignal.getWiFiWidth().getFrequencyWidth() + WifiInfo.FREQUENCY_UNITS + ")", R.id.width);
        validateTextViewValue(String.format("%.1fm", wiFiSignal.getDistance()), R.id.distance);
        validateTextViewValue(wiFiDetail.getCapabilities(), R.id.capabilities);
    }

    private void validateTextViewValue(@NonNull String expected, int id) {
        assertEquals(expected, ((TextView) view.findViewById(id)).getText().toString());
    }
}