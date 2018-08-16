package org.bahmni.module.bahmnimaadi;

import org.openmrs.api.context.Context;
import org.openmrs.module.appointments.model.Appointment;
import org.openmrs.module.appointments.service.AppointmentsService;
import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;
import java.util.List;

import static org.bahmni.module.bahmnimaadi.util.DateUtil.startOfDay;

public class OpenmrsVisitAdvice implements AfterReturningAdvice {

    public void afterReturning(Object returnValue, Method method, Object[] parameters, Object o1) {
        AppointmentsService appointmentsService = Context.getService(AppointmentsService.class);
        List<Appointment> allAppointments = appointmentsService.getAllAppointments(startOfDay());
    }

}