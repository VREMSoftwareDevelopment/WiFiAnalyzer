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

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class VendorAdapterTest {
    private static final String VENDOR1 = "V1";
    private static final String VENDOR2 = "V2";
    private static final String VENDOR3 = "V3";

    private VendorAdapter fixture;
    private SortedMap<String, List<String>> vendors;

    @Before
    public void setUp() {
        MainActivity mainActivity = RobolectricUtil.INSTANCE.getActivity();
        vendors = withVendors();
        fixture = new VendorAdapter(mainActivity, vendors);

        assertEquals(vendors, fixture.getVendors());
    }

    @Test
    public void testConstructor() throws Exception {
        assertEquals(vendors, fixture.getVendors());
    }

    @Test
    public void testGetView() throws Exception {
        // setup
        List<String> macs = vendors.get(VENDOR2);
        String expected = String.format("%s:%s:%s", macs.get(0).substring(0, 2), macs.get(0).substring(2, 4), macs.get(0).substring(4, 6)) +
            ", *" + macs.get(1) + "*, " +
            String.format("%s:%s:%s", macs.get(2).substring(0, 2), macs.get(2).substring(2, 4), macs.get(2).substring(4, 6));
        // execute
        View actual = fixture.getView(1, null, null);
        // validate
        assertNotNull(actual);

        assertEquals(VENDOR2, ((TextView) actual.findViewById(R.id.vendor_name)).getText());
        assertEquals(expected, ((TextView) actual.findViewById(R.id.vendor_macs)).getText());
    }

    @Test
    public void testSetVendors() throws Exception {
        // setup
        SortedMap<String, List<String>> expected = new TreeMap<>();
        // execute
        fixture.setVendors(expected);
        // validate
        assertEquals(expected, fixture.getVendors());
    }

    @NonNull
    private SortedMap<String, List<String>> withVendors() {
        SortedMap<String, List<String>> vendors = new TreeMap<>();
        vendors.put(VENDOR1, Arrays.asList("V1M1Z1", "V1M2Z1", "V1M3Z1"));
        vendors.put(VENDOR2, Arrays.asList("V2M1X1", "V2M2", "V2M3X1"));
        vendors.put(VENDOR3, Arrays.asList("V3M1Y1", "V3M2Y1", "V3M3Y1"));
        return vendors;
    }

}