package org.bahmni.module.bahmnimaadi.extension;

import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.module.appointments.model.Appointment;
import org.openmrs.module.appointments.web.extension.AppointmentResponseExtension;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PatientLanguageDetails implements AppointmentResponseExtension {

    @Override
    public Map<String, String> run(Appointment appointment) {
        Map<String, String> patientLanguageDetails = new HashMap<>();
        Patient patient = appointment.getPatient();
        PersonAttribute language = patient.getAttribute("Language");
        PersonAttribute otherLanguage = patient.getAttribute("Other Language");
        if (language != null) {
            patientLanguageDetails.put("language", language.toString());
        }
        if (otherLanguage != null) {
            patientLanguageDetails.put("otherLanguage", otherLanguage.toString());
        }
        return patientLanguageDetails;
    }

}
