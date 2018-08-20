package org.bahmni.module.bahmnimaadi;

import org.bahmni.module.bahmnimaadi.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.Patient;
import org.openmrs.Visit;
import org.openmrs.VisitType;
import org.openmrs.api.context.Context;
import org.openmrs.module.appointments.model.Appointment;
import org.openmrs.module.appointments.service.AppointmentsService;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.openmrs.module.appointments.model.AppointmentStatus.Cancelled;
import static org.openmrs.module.appointments.model.AppointmentStatus.CheckedIn;
import static org.openmrs.module.appointments.model.AppointmentStatus.Scheduled;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class, DateUtil.class})
public class OpenmrsVisitAdviceTest {

    @Mock
    private AppointmentsService appointmentsService;

    @Mock
    private Patient patient;

    @Mock
    private Patient anotherPatient;

    @Mock
    private Visit visit;

    @Mock
    private VisitType visitType;

    @Mock
    private Appointment appointment;

    @Mock
    private Appointment anotherAppointment;

    @Mock
    private Appointment otherAppointment;

    private OpenmrsVisitAdvice visitAdvice;

    @Before
    public void setUp() {
        mockStatic(Context.class);
        when(visit.getVisitType()).thenReturn(visitType);
        when(visitType.getName()).thenReturn("MDT");
        visitAdvice = new OpenmrsVisitAdvice();
    }

    @Test
    public void shouldCheckInCurrentPatientsScheduledAppointmentsIfMdtVisitType() throws NoSuchMethodException {
        mockStatic(DateUtil.class);
        when(Context.getService(AppointmentsService.class)).thenReturn(appointmentsService);
        Date startOfDay = new Date();
        when(DateUtil.startOfDay()).thenReturn(startOfDay);
        when(appointmentsService.getAllAppointments(any())).thenReturn(Arrays.asList(appointment, anotherAppointment, otherAppointment));
        String patientUuid = "patientUuid";
        when(patient.getUuid()).thenReturn(patientUuid);
        when(visit.getPatient()).thenReturn(patient);
        when(visit.getVisitType()).thenReturn(visitType);
        when(visitType.getName()).thenReturn("MDT");
        when(appointment.getPatient()).thenReturn(patient);
        when(appointment.getStatus()).thenReturn(Cancelled);
        when(anotherAppointment.getPatient()).thenReturn(anotherPatient);
        when(anotherAppointment.getStatus()).thenReturn(Scheduled);
        when(otherAppointment.getPatient()).thenReturn(patient);
        when(otherAppointment.getStatus()).thenReturn(Scheduled);

        visitAdvice.afterReturning(null, this.getClass().getMethod("saveVisit"), new Object[]{visit}, null);

        verifyStatic();
        Context.getService(AppointmentsService.class);
        verifyStatic();
        DateUtil.startOfDay();
        verify(appointmentsService).getAllAppointments(startOfDay);
        verify(visit).getPatient();
        verify(visit).getVisitType();
        verify(visitType).getName();
        verify(patient, times(3)).getUuid();
        verify(anotherPatient, times(1)).getUuid();
        verify(appointment, times(1)).getPatient();
        verify(appointment, times(1)).getStatus();
        verify(anotherAppointment, times(1)).getPatient();
        verify(anotherAppointment, never()).getStatus();
        verify(otherAppointment, times(1)).getPatient();
        verify(otherAppointment, times(1)).getStatus();
        verify(appointmentsService, times(1))
                .changeStatus(eq(otherAppointment), eq(CheckedIn.toString()), any(Date.class));
        verify(appointmentsService, never())
                .changeStatus(eq(appointment), eq(CheckedIn.toString()), any(Date.class));
        verify(appointmentsService, never())
                .changeStatus(eq(anotherAppointment), eq(CheckedIn.toString()), any(Date.class));
    }

    @Test
    public void shouldNotDoCheckInIfSaveVisitMethodNotCalled() throws NoSuchMethodException {

        visitAdvice.afterReturning(null, this.getClass().getMethod("otherThanSave"), new Object[]{visit}, null);

        verifyStatic(never());
        Context.getService(AppointmentsService.class);
    }


    @Test
    public void shouldNotDoCheckInIfVisitTypeIsNotMdt() throws NoSuchMethodException {
        when(visitType.getName()).thenReturn("NonMDT");

        visitAdvice.afterReturning(null, this.getClass().getMethod("saveVisit"), new Object[]{visit}, null);

        verifyStatic(never());
        Context.getService(AppointmentsService.class);
    }

    public void otherThanSave() {
    }

    public void saveVisit() {
    }
}
