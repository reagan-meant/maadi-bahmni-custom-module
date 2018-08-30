package org.bahmni.module.bahmnimaadi;

import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.Provider;
import org.openmrs.module.appointments.model.Appointment;
import org.openmrs.module.appointments.model.AppointmentServiceType;
import org.openmrs.module.appointments.web.contract.AppointmentDefaultResponse;
import org.openmrs.module.appointments.web.extension.AppointmentResponseExtension;
import org.openmrs.module.appointments.web.mapper.AppointmentMapper;
import org.openmrs.module.appointments.web.mapper.AppointmentServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Primary
@Component
public class AppointmentMapperOverride extends AppointmentMapper {

    @Autowired
    AppointmentServiceMapper appointmentServiceMapper;

    @Autowired(required = false)
    AppointmentResponseExtension appointmentResponseExtension;

    public List<AppointmentDefaultResponse> constructResponse(List<Appointment> appointments) {
        return appointments.stream().map(as -> this.mapToDefaultResponse(as, new AppointmentDefaultResponse())).collect(Collectors.toList());
    }

    public AppointmentDefaultResponse constructResponse(Appointment appointment) {
        return this.mapToDefaultResponse(appointment, new AppointmentDefaultResponse());
    }

    private AppointmentDefaultResponse mapToDefaultResponse(Appointment a, AppointmentDefaultResponse response) {
        response.setUuid(a.getUuid());
        response.setAppointmentNumber(a.getAppointmentNumber());
        response.setPatient(createPatientMap(a.getPatient()));
        response.setService(appointmentServiceMapper.constructDefaultResponse(a.getService()));
        response.setServiceType(createServiceTypeMap(a.getServiceType()));
        response.setProvider(createProviderMap(a.getProvider()));
        response.setLocation(createLocationMap(a.getLocation()));
        response.setStartDateTime(a.getStartDateTime());
        response.setEndDateTime(a.getEndDateTime());
        response.setAppointmentKind(a.getAppointmentKind().name());
        response.setStatus(a.getStatus().name());
        response.setComments(a.getComments());
        if (appointmentResponseExtension != null)
            response.setAdditionalInfo(appointmentResponseExtension.run(a));
        return response;
    }

    private Map createServiceTypeMap(AppointmentServiceType s) {
        Map serviceTypeMap = null;
        if (s != null) {
            serviceTypeMap = new HashMap();
            serviceTypeMap.put("name", s.getName());
            serviceTypeMap.put("uuid", s.getUuid());
            serviceTypeMap.put("duration", s.getDuration());
        }
        return serviceTypeMap;
    }

    private Map createProviderMap(Provider p) {
        Map providerMap = null;
        if (p != null) {
            providerMap = new HashMap();
            providerMap.put("name", p.getName());
            providerMap.put("uuid", p.getUuid());
        }
        return providerMap;
    }

    private Map createLocationMap(Location l) {
        Map locationMap = null;
        if (l != null) {
            locationMap = new HashMap();
            locationMap.put("name", l.getName());
            locationMap.put("uuid", l.getUuid());
        }
        return locationMap;
    }

    private Map createPatientMap(Patient p) {
        Map map = new HashMap();
        map.put("name", p.getPersonName().getGivenName());
        map.put("uuid", p.getUuid());
        map.put("identifier", p.getPatientIdentifier().getIdentifier());
        return map;
    }
}
