package org.bahmni.module.bahmnimaadi;

import org.bahmni.module.bahmnimaadi.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.api.context.Context;
import org.openmrs.module.appointments.service.AppointmentsService;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class, DateUtil.class})
public class OpenmrsVisitAdviceTest {

    @Mock
    private AppointmentsService appointmentsService;

    @Test
    public void shouldGetTodayAppointments() {
        mockStatic(Context.class);
        mockStatic(DateUtil.class);
        when(Context.getService(AppointmentsService.class)).thenReturn(appointmentsService);
        OpenmrsVisitAdvice visitAdvice = new OpenmrsVisitAdvice();

        visitAdvice.afterReturning(null, null, null, null);

        verifyStatic();
        Context.getService(AppointmentsService.class);
        verifyStatic();
        DateUtil.startOfDay();
        verify(appointmentsService).getAllAppointments(DateUtil.startOfDay());
    }

}
