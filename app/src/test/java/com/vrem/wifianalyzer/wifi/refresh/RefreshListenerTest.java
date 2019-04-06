package com.vrem.wifianalyzer.wifi.refresh;

import com.vrem.util.BuildUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BuildUtils.class)
public class RefreshListenerTest {
    @Mock
    private RefreshAction refreshAction;

    private RefreshListener fixture;

    @Before
    public void setUp() {
        mockStatic(BuildUtils.class);
        fixture = new RefreshListener(refreshAction);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(BuildUtils.class);
        verifyNoMoreInteractions(refreshAction);
    }

    @Test
    public void onRefresh() {
        // setup
        when(BuildUtils.isMinVersionP()).thenReturn(false);
        // execute
        fixture.onRefresh();
        // validate
        verify(refreshAction).refresh();
        verifyStatic(BuildUtils.class);
        BuildUtils.isMinVersionP();
    }

    @Test
    public void onRefreshWithAndroidPPlus() {
        // setup
        when(BuildUtils.isMinVersionP()).thenReturn(true);
        // execute
        fixture.onRefresh();
        // validate
        verifyStatic(BuildUtils.class);
        BuildUtils.isMinVersionP();
    }
}