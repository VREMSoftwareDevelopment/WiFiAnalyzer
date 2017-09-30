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
import android.view.ViewGroup;
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
    private static final String VENDOR_NAME1 = "N1";
    private static final String VENDOR_NAME2 = "N2";
    private static final String VENDOR_NAME3 = "N3";

    private MainActivity mainActivity;
    private VendorService vendorService;
    private VendorAdapter fixture;

    @Before
    public void setUp() {
        mainActivity = RobolectricUtil.INSTANCE.getActivity();

        vendorService = MainContextHelper.INSTANCE.getVendorService();

        withVendorNames();

        fixture = new VendorAdapter(mainActivity, vendorService);
    }

    @After
    public void tearDown() {
        verify(vendorService).findVendors();
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testGetView() throws Exception {
        // setup
        when(vendorService.findMacAddresses(VENDOR_NAME2)).thenReturn(Arrays.asList("V2M1X1", "V2M2", "V2M3X1"));
        String expected = "V2:M1:X1, *V2M2*, V2:M3:X1";
        ViewGroup viewGroup = (ViewGroup) mainActivity.findViewById(android.R.id.content);
        // execute
        View actual = fixture.getView(1, null, viewGroup);
        // validate
        assertNotNull(actual);

        assertEquals(VENDOR_NAME2, ((TextView) actual.findViewById(R.id.vendor_name)).getText());
        assertEquals(expected, ((TextView) actual.findViewById(R.id.vendor_macs)).getText());

        verify(vendorService).findMacAddresses(VENDOR_NAME2);

        verify(vendorService, never()).findVendorName(VENDOR_NAME1);
        verify(vendorService, never()).findVendorName(VENDOR_NAME3);
    }

    private void withVendorNames() {
        when(vendorService.findVendors()).thenReturn(Arrays.asList(VENDOR_NAME1, VENDOR_NAME2, VENDOR_NAME3));
    }

}