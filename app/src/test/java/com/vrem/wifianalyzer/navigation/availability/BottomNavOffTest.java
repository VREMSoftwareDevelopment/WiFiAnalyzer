package com.vrem.wifianalyzer.navigation.availability;

import android.view.View;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BottomNavOffTest {

    @Mock
    private MainActivity mainActivity;
    @Mock
    private View view;

    private BottomNavOff fixture;

    @Before
    public void setUp() {
        fixture = new BottomNavOff();
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(mainActivity);
        verifyNoMoreInteractions(view);
    }

    @Test
    public void testApply() {
        // setup
        when(mainActivity.findViewById(R.id.nav_bottom)).thenReturn(view);
        // execute
        fixture.apply(mainActivity);
        // validate
        verify(mainActivity).findViewById(R.id.nav_bottom);
        verify(view).setVisibility(View.GONE);
    }

}