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

package com.vrem.util;

import android.content.res.Resources;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileUtilsTest {
    @Mock
    private Resources resources;

    @Test
    public void testReadFile() {
        // setup
        int id = 11;
        String expected = "Line-1\nLine-2\n";
        InputStream inputStream = new ByteArrayInputStream(expected.getBytes());
        when(resources.openRawResource(id)).thenReturn(inputStream);
        // execute
        String actual = FileUtils.readFile(resources, id);
        // validate
        assertEquals(expected, actual);
        verify(resources).openRawResource(id);
    }

    @Test
    public void testReadFileHandleException() {
        // setup
        int id = 11;
        when(resources.openRawResource(id)).thenThrow(Resources.NotFoundException.class);
        // execute
        String actual = FileUtils.readFile(resources, id);
        // validate
        assertEquals(StringUtils.EMPTY, actual);
        verify(resources).openRawResource(id);
    }

}