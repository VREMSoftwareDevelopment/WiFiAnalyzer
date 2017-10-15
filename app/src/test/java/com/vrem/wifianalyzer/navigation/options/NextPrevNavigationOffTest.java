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

package com.vrem.wifianalyzer.navigation.options;

import android.view.View;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NextPrevNavigationOffTest {
    @Mock
    private MainActivity mainActivity;
    @Mock
    private View view;

    @Test
    public void testApplySwitchesOffOnTouchListener() throws Exception {
        // setup
        NextPrevNavigationOff fixture = new NextPrevNavigationOff();
        when(mainActivity.findViewById(R.id.main_fragment_layout)).thenReturn(view);
        // execute
        fixture.apply(mainActivity);
        // validate
        verify(mainActivity).findViewById(R.id.main_fragment_layout);
        verify(view).setOnTouchListener(NextPrevNavigationOff.ON_TOUCH_LISTENER_EMPTY);
    }


    @Test
    public void testOnTouchListenerEmptyDoesNotDoAnyEvents() throws Exception {
        assertFalse(NextPrevNavigationOff.ON_TOUCH_LISTENER_EMPTY.onTouch(null, null));
    }
}