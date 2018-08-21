package org.bahmni.module.bahmnimaadi;

import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.module.appointments.model.Appointment;
import org.openmrs.module.appointments.service.AppointmentsService;
import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.bahmni.module.bahmnimaadi.util.DateUtil.startOfDay;
import static org.openmrs.module.appointments.model.AppointmentStatus.CheckedIn;
import static org.openmrs.module.appointments.model.AppointmentStatus.Scheduled;

public class OpenmrsVisitAdvice implements AfterReturningAdvice {

    private static final String SAVE_VISIT_METHOD_NAME = "saveVisit";
    private static final String MDT_VISIT_TYPE = "MDT";

    public void afterReturning(Object returnValue, Method method, Object[] parameters, Object o1) {
        if (!SAVE_VISIT_METHOD_NAME.equals(method.getName()) || isNull(returnValue)) {
            return;
        }
        Visit visit = (Visit) returnValue;
        if (isVisitForPastDate(visit) || !MDT_VISIT_TYPE.equals(visit.getVisitType().getName())) {
            return;
        }
        checkInScheduledAppointments(visit);
    }

    private boolean isVisitForPastDate(Visit visit) {
        return visit.getStartDatetime().before(startOfDay());
    }

    private void checkInScheduledAppointments(Visit visit) {
        AppointmentsService appointmentsService = Context.getService(AppointmentsService.class);
        List<Appointment> allAppointments = appointmentsService.getAllAppointments(startOfDay());
        String patientUuid = visit.getPatient().getUuid();
        List<Appointment> patientAppointments = getScheduledPatientAppointments(allAppointments, patientUuid);
        Date checkInDate = new Date();
        patientAppointments.forEach(appointment ->
                appointmentsService.changeStatus(appointment, CheckedIn.toString(), checkInDate));
    }

    private List<Appointment> getScheduledPatientAppointments(List<Appointment> allAppointments, String patientUuid) {
        return allAppointments.stream()
                .filter(appointment -> patientUuid.equals(appointment.getPatient().getUuid()))
                .filter(appointment -> appointment.getStatus().equals(Scheduled))
                .collect(Collectors.toList());
    }

}