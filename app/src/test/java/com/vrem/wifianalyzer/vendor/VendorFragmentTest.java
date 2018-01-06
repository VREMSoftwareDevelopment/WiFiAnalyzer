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

package com.vrem.wifianalyzer.vendor;

import android.text.Editable;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.vendor.model.VendorService;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class VendorFragmentTest {

    private VendorFragment fixture;
    private VendorService vendorService;

    @Before
    public void setUp() {
        RobolectricUtil.INSTANCE.getActivity();

        vendorService = MainContextHelper.INSTANCE.getVendorService();

        fixture = new VendorFragment();
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testOnCreateView() throws Exception {
        // setup
        when(vendorService.findVendors()).thenReturn(Collections.<String>emptyList());
        // execute
        SupportFragmentTestUtil.startFragment(fixture);
        // validate
        assertNotNull(fixture);
        verify(vendorService).findVendors();
    }

    @Test
    public void testOnChangeAfterTextChangedWithValues() throws Exception {
        // setup
        String values = "     ABS       ADF      ";
        String expected = "ABS ADF";
        VendorAdapter vendorAdapter = mock(VendorAdapter.class);
        VendorFragment.OnChange onChange = new VendorFragment.OnChange(vendorAdapter);
        Editable editable = mock(Editable.class);
        when(editable.toString()).thenReturn(values);
        // execute
        onChange.afterTextChanged(editable);
        // verify
        verify(vendorAdapter).update(expected);
    }

    @Test
    public void testOnChangeAfterTextChangedWithNull() throws Exception {
        // setup
        VendorAdapter vendorAdapter = mock(VendorAdapter.class);
        VendorFragment.OnChange onChange = new VendorFragment.OnChange(vendorAdapter);
        // execute
        onChange.afterTextChanged(null);
        // verify
        verify(vendorAdapter).update(StringUtils.EMPTY);
    }


}