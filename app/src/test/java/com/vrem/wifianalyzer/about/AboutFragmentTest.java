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

package com.vrem.wifianalyzer.about;

import android.app.AlertDialog;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.view.View;
import android.widget.TextView;

import com.vrem.util.FileUtils;
import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.Configuration;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class AboutFragmentTest {

    private Configuration configuration;
    private AboutFragment fixture;

    @Before
    public void setUp() {
        RobolectricUtil.INSTANCE.getActivity();
        configuration = MainContextHelper.INSTANCE.getConfiguration();
        when(configuration.isSizeAvailable()).thenReturn(true);
        when(configuration.isLargeScreen()).thenReturn(true);

        fixture = new AboutFragment();
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
        verify(configuration).isSizeAvailable();
        verify(configuration).isLargeScreen();
        verifyNoMoreInteractions(configuration);
    }

    @Test
    public void testOnCreateView() {
        // execute
        SupportFragmentTestUtil.startFragment(fixture);
        // validate
        assertNotNull(fixture.getView());
    }

    @Test
    public void testVersionNumber() {
        // setup
        String expected = BuildConfig.VERSION_NAME + " - " + BuildConfig.VERSION_CODE + "SL"
            + " (" + Build.VERSION.RELEASE + "-" + Build.VERSION.SDK_INT + ")";
        SupportFragmentTestUtil.startFragment(fixture);
        // execute
        TextView actual = fixture.getView().findViewById(R.id.about_version_info);
        // validate
        assertNotNull(actual);
        assertEquals(expected, actual.getText());
    }

    @Test
    public void testPackageName() {
        // setup
        SupportFragmentTestUtil.startFragment(fixture);
        // execute
        TextView actual = fixture.getView().findViewById(R.id.about_package_name);
        // validate
        assertNotNull(actual);
        assertEquals(BuildConfig.APPLICATION_ID, actual.getText());
    }

    @Test
    public void testApplicationName() {
        // setup
        SupportFragmentTestUtil.startFragment(fixture);
        String expectedName = fixture.getString(R.string.app_full_name);
        // execute
        TextView actual = fixture.getView().findViewById(R.id.about_application_name);
        // validate
        assertNotNull(actual);
        assertEquals(expectedName, actual.getText());
    }

    @Test
    public void testCopyright() {
        // setup
        SupportFragmentTestUtil.startFragment(fixture);
        String expectedName = fixture.getString(R.string.app_copyright)
            + new SimpleDateFormat("yyyy").format(new Date());
        // execute
        TextView actual = fixture.getView().findViewById(R.id.about_copyright);
        // validate
        assertNotNull(actual);
        assertEquals(expectedName, actual.getText());
    }

    @Test
    public void testWriteReview() {
        // setup
        SupportFragmentTestUtil.startFragment(fixture);
        View actual = fixture.getView().findViewById(R.id.writeReview);
        // execute
        fixture.writeReview(actual);
        // validate
        assertNotNull(actual);
    }

    @Test
    public void testShowALv2() {
        // setup
        SupportFragmentTestUtil.startFragment(fixture);
        View view = fixture.getView().findViewById(R.id.license);
        // execute
        fixture.showALv2(view);
        // validate
        AlertDialog alertDialog = fixture.getAlertDialog();
        assertNotNull(alertDialog);
        validateMessage(fixture, alertDialog, R.raw.al);
        validateTitle(fixture, alertDialog, R.string.al);
    }

    @Test
    public void testShowGPLv3() {
        // setup
        SupportFragmentTestUtil.startFragment(fixture);
        View view = fixture.getView().findViewById(R.id.license);
        // execute
        fixture.showGPLv3(view);
        // validate
        AlertDialog alertDialog = fixture.getAlertDialog();
        assertNotNull(alertDialog);
        validateMessage(fixture, alertDialog, R.raw.gpl);
        validateTitle(fixture, alertDialog, R.string.gpl);
    }

    @Test
    public void testShowContributors() {
        // setup
        SupportFragmentTestUtil.startFragment(fixture);
        View view = fixture.getView().findViewById(R.id.contributors);
        // execute
        fixture.showContributors(view);
        // validate
        AlertDialog alertDialog = fixture.getAlertDialog();
        assertNotNull(alertDialog);
        validateMessage(fixture, alertDialog, R.raw.contributors);
        validateTitle(fixture, alertDialog, R.string.about_contributor_title);
    }

    private void validateMessage(@NonNull AboutFragment aboutFragment, @NonNull AlertDialog alertDialog, @RawRes int id) {
        String expected = FileUtils.readFile(aboutFragment.getResources(), id);
        TextView messageView = alertDialog.findViewById(android.R.id.message);
        assertEquals(expected, messageView.getText().toString());
    }

    private void validateTitle(@NonNull AboutFragment aboutFragment, @NonNull AlertDialog alertDialog, @RawRes int id) {
        String expected = aboutFragment.getResources().getString(id);
        int titleId = aboutFragment.getResources().getIdentifier("alertTitle", "id", "android");
        TextView titleView = alertDialog.findViewById(titleId);
        assertEquals(expected, titleView.getText().toString());
    }

}