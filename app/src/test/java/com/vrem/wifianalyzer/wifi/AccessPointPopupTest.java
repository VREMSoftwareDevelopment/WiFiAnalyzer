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
import android.view.View;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainActivity;
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AccessPointPopupTest {
    private MainActivity mainActivity;

    private AccessPointDetail accessPointDetail;
    private AccessPointPopup fixture;

    @Before
    public void setUp() {
        mainActivity = RobolectricUtil.INSTANCE.getMainActivity();

        accessPointDetail = mock(AccessPointDetail.class);

        fixture = new AccessPointPopup(accessPointDetail);
    }

    @Test
    public void testShowWithViewPopupAccessPointDetail() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(new WiFiAdditional(StringUtils.EMPTY, false));
        View popupView = fixture.getPopupView();
        // execute
        Dialog actual = fixture.show(wiFiDetail);
        // validate
        assertNotNull(actual);
        assertTrue(actual.isShowing());
        verify(accessPointDetail).setViewPopup(mainActivity.getResources(), popupView, wiFiDetail);
    }

    @Test
    public void testShowWithAttachedOnClickListener() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail(new WiFiAdditional(StringUtils.EMPTY, false));
        Dialog dialog = fixture.show(wiFiDetail);
        View view = dialog.findViewById(R.id.popupButton);
        // execute
        view.performClick();
        // validate
        assertFalse(dialog.isShowing());

    }

    private WiFiDetail withWiFiDetail(WiFiAdditional wiFiAdditional) {
        return new WiFiDetail("SSID", "BSSID", "capabilities", new WiFiSignal(1, 1, WiFiWidth.MHZ_40, 2), wiFiAdditional);
    }

}