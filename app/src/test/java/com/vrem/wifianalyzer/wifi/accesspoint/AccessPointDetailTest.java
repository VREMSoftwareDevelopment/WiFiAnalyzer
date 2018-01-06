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

package com.vrem.wifianalyzer.wifi.accesspoint;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.band.WiFiWidth;
import com.vrem.wifianalyzer.wifi.model.WiFiAdditional;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AccessPointDetailTest {
    private static final String SSID = "SSID";
    private static final String VENDOR_NAME = "VendorName-VendorName-VendorName-VendorName-VendorName-VendorName";

    private MainActivity mainActivity;
    private Settings settings;

    private AccessPointDetail fixture;

    @Before
    public void setUp() {
        mainActivity = RobolectricUtil.INSTANCE.getActivity();
        settings = MainContextHelper.INSTANCE.getSettings();
        when(settings.getAccessPointView()).thenReturn(AccessPointViewType.COMPLETE);
        fixture = new AccessPointDetail();
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testMakeViewShouldCreateNewView() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(SSID, new WiFiAdditional(StringUtils.EMPTY, true));
        // execute
        View actual = fixture.makeView(null, null, wiFiDetail, false);
        // validate
        assertNotNull(actual);
    }

    @Test
    public void testMakeViewShouldUseGivenView() throws Exception {
        // setup
        View expected = mainActivity.getLayoutInflater().inflate(AccessPointViewType.COMPLETE.getLayout(), null, false);
        WiFiDetail wiFiDetail = withWiFiDetail(SSID, new WiFiAdditional(StringUtils.EMPTY, true));
        // execute
        View actual = fixture.makeView(expected, null, wiFiDetail, false);
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testMakeViewFullWithConfiguredImageVisible() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(SSID, new WiFiAdditional(StringUtils.EMPTY, true));
        // execute
        View actual = fixture.makeView(null, null, wiFiDetail, false);
        // validate
        assertEquals(View.VISIBLE, actual.findViewById(R.id.configuredImage).getVisibility());
    }

    @Test
    public void testMakeViewFullWithTabGone() throws Exception {
        // setup
        WiFiAdditional wiFiAdditional = new WiFiAdditional(StringUtils.EMPTY, false);
        WiFiDetail wiFiDetail = withWiFiDetail(SSID, wiFiAdditional);
        // execute
        View actual = fixture.makeView(null, null, wiFiDetail, false);
        // validate
        assertEquals(View.GONE, actual.findViewById(R.id.tab).getVisibility());
    }

    @Test
    public void testMakeViewFullWithGroupIndicatorGone() throws Exception {
        // setup
        WiFiAdditional wiFiAdditional = new WiFiAdditional(StringUtils.EMPTY, false);
        WiFiDetail wiFiDetail = withWiFiDetail(SSID, wiFiAdditional);
        // execute
        View actual = fixture.makeView(null, null, wiFiDetail, false);
        // validate
        assertEquals(View.GONE, actual.findViewById(R.id.groupIndicator).getVisibility());
    }

    @Test
    public void testMakeViewFullWithVendorShortNotVisible() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(SSID, new WiFiAdditional(StringUtils.EMPTY, false));
        // execute
        View actual = fixture.makeView(null, null, wiFiDetail, false);
        // validate
        assertNull(actual.findViewById(R.id.vendorLong));
        assertEquals(View.GONE, actual.findViewById(R.id.vendorShort).getVisibility());
    }

    @Test
    public void testMakeViewFullWithVendorShortVisible() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(SSID, new WiFiAdditional(VENDOR_NAME, false));
        // execute
        View actual = fixture.makeView(null, null, wiFiDetail, false);
        // validate
        assertNull(actual.findViewById(R.id.vendorLong));
        assertEquals(View.VISIBLE, actual.findViewById(R.id.vendorShort).getVisibility());
    }

    @Test
    public void testMakeViewFullWithVendorShortMaximumSize() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(SSID, new WiFiAdditional(VENDOR_NAME, false));
        // execute
        View actual = fixture.makeView(null, null, wiFiDetail, false);
        // validate
        validateTextViewValue(actual, VENDOR_NAME.substring(0, 12), R.id.vendorShort);
    }

    @Test
    public void testMakeViewFullWithTabVisible() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(StringUtils.EMPTY, new WiFiAdditional(StringUtils.EMPTY, false));
        // execute
        View actual = fixture.makeView(null, null, wiFiDetail, true);
        // validate
        assertEquals(View.VISIBLE, actual.findViewById(R.id.tab).getVisibility());
    }

    @Test
    public void testMakeViewFullWithWiFiDetailAndEmptySSID() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(StringUtils.EMPTY, new WiFiAdditional(StringUtils.EMPTY, false));
        // execute
        View actual = fixture.makeView(null, null, wiFiDetail, false);
        // validate
        validateTextViewValuesFullView(actual, wiFiDetail);
    }

    @Test
    public void testMakeViewFullWithWiFiDetail() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(SSID, new WiFiAdditional(StringUtils.EMPTY, false));
        // execute
        View actual = fixture.makeView(null, null, wiFiDetail, false);
        // validate
        validateTextViewValuesFullView(actual, wiFiDetail);
    }

    @Test
    public void testMakeViewCompactWithTabGone() throws Exception {
        // setup
        WiFiAdditional wiFiAdditional = new WiFiAdditional(StringUtils.EMPTY, false);
        WiFiDetail wiFiDetail = withWiFiDetail(SSID, wiFiAdditional);
        when(settings.getAccessPointView()).thenReturn(AccessPointViewType.COMPACT);
        // execute
        View actual = fixture.makeView(null, null, wiFiDetail, false);
        // validate
        assertEquals(View.GONE, actual.findViewById(R.id.tab).getVisibility());
    }

    @Test
    public void testMakeViewCompactWithGroupIndicatorGone() throws Exception {
        // setup
        WiFiAdditional wiFiAdditional = new WiFiAdditional(StringUtils.EMPTY, false);
        WiFiDetail wiFiDetail = withWiFiDetail("SSID", wiFiAdditional);
        when(settings.getAccessPointView()).thenReturn(AccessPointViewType.COMPACT);
        // execute
        View actual = fixture.makeView(null, null, wiFiDetail, false);
        // validate
        assertEquals(View.GONE, actual.findViewById(R.id.groupIndicator).getVisibility());
    }

    @Test
    public void testMakeViewCompactWithTabVisible() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(StringUtils.EMPTY, new WiFiAdditional(StringUtils.EMPTY, false));
        when(settings.getAccessPointView()).thenReturn(AccessPointViewType.COMPACT);
        // execute
        View actual = fixture.makeView(null, null, wiFiDetail, true);
        // validate
        assertEquals(View.VISIBLE, actual.findViewById(R.id.tab).getVisibility());
    }

    @Test
    public void testMakeViewCompactWithWiFiDetailAndEmptySSID() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(StringUtils.EMPTY, new WiFiAdditional(StringUtils.EMPTY, false));
        when(settings.getAccessPointView()).thenReturn(AccessPointViewType.COMPACT);
        // execute
        View actual = fixture.makeView(null, null, wiFiDetail, false);
        // validate
        validateTextViewValuesCompactView(actual, wiFiDetail);
    }

    @Test
    public void testMakeViewCompactWithWiFiDetail() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(SSID, new WiFiAdditional(StringUtils.EMPTY, false));
        when(settings.getAccessPointView()).thenReturn(AccessPointViewType.COMPACT);
        // execute
        View actual = fixture.makeView(null, null, wiFiDetail, false);
        // validate
        validateTextViewValuesCompactView(actual, wiFiDetail);
    }

    @Test
    public void testMakeViewCompactWithAttachPopup() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(SSID, new WiFiAdditional(StringUtils.EMPTY, false));
        when(settings.getAccessPointView()).thenReturn(AccessPointViewType.COMPACT);
        // execute
        View actual = fixture.makeView(null, null, wiFiDetail, false);
        // validate
        assertNotNull(actual.findViewById(R.id.attachPopup));
    }

    @Test
    public void testMakeViewCompactDoesNotHaveFullDetails() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(SSID, new WiFiAdditional(StringUtils.EMPTY, false));
        when(settings.getAccessPointView()).thenReturn(AccessPointViewType.COMPACT);
        // execute
        View actual = fixture.makeView(null, null, wiFiDetail, false);
        // validate
        assertNull(actual.findViewById(R.id.channel_frequency_range));
        assertNull(actual.findViewById(R.id.width));
        assertNull(actual.findViewById(R.id.capabilities));
        assertNull(actual.findViewById(R.id.vendorShort));
    }

    @Test
    public void testMakeViewPopupWithWiFiDetail() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(SSID, new WiFiAdditional(StringUtils.EMPTY, false));
        // execute
        View actual = fixture.makeViewDetailed(wiFiDetail);
        // validate
        validateTextViewValuesFullView(actual, wiFiDetail);
    }

    @Test
    public void testMakeViewPopupWithVendorNotVisible() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(SSID, new WiFiAdditional(StringUtils.EMPTY, false));
        // execute
        View actual = fixture.makeViewDetailed(wiFiDetail);
        // validate
        assertEquals(View.GONE, actual.findViewById(R.id.vendorShort).getVisibility());
        assertEquals(View.GONE, actual.findViewById(R.id.vendorLong).getVisibility());
    }

    @Test
    public void testMakeViewPopupWithVendorVisible() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(SSID, new WiFiAdditional(VENDOR_NAME, false));
        // execute
        View actual = fixture.makeViewDetailed(wiFiDetail);
        // validate
        assertEquals(View.GONE, actual.findViewById(R.id.vendorShort).getVisibility());
        assertEquals(View.VISIBLE, actual.findViewById(R.id.vendorLong).getVisibility());
    }

    private WiFiDetail withWiFiDetail(String SSID, WiFiAdditional wiFiAdditional) {
        return new WiFiDetail(SSID, "BSSID", "capabilities", new WiFiSignal(1, 1, WiFiWidth.MHZ_40, 2), wiFiAdditional);
    }

    private void validateTextViewValuesFullView(@NonNull View view, @NonNull WiFiDetail wiFiDetail) {
        validateTextViewValuesCompactView(view, wiFiDetail);

        WiFiSignal wiFiSignal = wiFiDetail.getWiFiSignal();
        validateTextViewValue(view, wiFiSignal.getFrequencyStart() + " - " + wiFiSignal.getFrequencyEnd(), R.id.channel_frequency_range);
        validateTextViewValue(view, "(" + wiFiSignal.getWiFiWidth().getFrequencyWidth() + WiFiSignal.FREQUENCY_UNITS + ")", R.id.width);
        validateTextViewValue(view, wiFiDetail.getCapabilities(), R.id.capabilities);
    }

    private void validateTextViewValuesCompactView(@NonNull View view, @NonNull WiFiDetail wiFiDetail) {
        WiFiSignal wiFiSignal = wiFiDetail.getWiFiSignal();
        validateTextViewValue(view, wiFiDetail.getTitle(), R.id.ssid);
        validateTextViewValue(view, String.format(Locale.ENGLISH, "%ddBm", wiFiSignal.getLevel()), R.id.level);
        validateTextViewValue(view, wiFiSignal.getChannelDisplay(), R.id.channel);
        validateTextViewValue(view, String.format(Locale.ENGLISH, "%d%s", wiFiSignal.getPrimaryFrequency(), WiFiSignal.FREQUENCY_UNITS), R.id.primaryFrequency);
        validateTextViewValue(view, String.format(Locale.ENGLISH, "%5.1fm", wiFiSignal.getDistance()), R.id.distance);
    }

    private void validateTextViewValue(@NonNull View view, @NonNull String expected, int id) {
        assertEquals(expected, ((TextView) view.findViewById(id)).getText().toString());
    }

}