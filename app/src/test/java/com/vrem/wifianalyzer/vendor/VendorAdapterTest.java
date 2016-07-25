/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
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
    public void setUp() throws Exception {
        MainActivity mainActivity = RobolectricUtil.INSTANCE.getMainActivity();
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