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
import android.view.LayoutInflater;
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

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AccessPointDetailTest {
    private static final String SSID = "SSID";
    private static final String VENDOR_NAME = "VendorName-VendorName-VendorName-VendorName-VendorName-VendorName";

    private MainActivity mainActivity;
    private LayoutInflater layoutInflater;

    private AccessPointDetail fixture;

    @Before
    public void setUp() {
        mainActivity = RobolectricUtil.INSTANCE.getMainActivity();
        layoutInflater = MainContext.INSTANCE.getLayoutInflater();

        fixture = new AccessPointDetail();
    }

    @Test
    public void testSetViewFullWithConfiguredImageVisible() throws Exception {
        // setup
        WiFiAdditional wiFiAdditional = new WiFiAdditional(StringUtils.EMPTY, true);
        WiFiDetail wiFiDetail = withWiFiDetail(SSID, wiFiAdditional);
        View view = layoutInflater.inflate(R.layout.access_point_view_full, null);
        // execute
        fixture.setView(mainActivity.getResources(), view, wiFiDetail, false);
        // validate
        assertEquals(View.VISIBLE, view.findViewById(R.id.configuredImage).getVisibility());
    }

    @Test
    public void testSetViewFullWithTabGone() throws Exception {
        // setup
        WiFiAdditional wiFiAdditional = new WiFiAdditional(StringUtils.EMPTY, false);
        WiFiDetail wiFiDetail = withWiFiDetail(SSID, wiFiAdditional);
        View view = layoutInflater.inflate(R.layout.access_point_view_full, null);
        // execute
        fixture.setView(mainActivity.getResources(), view, wiFiDetail, false);
        // validate
        assertEquals(View.GONE, view.findViewById(R.id.tab).getVisibility());
    }

    @Test
    public void testSetViewFullWithGroupIndicatorGone() throws Exception {
        // setup
        WiFiAdditional wiFiAdditional = new WiFiAdditional(StringUtils.EMPTY, false);
        WiFiDetail wiFiDetail = withWiFiDetail(SSID, wiFiAdditional);
        View view = layoutInflater.inflate(R.layout.access_point_view_full, null);
        // execute
        fixture.setView(mainActivity.getResources(), view, wiFiDetail, false);
        // validate
        assertEquals(View.GONE, view.findViewById(R.id.groupIndicator).getVisibility());

    }

    @Test
    public void testSetViewFullWithVendorShortNotVisible() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(SSID, new WiFiAdditional(StringUtils.EMPTY, false));
        View view = layoutInflater.inflate(R.layout.access_point_view_full, null);
        // execute
        fixture.setView(mainActivity.getResources(), view, wiFiDetail, false);
        // validate
        assertEquals(View.GONE, view.findViewById(R.id.vendorShort).getVisibility());
    }

    @Test
    public void testSetViewFullWithVendorShortVisible() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(SSID, new WiFiAdditional(VENDOR_NAME, false));
        View view = layoutInflater.inflate(R.layout.access_point_view_full, null);
        // execute
        fixture.setView(mainActivity.getResources(), view, wiFiDetail, false);
        // validate
        assertEquals(View.VISIBLE, view.findViewById(R.id.vendorShort).getVisibility());
    }

    @Test
    public void testSetViewFullWithVendorShortMaximumSize() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(SSID, new WiFiAdditional(VENDOR_NAME, false));
        View view = layoutInflater.inflate(R.layout.access_point_view_full, null);
        // execute
        fixture.setView(mainActivity.getResources(), view, wiFiDetail, false);
        // validate
        validateTextViewValue(view, VENDOR_NAME.substring(0, 12), R.id.vendorShort);
    }

    @Test
    public void testSetViewFullWithTabVisible() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(StringUtils.EMPTY, new WiFiAdditional(StringUtils.EMPTY, false));
        View view = layoutInflater.inflate(R.layout.access_point_view_full, null);
        // execute
        fixture.setView(mainActivity.getResources(), view, wiFiDetail, true);
        // validate
        assertEquals(View.VISIBLE, view.findViewById(R.id.tab).getVisibility());
    }

    @Test
    public void testSetViewFullWithWiFiDetailAndEmptySSID() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(StringUtils.EMPTY, new WiFiAdditional(StringUtils.EMPTY, false));
        View view = layoutInflater.inflate(R.layout.access_point_view_full, null);
        // execute
        fixture.setView(mainActivity.getResources(), view, wiFiDetail, false);
        // validate
        validateTextViewValuesFullView(view, wiFiDetail, "***");
    }

    @Test
    public void testSetViewFullWithWiFiDetail() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(SSID, new WiFiAdditional(StringUtils.EMPTY, false));
        View view = layoutInflater.inflate(R.layout.access_point_view_full, null);
        // execute
        fixture.setView(mainActivity.getResources(), view, wiFiDetail, false);
        // validate
        validateTextViewValuesFullView(view, wiFiDetail, SSID);
    }

    @Test
    public void testSetViewCompactWithTabGone() throws Exception {
        // setup
        WiFiAdditional wiFiAdditional = new WiFiAdditional(StringUtils.EMPTY, false);
        WiFiDetail wiFiDetail = withWiFiDetail(SSID, wiFiAdditional);
        View view = layoutInflater.inflate(R.layout.access_point_view_compact, null);
        // execute
        fixture.setView(mainActivity.getResources(), view, wiFiDetail, false);
        // validate
        assertEquals(View.GONE, view.findViewById(R.id.tab).getVisibility());
    }

    @Test
    public void testSetViewCompactWithGroupIndicatorGone() throws Exception {
        // setup
        WiFiAdditional wiFiAdditional = new WiFiAdditional(StringUtils.EMPTY, false);
        WiFiDetail wiFiDetail = withWiFiDetail("SSID", wiFiAdditional);
        View view = layoutInflater.inflate(R.layout.access_point_view_compact, null);
        // execute
        fixture.setView(mainActivity.getResources(), view, wiFiDetail, false);
        // validate
        assertEquals(View.GONE, view.findViewById(R.id.groupIndicator).getVisibility());
    }

    @Test
    public void testSetViewCompactWithTabVisible() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(StringUtils.EMPTY, new WiFiAdditional(StringUtils.EMPTY, false));
        View view = layoutInflater.inflate(R.layout.access_point_view_compact, null);
        // execute
        fixture.setView(mainActivity.getResources(), view, wiFiDetail, true);
        // validate
        assertEquals(View.VISIBLE, view.findViewById(R.id.tab).getVisibility());
    }

    @Test
    public void testSetViewCompactWithWiFiDetailAndEmptySSID() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(StringUtils.EMPTY, new WiFiAdditional(StringUtils.EMPTY, false));
        View view = layoutInflater.inflate(R.layout.access_point_view_compact, null);
        // execute
        fixture.setView(mainActivity.getResources(), view, wiFiDetail, false);
        // validate
        validateTextViewValuesCompactView(view, wiFiDetail, "***");
    }

    @Test
    public void testSetViewCompactWithWiFiDetail() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(SSID, new WiFiAdditional(StringUtils.EMPTY, false));
        View view = layoutInflater.inflate(R.layout.access_point_view_compact, null);
        // execute
        fixture.setView(mainActivity.getResources(), view, wiFiDetail, false);
        // validate
        validateTextViewValuesCompactView(view, wiFiDetail, SSID);
    }

    private WiFiDetail withWiFiDetail(String SSID, WiFiAdditional wiFiAdditional) {
        return new WiFiDetail(SSID, "BSSID", "capabilities", new WiFiSignal(1, 1, WiFiWidth.MHZ_40, 2), wiFiAdditional);
    }

    private void validateTextViewValuesFullView(@NonNull View view, @NonNull WiFiDetail wiFiDetail, @NonNull String ssid) {
        validateTextViewValuesCompactView(view, wiFiDetail, ssid);

        WiFiSignal wiFiSignal = wiFiDetail.getWiFiSignal();
        validateTextViewValue(view, wiFiSignal.getFrequencyStart() + " - " + wiFiSignal.getFrequencyEnd(), R.id.channel_frequency_range);
        validateTextViewValue(view, "(" + wiFiSignal.getWiFiWidth().getFrequencyWidth() + WifiInfo.FREQUENCY_UNITS + ")", R.id.width);
        validateTextViewValue(view, wiFiDetail.getCapabilities(), R.id.capabilities);
    }

    private void validateTextViewValuesCompactView(@NonNull View view, @NonNull WiFiDetail wiFiDetail, @NonNull String ssid) {
        WiFiSignal wiFiSignal = wiFiDetail.getWiFiSignal();
        validateTextViewValue(view, ssid + " (" + wiFiDetail.getBSSID() + ")", R.id.ssid);
        validateTextViewValue(view, wiFiSignal.getLevel() + "dBm", R.id.level);
        validateTextViewValue(view, wiFiSignal.getChannelDisplay(), R.id.channel);
        validateTextViewValue(view, wiFiSignal.getPrimaryFrequency() + WifiInfo.FREQUENCY_UNITS, R.id.primaryFrequency);
        validateTextViewValue(view, String.format("%.1fm", wiFiSignal.getDistance()), R.id.distance);
    }

    private void validateTextViewValue(@NonNull View view, @NonNull String expected, int id) {
        assertEquals(expected, ((TextView) view.findViewById(id)).getText().toString());
    }
}