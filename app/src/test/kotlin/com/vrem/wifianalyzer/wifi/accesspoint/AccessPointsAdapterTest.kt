/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2021 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.wifi.accesspoint

import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.model.WiFiConnection
import com.vrem.wifianalyzer.wifi.model.WiFiData
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q])
class AccessPointsAdapterTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val accessPointsAdapterData: AccessPointsAdapterData = mock()
    private val accessPointDetail: AccessPointDetail = mock()
    private val accessPointPopup: AccessPointPopup = mock()
    private val expandableListView: ExpandableListView = mock()
    private val viewGroup: ViewGroup = mock()
    private val settings = MainContextHelper.INSTANCE.settings
    private val fixture = AccessPointsAdapter(accessPointsAdapterData, accessPointDetail, accessPointPopup)

    @Before
    fun setUp() {
        fixture.expandableListView = expandableListView
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(accessPointsAdapterData)
        verifyNoMoreInteractions(accessPointDetail)
        verifyNoMoreInteractions(expandableListView)
        verifyNoMoreInteractions(viewGroup)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun testGetGroupViewWithNoChildren() {
        // setup
        val wiFiDetail = WiFiDetail.EMPTY
        whenever(accessPointsAdapterData.parent(1)).thenReturn(wiFiDetail)
        whenever(accessPointsAdapterData.childrenCount(1)).thenReturn(0)
        val view = withView(wiFiDetail, AccessPointViewType.COMPLETE)
        // execute
        val actual = fixture.getGroupView(1, false, view, viewGroup)
        // validate
        assertNotNull(actual)
        assertEquals(View.GONE, actual.findViewById<View>(R.id.groupIndicator).visibility)
        verify(accessPointsAdapterData).parent(1)
        verify(accessPointsAdapterData).childrenCount(1)
        verifyView(view, wiFiDetail)
    }

    @Test
    fun testGetGroupViewCompactAddsPopup() {
        // setup
        val wiFiDetail = WiFiDetail.EMPTY
        whenever(accessPointsAdapterData.parent(1)).thenReturn(wiFiDetail)
        whenever(accessPointsAdapterData.childrenCount(1)).thenReturn(0)
        val view = withView(wiFiDetail, AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.getGroupView(1, false, view, viewGroup)
        // validate
        assertNotNull(actual)
        assertEquals(View.GONE, actual.findViewById<View>(R.id.groupIndicator).visibility)
        verify(accessPointsAdapterData).parent(1)
        verify(accessPointsAdapterData).childrenCount(1)
        verifyView(view, wiFiDetail)
        verify(accessPointPopup).attach(view.findViewById(R.id.attachPopup), wiFiDetail)
        verify(accessPointPopup).attach(view.findViewById(R.id.ssid), wiFiDetail)
    }

    @Test
    fun testGetGroupViewWithChildren() {
        // setup
        val wiFiDetail = WiFiDetail.EMPTY
        whenever(accessPointsAdapterData.parent(1)).thenReturn(wiFiDetail)
        whenever(accessPointsAdapterData.childrenCount(1)).thenReturn(5)
        val view = withView(wiFiDetail, AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.getGroupView(1, false, view, viewGroup)
        // validate
        assertNotNull(actual)
        assertEquals(View.VISIBLE, actual.findViewById<View>(R.id.groupIndicator).visibility)
        verify(accessPointsAdapterData).parent(1)
        verify(accessPointsAdapterData).childrenCount(1)
        verifyView(view, wiFiDetail)
    }

    @Test
    fun testGetChildView() {
        // setup
        val wiFiDetail = WiFiDetail.EMPTY
        whenever(accessPointsAdapterData.child(0, 0)).thenReturn(wiFiDetail)
        val view = withChildView(wiFiDetail, AccessPointViewType.COMPLETE)
        // execute
        val actual = fixture.getChildView(0, 0, false, view, viewGroup)
        // validate
        assertNotNull(actual)
        assertEquals(View.GONE, actual.findViewById<View>(R.id.groupIndicator).visibility)
        verify(accessPointsAdapterData).child(0, 0)
        verifyChildView(view, wiFiDetail)
    }

    @Test
    fun testGetChildViewCompactAddsPopup() {
        // setup
        val wiFiDetail = WiFiDetail.EMPTY
        whenever(accessPointsAdapterData.child(0, 0)).thenReturn(wiFiDetail)
        val view = withChildView(wiFiDetail, AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.getChildView(0, 0, false, view, viewGroup)
        // validate
        assertNotNull(actual)
        assertEquals(View.GONE, actual.findViewById<View>(R.id.groupIndicator).visibility)
        verify(accessPointsAdapterData).child(0, 0)
        verifyChildView(view, wiFiDetail)
        verify(accessPointPopup).attach(view.findViewById(R.id.attachPopup), wiFiDetail)
        verify(accessPointPopup).attach(view.findViewById(R.id.ssid), wiFiDetail)
    }

    @Test
    fun testUpdate() {
        // setup
        val wiFiData = WiFiData(listOf(), WiFiConnection.EMPTY)
        // execute
        fixture.update(wiFiData)
        // validate
        verify(accessPointsAdapterData).update(wiFiData, expandableListView)
    }

    @Test
    fun testGetGroupCount() {
        // setup
        val expected = 5
        whenever(accessPointsAdapterData.parentsCount()).thenReturn(expected)
        // execute
        val actual = fixture.groupCount
        // validate
        assertEquals(expected, actual)
        verify(accessPointsAdapterData).parentsCount()
    }

    @Test
    fun testGetChildrenCount() {
        // setup
        val expected = 25
        whenever(accessPointsAdapterData.childrenCount(1)).thenReturn(expected)
        // execute
        val actual = fixture.getChildrenCount(1)
        // validate
        assertEquals(expected, actual)
        verify(accessPointsAdapterData).childrenCount(1)
    }

    @Test
    fun testGetGroup() {
        // setup
        val expected = WiFiDetail.EMPTY
        whenever(accessPointsAdapterData.parent(3)).thenReturn(expected)
        // execute
        val actual = fixture.getGroup(3)
        // validate
        assertEquals(expected, actual)
        verify(accessPointsAdapterData).parent(3)
    }

    @Test
    fun testGetChild() {
        // setup
        val expected = WiFiDetail.EMPTY
        whenever(accessPointsAdapterData.child(1, 2)).thenReturn(expected)
        // execute
        val actual = fixture.getChild(1, 2)
        // validate
        assertEquals(expected, actual)
        verify(accessPointsAdapterData).child(1, 2)
    }

    @Test
    fun testGetGroupId() {
        assertEquals(22, fixture.getGroupId(22))
    }

    @Test
    fun testGetChildId() {
        assertEquals(11, fixture.getChildId(1, 11))
    }

    @Test
    fun testHasStableIds() {
        assertTrue(fixture.hasStableIds())
    }

    @Test
    fun testIsChildSelectable() {
        assertTrue(fixture.isChildSelectable(0, 0))
    }

    @Test
    fun testOnGroupCollapsed() {
        // setup
        val index = 11
        // execute
        fixture.onGroupCollapsed(index)
        // validate
        verify(accessPointsAdapterData).onGroupCollapsed(index)
    }

    @Test
    fun testOnGroupExpanded() {
        // setup
        val index = 22
        // execute
        fixture.onGroupExpanded(index)
        // validate
        verify(accessPointsAdapterData).onGroupExpanded(index)
    }

    private fun withView(wiFiDetail: WiFiDetail, accessPointViewType: AccessPointViewType): View {
        val view = mainActivity.layoutInflater.inflate(accessPointViewType.layout, null, false)
        whenever(settings.accessPointView()).thenReturn(accessPointViewType)
        whenever(accessPointDetail.makeView(view, viewGroup, wiFiDetail)).thenReturn(view)
        return view
    }

    private fun withChildView(wiFiDetail: WiFiDetail, accessPointViewType: AccessPointViewType): View {
        val view = mainActivity.layoutInflater.inflate(accessPointViewType.layout, null, true)
        whenever(settings.accessPointView()).thenReturn(accessPointViewType)
        whenever(accessPointDetail.makeView(view, viewGroup, wiFiDetail, true)).thenReturn(view)
        return view
    }

    private fun verifyView(view: View, wiFiDetail: WiFiDetail) {
        verify(accessPointDetail).makeView(view, viewGroup, wiFiDetail)
    }

    private fun verifyChildView(view: View, wiFiDetail: WiFiDetail) {
        verify(accessPointDetail).makeView(view, viewGroup, wiFiDetail, true)
    }
}