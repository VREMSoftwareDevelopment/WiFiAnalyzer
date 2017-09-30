/*
 * WiFiAnalyzer
 * Copyright (C) 2017  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.view.View;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.wifi.band.WiFiWidth;
import com.vrem.wifianalyzer.wifi.model.WiFiAdditional;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AccessPointPopupTest {
    private MainActivity mainActivity;
    private AccessPointPopup fixture;

    @Before
    public void setUp() {
        mainActivity = RobolectricUtil.INSTANCE.getActivity();
        fixture = new AccessPointPopup();
    }

    @Test
    public void testShowOpensPopup() throws Exception {
        // setup
        View view = mainActivity.getLayoutInflater().inflate(R.layout.access_point_view_popup, null);
        // execute
        Dialog actual = fixture.show(view);
        // validate
        assertNotNull(actual);
        assertTrue(actual.isShowing());
    }

    @Test
    public void testPopupIsClosedOnCloseButtonClick() throws Exception {
        // setup
        View view = mainActivity.getLayoutInflater().inflate(R.layout.access_point_view_popup, null);
        Dialog dialog = fixture.show(view);
        View closeButton = dialog.findViewById(R.id.popupButtonClose);
        // execute
        closeButton.performClick();
        // validate
        assertFalse(dialog.isShowing());
    }

    @Test
    public void testAttach() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withWiFiDetail();
        View view = mainActivity.getLayoutInflater().inflate(R.layout.access_point_view_compact, null);
        // execute
        fixture.attach(view, wiFiDetail);
        // validate
        assertTrue(view.performClick());
    }

    @NonNull
    private WiFiDetail withWiFiDetail() {
        return new WiFiDetail("SSID", "BSSID", "capabilities", new WiFiSignal(1, 1, WiFiWidth.MHZ_40, 2), WiFiAdditional.EMPTY);
    }

}