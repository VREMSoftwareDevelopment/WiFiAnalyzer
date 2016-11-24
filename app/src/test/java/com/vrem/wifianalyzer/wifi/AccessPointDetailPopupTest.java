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

import android.app.Dialog;
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
public class AccessPointDetailPopupTest {
    private static final String VENDOR_NAME = "VendorName-VendorName-VendorName-VendorName-VendorName";
    private MainActivity mainActivity;

    private AccessPointDetail fixture;

    @Before
    public void setUp() {
        mainActivity = RobolectricUtil.INSTANCE.getMainActivity();
        fixture = new AccessPointDetail();
    }

    private WiFiDetail withWiFiDetail(WiFiAdditional wiFiAdditional) {
        return new WiFiDetail("SSID", "BSSID", "capabilities", new WiFiSignal(1, 1, WiFiWidth.MHZ_40, 2), wiFiAdditional);
    }

    @Test
    public void testPopupDialog() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(new WiFiAdditional(StringUtils.EMPTY, false));
        // execute
        Dialog dialog = fixture.popupDialog(mainActivity, MainContext.INSTANCE.getLayoutInflater(), wiFiDetail);
        // validate
        assertNotNull(dialog);
    }

    @Test
    public void testSetViewPopup() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(new WiFiAdditional(StringUtils.EMPTY, false));
        // execute
        Dialog dialog = fixture.popupDialog(mainActivity, MainContext.INSTANCE.getLayoutInflater(), wiFiDetail);
        // validate
        validateTextViewValues(dialog, wiFiDetail);
    }

    @Test
    public void testSetViewFullWithVendorShortNotVisible() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(new WiFiAdditional(StringUtils.EMPTY, false));
        // execute
        Dialog dialog = fixture.popupDialog(mainActivity, MainContext.INSTANCE.getLayoutInflater(), wiFiDetail);
        // validate
        assertEquals(View.GONE, dialog.findViewById(R.id.vendorLong).getVisibility());
    }

    @Test
    public void testSetViewFullWithVendorShortVisible() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(new WiFiAdditional(VENDOR_NAME, false));
        // execute
        Dialog dialog = fixture.popupDialog(mainActivity, MainContext.INSTANCE.getLayoutInflater(), wiFiDetail);
        // validate
        assertEquals(View.VISIBLE, dialog.findViewById(R.id.vendorLong).getVisibility());
    }

    @Test
    public void testSetViewFullWithVendorShortMaximumSize() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(new WiFiAdditional(VENDOR_NAME, false));
        // execute
        Dialog dialog = fixture.popupDialog(mainActivity, MainContext.INSTANCE.getLayoutInflater(), wiFiDetail);
        // validate
        validateTextViewValue(dialog, VENDOR_NAME.substring(0, 30), R.id.vendorLong);
    }

    private void validateTextViewValues(@NonNull Dialog dialog, @NonNull WiFiDetail wiFiDetail) {
        WiFiSignal wiFiSignal = wiFiDetail.getWiFiSignal();
        validateTextViewValue(dialog, wiFiDetail.getSSID() + " (" + wiFiDetail.getBSSID() + ")", R.id.ssid);
        validateTextViewValue(dialog, wiFiSignal.getLevel() + "dBm", R.id.level);
        validateTextViewValue(dialog, wiFiSignal.getChannelDisplay(), R.id.channel);
        validateTextViewValue(dialog, wiFiSignal.getPrimaryFrequency() + WifiInfo.FREQUENCY_UNITS, R.id.primaryFrequency);
        validateTextViewValue(dialog, wiFiSignal.getFrequencyStart() + " - " + wiFiSignal.getFrequencyEnd(), R.id.channel_frequency_range);
        validateTextViewValue(dialog, "(" + wiFiSignal.getWiFiWidth().getFrequencyWidth() + WifiInfo.FREQUENCY_UNITS + ")", R.id.width);
        validateTextViewValue(dialog, String.format("%.1fm", wiFiSignal.getDistance()), R.id.distance);
        validateTextViewValue(dialog, wiFiDetail.getCapabilities(), R.id.capabilities);
    }

    private void validateTextViewValue(@NonNull Dialog dialog, @NonNull String expected, int id) {
        assertEquals(expected, ((TextView) dialog.findViewById(id)).getText().toString());
    }

}