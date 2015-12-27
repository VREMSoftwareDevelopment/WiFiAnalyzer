package com.vrem.wifianalyzer;

import android.app.ActionBar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UtilsTest {
    @Mock
    private ActionBar actionBar;

    @Test
    public void testEnableHomeButtonSuccess() throws Exception {
        // execute
        Utils.enableHomeButton(actionBar);
        // validate
        verify(actionBar).setHomeButtonEnabled(true);
        verify(actionBar).setDisplayHomeAsUpEnabled(true);
        verify(actionBar).setHomeAsUpIndicator(R.mipmap.ic_launcher);
    }

    @Test
    public void testEnableHomeButtonFails() throws Exception {
        // execute
        Utils.enableHomeButton(null);
        // validate
        verify(actionBar, never()).setHomeButtonEnabled(true);
        verify(actionBar, never()).setDisplayHomeAsUpEnabled(true);
        verify(actionBar, never()).setHomeAsUpIndicator(R.mipmap.ic_launcher);
    }
}