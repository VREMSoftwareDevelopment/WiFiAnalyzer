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

package com.vrem.wifianalyzer.wifi;

import android.view.View;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.Configuration;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.wifi.model.WiFiConnection;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AccessPointsAdapterTest {

    private MainActivity mainActivity;
    private AccessPointsAdapter fixture;
    private AccessPointsAdapterData accessPointsAdapterData;
    private AccessPointsDetail accessPointsDetail;
    private Configuration configuration;
    private Scanner scanner;

    @Before
    public void setUp() throws Exception {
        mainActivity = RobolectricUtil.INSTANCE.getMainActivity();

        accessPointsAdapterData = mock(AccessPointsAdapterData.class);
        accessPointsDetail = mock(AccessPointsDetail.class);

        configuration = MainContextHelper.INSTANCE.getConfiguration();
        scanner = MainContextHelper.INSTANCE.getScanner();

        fixture = new AccessPointsAdapter(mainActivity);
        fixture.setAccessPointsAdapterData(accessPointsAdapterData);
        fixture.setAccessPointsDetail(accessPointsDetail);
    }

    @After
    public void tearDown() throws Exception {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testAccessPointAdapter() throws Exception {
        verify(scanner).addUpdateNotifier(fixture);
    }

    @Test
    public void testGetGroupViewWithNoChildren() throws Exception {
        // setup
        WiFiDetail wiFiDetail = WiFiDetail.EMPTY;
        when(configuration.isLargeScreenLayout()).thenReturn(true);
        when(accessPointsAdapterData.parent(1)).thenReturn(wiFiDetail);
        when(accessPointsAdapterData.childrenCount(1)).thenReturn(0);
        // execute
        View actual = fixture.getGroupView(1, false, null, null);
        // validate
        assertNotNull(actual);
        assertEquals(View.GONE, actual.findViewById(R.id.groupIndicator).getVisibility());
        verify(accessPointsDetail).setView(mainActivity.getResources(), actual, wiFiDetail, false, true);
        verify(accessPointsAdapterData).parent(1);
        verify(accessPointsAdapterData).childrenCount(1);
        verify(configuration).isLargeScreenLayout();
    }

    @Test
    public void testGetGroupViewWithChildren() throws Exception {
        // setup
        WiFiDetail wiFiDetail = WiFiDetail.EMPTY;
        when(accessPointsAdapterData.parent(1)).thenReturn(wiFiDetail);
        when(accessPointsAdapterData.childrenCount(1)).thenReturn(5);
        // execute
        View actual = fixture.getGroupView(1, false, null, null);
        // validate
        assertNotNull(actual);
        assertEquals(View.VISIBLE, actual.findViewById(R.id.groupIndicator).getVisibility());
        verify(accessPointsDetail).setView(mainActivity.getResources(), actual, wiFiDetail, false, false);
        verify(accessPointsAdapterData).parent(1);
        verify(accessPointsAdapterData).childrenCount(1);
    }

    @Test
    public void testGetChildView() throws Exception {
        // setup
        WiFiDetail wiFiDetail = WiFiDetail.EMPTY;
        when(accessPointsAdapterData.child(0, 0)).thenReturn(wiFiDetail);
        // execute
        View actual = fixture.getChildView(0, 0, false, null, null);
        // validate
        assertNotNull(actual);
        assertEquals(View.GONE, actual.findViewById(R.id.groupIndicator).getVisibility());
        verify(accessPointsDetail).setView(mainActivity.getResources(), actual, wiFiDetail, true, false);
        verify(accessPointsAdapterData).child(0, 0);
    }

    @Test
    public void testUpdate() throws Exception {
        // setup
        WiFiData wiFiData = new WiFiData(new ArrayList<WiFiDetail>(), WiFiConnection.EMPTY, new ArrayList<String>());
        // execute
        fixture.update(wiFiData);
        // validate
        verify(accessPointsAdapterData).update(wiFiData);
    }

    @Test
    public void testGetGroupCount() throws Exception {
        // setup
        int expected = 5;
        when(accessPointsAdapterData.parentsCount()).thenReturn(expected);
        // execute
        int actual = fixture.getGroupCount();
        // validate
        assertEquals(expected, actual);
        verify(accessPointsAdapterData).parentsCount();
    }

    @Test
    public void testGetChildrenCount() throws Exception {
        // setup
        int expected = 25;
        when(accessPointsAdapterData.childrenCount(1)).thenReturn(expected);
        // execute
        int actual = fixture.getChildrenCount(1);
        // validate
        assertEquals(expected, actual);
        verify(accessPointsAdapterData).childrenCount(1);
    }

    @Test
    public void testGetGroup() throws Exception {
        // setup
        WiFiDetail expected = WiFiDetail.EMPTY;
        when(accessPointsAdapterData.parent(3)).thenReturn(expected);
        // execute
        WiFiDetail actual = (WiFiDetail) fixture.getGroup(3);
        // validate
        assertEquals(expected, actual);
        verify(accessPointsAdapterData).parent(3);
    }

    @Test
    public void testGetChild() throws Exception {
        // setup
        WiFiDetail expected = WiFiDetail.EMPTY;
        when(accessPointsAdapterData.child(1, 2)).thenReturn(expected);
        // execute
        WiFiDetail actual = (WiFiDetail) fixture.getChild(1, 2);
        // validate
        assertEquals(expected, actual);
        verify(accessPointsAdapterData).child(1, 2);
    }

    @Test
    public void testGetGroupId() throws Exception {
        assertEquals(22, fixture.getGroupId(22));
    }

    @Test
    public void testGetChildId() throws Exception {
        assertEquals(11, fixture.getChildId(1, 11));
    }

    @Test
    public void testHasStableIds() throws Exception {
        assertTrue(fixture.hasStableIds());
    }

    @Test
    public void testIsChildSelectable() throws Exception {
        assertTrue(fixture.isChildSelectable(0, 0));
    }
}