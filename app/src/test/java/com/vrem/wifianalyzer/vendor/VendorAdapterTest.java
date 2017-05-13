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

package com.vrem.wifianalyzer.vendor;

import android.view.View;
import android.widget.TextView;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.vendor.model.VendorService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class VendorAdapterTest {
    private static final String VENDOR1 = "V1";
    private static final String VENDOR2 = "V2";
    private static final String VENDOR3 = "V3";

    private VendorService vendorService;
    private VendorAdapter fixture;

    @Before
    public void setUp() {
        MainActivity mainActivity = RobolectricUtil.INSTANCE.getActivity();

        vendorService = MainContextHelper.INSTANCE.getVendorService();

        withVendorNames();

        fixture = new VendorAdapter(mainActivity, vendorService);
    }

    @After
    public void tearDown() {
        verify(vendorService).findVendorNames();
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testGetView() throws Exception {
        // setup
        when(vendorService.findMacAddresses(VENDOR2)).thenReturn(Arrays.asList("V2M1X1", "V2M2", "V2M3X1"));
        String expected = "V2:M1:X1, *V2M2*, V2:M3:X1";
        // execute
        View actual = fixture.getView(1, null, null);
        // validate
        assertNotNull(actual);

        assertEquals(VENDOR2, ((TextView) actual.findViewById(R.id.vendor_name)).getText());
        assertEquals(expected, ((TextView) actual.findViewById(R.id.vendor_macs)).getText());

        verify(vendorService, never()).findMacAddresses(VENDOR1);
        verify(vendorService).findMacAddresses(VENDOR2);
        verify(vendorService, never()).findMacAddresses(VENDOR3);
    }

    private void withVendorNames() {
        when(vendorService.findVendorNames()).thenReturn(Arrays.asList(VENDOR1, VENDOR2, VENDOR3));
    }

}