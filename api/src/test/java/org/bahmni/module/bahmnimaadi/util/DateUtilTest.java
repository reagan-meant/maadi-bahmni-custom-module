package org.bahmni.module.bahmnimaadi.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Calendar;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DateUtil.class)
public class DateUtilTest {

    @Mock
    private Calendar calendar;

    @Test
    public void shouldGetStartOfDay() {
        mockStatic(Calendar.class);
        when(Calendar.getInstance()).thenReturn(calendar);
        when(calendar.get(YEAR)).thenReturn(2018);
        when(calendar.get(MONTH)).thenReturn(8);
        when(calendar.get(DATE)).thenReturn(16);

        DateUtil.startOfDay();

        verify(calendar).get(YEAR);
        verify(calendar).get(MONTH);
        verify(calendar).get(DATE);
        verify(calendar).set(2018, 8, 16, 0, 0, 0);
        verify(calendar).set(MILLISECOND, 0);
        verify(calendar).getTime();
    }
}
