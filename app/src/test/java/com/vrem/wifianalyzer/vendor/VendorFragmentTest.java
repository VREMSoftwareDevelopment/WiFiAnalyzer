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

package com.vrem.wifianalyzer.vendor;

import android.os.Build;

import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.vendor.VendorFragment.Listener;
import com.vrem.wifianalyzer.vendor.model.VendorService;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import java.util.Collections;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.robolectric.annotation.LooperMode.Mode.PAUSED;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
@LooperMode(PAUSED)
public class VendorFragmentTest {

    private VendorFragment fixture;
    private VendorService vendorService;

    @Before
    public void setUp() {
        RobolectricUtil.INSTANCE.getActivity();
        vendorService = MainContextHelper.INSTANCE.getVendorService();

        when(vendorService.findVendors()).thenReturn(Collections.emptyList());
        fixture = new VendorFragment();
        RobolectricUtil.INSTANCE.startFragment(fixture);

    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testOnCreateView() {
        // validate
        assertNotNull(fixture);
        verify(vendorService).findVendors();
    }

    @Test
    public void testListenerOnQueryTextChange() {
        // setup
        String values = "     ABS       ADF      ";
        String expected = "ABS ADF";
        VendorAdapter vendorAdapter = mock(VendorAdapter.class);
        Listener fixture = new Listener(vendorAdapter);
        // execute
        boolean actual = fixture.onQueryTextChange(values);
        // verify
        assertTrue(actual);
        verify(vendorAdapter).update(expected);
    }

    @Test
    public void testListenerOnQueryTextChangeWithNull() {
        // setup
        VendorAdapter vendorAdapter = mock(VendorAdapter.class);
        Listener fixture = new Listener(vendorAdapter);
        // execute
        boolean actual = fixture.onQueryTextChange(null);
        // verify
        assertTrue(actual);
        verify(vendorAdapter).update(StringUtils.EMPTY);
    }


}