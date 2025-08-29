package com.vrem.wifianalyzer.wifi.model

import android.net.wifi.ScanResult
import android.net.wifi.ScanResult.InformationElement
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class FastRoamingMockTest {
    private val scanResult: ScanResult = mock()
    private val informationElement: InformationElement = mock()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(scanResult, informationElement)
    }

    @Test
    fun findShouldReturnEmptyListWhenIdOfInformationElementThrowsException() {
        // setup
        doThrow(RuntimeException("Test exception")).whenever(informationElement).id
        doReturn(listOf(informationElement)).whenever(scanResult).informationElements
        // execute
        val result = FastRoaming.find(scanResult)
        // validate
        assertThat(result).isEmpty()
        verify(scanResult).informationElements
        verify(informationElement, times(FastRoaming.entries.size)).id
        verify(informationElement, never()).bytes
    }

    @Test
    fun findShouldReturnEmptyListWhenBytesInformationElementThrowsException() {
        // setup
        doReturn(0).whenever(informationElement).id
        doThrow(RuntimeException("Test exception")).whenever(informationElement).bytes
        doReturn(listOf(informationElement)).whenever(scanResult).informationElements
        // execute
        val result = FastRoaming.find(scanResult)
        // validate
        assertThat(result).isEmpty()
        verify(scanResult).informationElements
        verify(informationElement, times(FastRoaming.entries.size)).id
        verify(informationElement, times(FastRoaming.entries.size)).bytes
    }
}
