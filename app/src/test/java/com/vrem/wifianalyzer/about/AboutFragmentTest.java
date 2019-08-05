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

package com.vrem.wifianalyzer.about;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.vrem.util.FileUtils;
import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.Configuration;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;
import org.robolectric.shadows.ShadowAlertDialog;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.robolectric.annotation.LooperMode.Mode.PAUSED;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
@LooperMode(PAUSED)
public class AboutFragmentTest {

    private Configuration configuration;
    private AboutFragment fixture;
    private MainActivity mainActivity;

    @Before
    public void setUp() {
        mainActivity = RobolectricUtil.INSTANCE.getActivity();
        configuration = MainContextHelper.INSTANCE.getConfiguration();
        when(configuration.isSizeAvailable()).thenReturn(true);
        when(configuration.isLargeScreen()).thenReturn(true);

        fixture = new AboutFragment();
        RobolectricUtil.INSTANCE.startFragment(fixture);
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
        assertNotNull(fixture.getView());
    }

    @Test
    public void testVersionNumber() {
        // setup
        String expected = BuildConfig.VERSION_NAME + " - " + BuildConfig.VERSION_CODE + "SL"
            + " (" + Build.VERSION.RELEASE + "-" + Build.VERSION.SDK_INT + ")";
        // execute
        TextView actual = fixture.getView().findViewById(R.id.about_version_info);
        // validate
        assertNotNull(actual);
        assertEquals(expected, actual.getText());
    }

    @Test
    public void testPackageName() {
        // execute
        TextView actual = fixture.getView().findViewById(R.id.about_package_name);
        // validate
        assertNotNull(actual);
        assertEquals(BuildConfig.APPLICATION_ID, actual.getText());
    }

    @Test
    public void testApplicationName() {
        // setup
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
        String url = "market://details?id=" + BuildConfig.APPLICATION_ID;
        Intent expectedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        View view = fixture.getView().findViewById(R.id.writeReview);
        // execute
        view.performClick();
    }

    @Test
    public void testAlertDialogClickListener() {
        validateAlertDialogClickListener(R.id.contributors, R.string.about_contributor_title, R.raw.contributors);
        validateAlertDialogClickListener(R.id.license, R.string.gpl, R.raw.gpl);
        validateAlertDialogClickListener(R.id.apacheCommonsLicense, R.string.al, R.raw.al);
        validateAlertDialogClickListener(R.id.graphViewLicense, R.string.al, R.raw.al);
        validateAlertDialogClickListener(R.id.materialDesignIconsLicense, R.string.al, R.raw.al);
    }

    private void validateAlertDialogClickListener(int viewId, int titleId, int messageId) {
        // setup
        View view = fixture.getView().findViewById(viewId);
        String expectedTitle = mainActivity.getApplicationContext().getString(titleId);
        String expectedMessage = FileUtils.readFile(mainActivity.getResources(), messageId);
        // execute
        view.performClick();
        // validate
        AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
        ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(alertDialog);
        assertEquals(expectedTitle, shadowAlertDialog.getTitle().toString());
        assertEquals(expectedMessage, shadowAlertDialog.getMessage().toString());
    }

}