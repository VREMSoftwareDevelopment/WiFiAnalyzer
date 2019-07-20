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

package com.vrem.wifianalyzer.wifi.accesspoint;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.model.WiFiConnection;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.annotation.LooperMode.Mode.PAUSED;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
@LooperMode(PAUSED)
public class AccessPointsAdapterTest {

    private MainActivity mainActivity;
    private AccessPointsAdapter fixture;
    private AccessPointsAdapterData accessPointsAdapterData;
    private AccessPointDetail accessPointDetail;
    private AccessPointPopup accessPointPopup;
    private ExpandableListView expandableListView;
    private ViewGroup viewGroup;
    private Settings settings;

    @Before
    public void setUp() {
        mainActivity = RobolectricUtil.INSTANCE.getActivity();

        settings = MainContextHelper.INSTANCE.getSettings();
        accessPointsAdapterData = mock(AccessPointsAdapterData.class);
        accessPointDetail = mock(AccessPointDetail.class);
        accessPointPopup = mock(AccessPointPopup.class);
        expandableListView = mock(ExpandableListView.class);
        viewGroup = mock(ViewGroup.class);

        fixture = new AccessPointsAdapter();
        fixture.setAccessPointsAdapterData(accessPointsAdapterData);
        fixture.setAccessPointDetail(accessPointDetail);
        fixture.setAccessPointPopup(accessPointPopup);
        fixture.setExpandableListView(expandableListView);
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testGetGroupViewWithNoChildren() {
        // setup
        WiFiDetail wiFiDetail = WiFiDetail.EMPTY;
        when(accessPointsAdapterData.parent(1)).thenReturn(wiFiDetail);
        when(accessPointsAdapterData.childrenCount(1)).thenReturn(0);
        View view = withView(wiFiDetail, AccessPointViewType.COMPLETE, false);
        // execute
        View actual = fixture.getGroupView(1, false, view, viewGroup);
        // validate
        assertNotNull(actual);
        assertEquals(View.GONE, actual.findViewById(R.id.groupIndicator).getVisibility());
        verify(accessPointsAdapterData).parent(1);
        verify(accessPointsAdapterData).childrenCount(1);
        verifyView(view, wiFiDetail, false);
    }

    @Test
    public void testGetGroupViewCompactAddsPopup() {
        // setup
        WiFiDetail wiFiDetail = WiFiDetail.EMPTY;
        when(accessPointsAdapterData.parent(1)).thenReturn(wiFiDetail);
        when(accessPointsAdapterData.childrenCount(1)).thenReturn(0);
        View view = withView(wiFiDetail, AccessPointViewType.COMPACT, false);
        // execute
        View actual = fixture.getGroupView(1, false, view, viewGroup);
        // validate
        assertNotNull(actual);
        assertEquals(View.GONE, actual.findViewById(R.id.groupIndicator).getVisibility());
        verify(accessPointsAdapterData).parent(1);
        verify(accessPointsAdapterData).childrenCount(1);
        verifyView(view, wiFiDetail, false);
        verify(accessPointPopup).attach(view.findViewById(R.id.attachPopup), wiFiDetail);
        verify(accessPointPopup).attach(view.findViewById(R.id.ssid), wiFiDetail);
    }

    @Test
    public void testGetGroupViewWithChildren() {
        // setup
        WiFiDetail wiFiDetail = WiFiDetail.EMPTY;
        when(accessPointsAdapterData.parent(1)).thenReturn(wiFiDetail);
        when(accessPointsAdapterData.childrenCount(1)).thenReturn(5);
        View view = withView(wiFiDetail, AccessPointViewType.COMPACT, false);
        // execute
        View actual = fixture.getGroupView(1, false, view, viewGroup);
        // validate
        assertNotNull(actual);
        assertEquals(View.VISIBLE, actual.findViewById(R.id.groupIndicator).getVisibility());
        verify(accessPointsAdapterData).parent(1);
        verify(accessPointsAdapterData).childrenCount(1);
        verifyView(view, wiFiDetail, false);
    }

    @Test
    public void testGetChildView() {
        // setup
        WiFiDetail wiFiDetail = WiFiDetail.EMPTY;
        when(accessPointsAdapterData.child(0, 0)).thenReturn(wiFiDetail);
        View view = withView(wiFiDetail, AccessPointViewType.COMPLETE, true);
        // execute
        View actual = fixture.getChildView(0, 0, false, view, viewGroup);
        // validate
        assertNotNull(actual);
        assertEquals(View.GONE, actual.findViewById(R.id.groupIndicator).getVisibility());
        verify(accessPointsAdapterData).child(0, 0);
        verifyView(view, wiFiDetail, true);
    }

    @Test
    public void testGetChildViewCompactAddsPopup() {
        // setup
        WiFiDetail wiFiDetail = WiFiDetail.EMPTY;
        when(accessPointsAdapterData.child(0, 0)).thenReturn(wiFiDetail);
        View view = withView(wiFiDetail, AccessPointViewType.COMPACT, true);
        // execute
        View actual = fixture.getChildView(0, 0, false, view, viewGroup);
        // validate
        assertNotNull(actual);
        assertEquals(View.GONE, actual.findViewById(R.id.groupIndicator).getVisibility());
        verify(accessPointsAdapterData).child(0, 0);
        verifyView(view, wiFiDetail, true);
        verify(accessPointPopup).attach(view.findViewById(R.id.attachPopup), wiFiDetail);
        verify(accessPointPopup).attach(view.findViewById(R.id.ssid), wiFiDetail);
    }

    @Test
    public void testUpdate() {
        // setup
        WiFiData wiFiData = new WiFiData(Collections.emptyList(), WiFiConnection.EMPTY);
        // execute
        fixture.update(wiFiData);
        // validate
        verify(accessPointsAdapterData).update(wiFiData, expandableListView);
    }

    @Test
    public void testGetGroupCount() {
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
    public void testGetChildrenCount() {
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
    public void testGetGroup() {
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
    public void testGetChild() {
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
    public void testGetGroupId() {
        assertEquals(22, fixture.getGroupId(22));
    }

    @Test
    public void testGetChildId() {
        assertEquals(11, fixture.getChildId(1, 11));
    }

    @Test
    public void testHasStableIds() {
        assertTrue(fixture.hasStableIds());
    }

    @Test
    public void testIsChildSelectable() {
        assertTrue(fixture.isChildSelectable(0, 0));
    }

    @Test
    public void testOnGroupCollapsed() {
        // setup
        int index = 11;
        // execute
        fixture.onGroupCollapsed(index);
        // validate
        verify(accessPointsAdapterData).onGroupCollapsed(index);
    }

    @Test
    public void testOnGroupExpanded() {
        // setup
        int index = 22;
        // execute
        fixture.onGroupExpanded(index);
        // validate
        verify(accessPointsAdapterData).onGroupExpanded(index);
    }

    private View withView(@NonNull WiFiDetail wiFiDetail, @NonNull AccessPointViewType accessPointViewType, boolean isChild) {
        View view = mainActivity.getLayoutInflater().inflate(accessPointViewType.getLayout(), null, isChild);
        when(settings.getAccessPointView()).thenReturn(accessPointViewType);
        when(accessPointDetail.makeView(view, viewGroup, wiFiDetail, isChild)).thenReturn(view);
        return view;
    }

    private void verifyView(View view, WiFiDetail wiFiDetail, boolean isChild) {
        verify(accessPointDetail).makeView(view, viewGroup, wiFiDetail, isChild);
    }

}