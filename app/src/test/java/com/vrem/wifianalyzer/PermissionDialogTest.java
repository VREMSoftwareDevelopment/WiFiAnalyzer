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

package com.vrem.wifianalyzer;

import android.app.Activity;
import android.content.DialogInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static com.vrem.wifianalyzer.PermissionDialog.CancelClick;
import static com.vrem.wifianalyzer.PermissionDialog.OkClick;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(RobolectricTestRunner.class)
public class PermissionDialogTest {

    private PermissionDialog fixture;

    @Before
    public void setUp() {
        MainActivity activity = RobolectricUtil.INSTANCE.getActivity();
        fixture = new PermissionDialog(activity);
    }

    @Test
    public void testShow() {
        // execute
        fixture.show();
    }

    @Test
    public void testOkClick() {
        // setup
        Activity activity = mock(Activity.class);
        DialogInterface dialog = mock(DialogInterface.class);
        OkClick fixture = new OkClick(activity);
        // execute
        fixture.onClick(dialog, 0);
        // validate
        verify(activity).requestPermissions(PermissionChecker.PERMISSIONS, PermissionChecker.REQUEST_CODE);
        verify(dialog).dismiss();
        verifyNoMoreInteractions(activity);
        verifyNoMoreInteractions(dialog);
    }

    @Test
    public void testCancelClick() {
        // setup
        Activity activity = mock(Activity.class);
        DialogInterface dialog = mock(DialogInterface.class);
        CancelClick fixture = new CancelClick(activity);
        // execute
        fixture.onClick(dialog, 0);
        // validate
        verify(activity).finish();
        verify(dialog).dismiss();
        verifyNoMoreInteractions(activity);
        verifyNoMoreInteractions(dialog);
    }

}