package org.bahmni.module.bahmnimaadi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.Person;
import org.openmrs.PersonName;
import org.openmrs.module.appointments.model.Appointment;
import org.openmrs.module.appointments.model.AppointmentKind;
import org.openmrs.module.appointments.web.contract.AppointmentDefaultResponse;
import org.openmrs.module.appointments.web.mapper.AppointmentServiceMapper;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentMapperOverrideTest {

    @InjectMocks
    private AppointmentMapperOverride appointmentMapperOverride;

    @Mock
    private AppointmentServiceMapper appointmentServiceMapper;

    @Test
    public void shouldContainOnlyGivenNameOfPatientInAllAppointments() {
        Appointment appointment = new Appointment();
        Person person = new Person();
        PersonName personName = new PersonName();
        personName.setGivenName("givenName");
        personName.setMiddleName("middleName");
        personName.setFamilyName("familyName");
        person.setNames(new HashSet<>(Collections.singletonList(personName)));
        Patient patient = new Patient(person);
        patient.setIdentifiers(new HashSet<>(Collections.singletonList(new PatientIdentifier())));
        appointment.setPatient(patient);
        appointment.setAppointmentKind(AppointmentKind.Scheduled);

        List<AppointmentDefaultResponse> response = appointmentMapperOverride
                .constructResponse(Collections.singletonList(appointment));

        String actualName = (String) response.get(0).getPatient().get("name");
        assertEquals(personName.getGivenName(), actualName);
    }
}