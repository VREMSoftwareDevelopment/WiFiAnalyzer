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

package com.vrem.wifianalyzer.wifi.filter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class FilterViewTest {

    private MainActivity mainActivity;
    private AlertDialog alertDialog;
    private FilterView fixture;

    @Before
    public void setUp() {
        mainActivity = RobolectricUtil.INSTANCE.getActivity();
        fixture = FilterView.build();
        alertDialog = fixture.getAlertDialog();
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testAlertDialog() throws Exception {
        assertFalse(alertDialog.isShowing());
        assertTrue(alertDialog instanceof AlertDialog);
    }

    @Test
    public void testShow() throws Exception {
        // execute
        fixture.show();
        // validate
        assertTrue(alertDialog.isShowing());
    }

    @Test
    public void testTitle() throws Exception {
        // setup
        String expected = mainActivity.getResources().getString(R.string.filter_title);
        ShadowAlertDialog shadowAlertDialog = shadowOf(alertDialog);
        // execute
        CharSequence actual = shadowAlertDialog.getTitle();
        // validate
        assertEquals(expected, actual.toString());
    }

    @Test
    public void testPositiveButton() throws Exception {
        // setup
        fixture.show();
        Button button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Filters filters = MainContextHelper.INSTANCE.getFilters();
        MainActivity mainActivity = MainContextHelper.INSTANCE.getMainActivity();
        // execute
        button.performClick();
        // validate
        assertFalse(alertDialog.isShowing());
        verify(filters).save();
        verify(mainActivity).update();
    }

    @Test
    public void testNegativeButton() throws Exception {
        // setup
        fixture.show();
        Button button = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        Filters filters = MainContextHelper.INSTANCE.getFilters();
        MainActivity mainActivity = MainContextHelper.INSTANCE.getMainActivity();
        // execute
        button.performClick();
        // validate
        assertFalse(alertDialog.isShowing());
        verify(filters).reset();
        verify(mainActivity).update();
    }

    @Test
    public void testNeutralButton() throws Exception {
        // setup
        fixture.show();
        Button button = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        Filters filters = MainContextHelper.INSTANCE.getFilters();
        MainActivity mainActivity = MainContextHelper.INSTANCE.getMainActivity();
        // execute
        button.performClick();
        // validate
        assertFalse(alertDialog.isShowing());
        verify(filters).reload();
        verify(mainActivity, never()).update();
    }

    @Test
    public void testWiFiBandFilterViewIsVisible() throws Exception {
        // setup
        fixture.show();
        // execute
        int actual = alertDialog.findViewById(R.id.filterWiFiBand).getVisibility();
        // validate
        assertEquals(View.VISIBLE, actual);
    }

    @Test
    public void testSecurityFilterViewIsVisible() throws Exception {
        // setup
        fixture.show();
        // execute
        int actual = alertDialog.findViewById(R.id.filterSecurity).getVisibility();
        // validate
        assertEquals(View.VISIBLE, actual);
    }

    @Test
    public void testStrengthFilterViewIsVisible() throws Exception {
        // setup
        fixture.show();
        // execute
        int actual = alertDialog.findViewById(R.id.filterStrength).getVisibility();
        // validate
        assertEquals(View.VISIBLE, actual);
    }
}