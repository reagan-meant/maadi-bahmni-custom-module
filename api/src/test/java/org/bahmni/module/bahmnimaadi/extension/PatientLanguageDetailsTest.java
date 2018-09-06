package org.bahmni.module.bahmnimaadi.extension;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.module.appointments.model.Appointment;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PatientLanguageDetailsTest {

    @Mock
    private Appointment appointment;

    @Mock
    private Patient patient;

    @Mock
    private PersonAttribute personAttribute;

    @Test
    public void shouldNotThrowExceptionIfLanguageAndOtherLanguageIsNull() {
        when(appointment.getPatient()).thenReturn(patient);
        when(patient.getAttribute("Language")).thenReturn(null);
        PatientLanguageDetails patientLanguageDetails = new PatientLanguageDetails();

        patientLanguageDetails.run(appointment);
    }

    @Test
    public void shouldReturnPatientLanguageAndOtherLanguageDetailsIfItExists() {
        when(appointment.getPatient()).thenReturn(patient);
        when(patient.getAttribute("Language"))
                .thenReturn(personAttribute);
        when(patient.getAttribute("Other Language"))
                .thenReturn(personAttribute);
        PatientLanguageDetails patientLanguageDetails = new PatientLanguageDetails();

        patientLanguageDetails.run(appointment);

        assertEquals(2, patientLanguageDetails.run(appointment).size());
    }
}
