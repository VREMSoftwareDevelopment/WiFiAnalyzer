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

package com.vrem.wifianalyzer.wifi.filter;

import android.app.Dialog;
import android.os.Build;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.wifi.filter.adapter.SSIDAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.robolectric.annotation.LooperMode.Mode.PAUSED;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
@LooperMode(PAUSED)
public class SSIDFilterTest {

    private Dialog dialog;
    private EditText editText;
    private View view;
    private SSIDAdapter ssidAdapter;
    private Editable editable;

    @Before
    public void setUp() {
        RobolectricUtil.INSTANCE.getActivity();

        dialog = mock(Dialog.class);
        editText = mock(EditText.class);
        view = mock(View.class);
        ssidAdapter = mock(SSIDAdapter.class);
        editable = mock(Editable.class);

        when(dialog.findViewById(R.id.filterSSIDtext)).thenReturn(editText);
        when(dialog.findViewById(R.id.filterSSID)).thenReturn(view);
    }

    @Test
    public void testSSIDFilterWithValues() {
        // setup
        Set<String> values = new HashSet<>(Arrays.asList("", " ", "ABC", " JDS "));
        when(ssidAdapter.getValues()).thenReturn(values);
        String expected = "ABC JDS";
        // execute
        new SSIDFilter(ssidAdapter, dialog);
        // verify
        verify(editText).setText(expected);
        verify(dialog).findViewById(R.id.filterSSIDtext);
        verify(dialog).findViewById(R.id.filterSSID);
        verify(view).setVisibility(View.VISIBLE);
        verify(editText).addTextChangedListener(any(SSIDFilter.OnChange.class));
    }

    @Test
    public void testOnChangeAfterTextChangedWithNull() {
        // setup
        SSIDFilter.OnChange onChange = new SSIDFilter.OnChange(ssidAdapter);
        // execute
        onChange.afterTextChanged(null);
        // verify
        verify(ssidAdapter).setValues(new HashSet<>());
    }

    @Test
    public void testOnChangeAfterTextChangedWithValues() {
        // setup
        String value = " ABS ADF ";
        SSIDFilter.OnChange onChange = new SSIDFilter.OnChange(ssidAdapter);
        when(editable.toString()).thenReturn(value);
        Set<String> expected = new HashSet<>(Arrays.asList("ABS", "ADF"));
        // execute
        onChange.afterTextChanged(editable);
        // verify
        verify(ssidAdapter).setValues(expected);
    }
}