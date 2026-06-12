package tw.edu.fju.miniclinic.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.edu.fju.miniclinic.model.AppointmentRepository;
import tw.edu.fju.miniclinic.model.DoctorRepository;
import tw.edu.fju.miniclinic.model.PatientRepository;

@RestController
public class StatsApiController {
    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @GetMapping("/api/stats")
    public Map<String, Object> getStats() {
        return Map.of(
            "status", "ok",

            "totalDoctors", String.valueOf(doctorRepo.count()),
            "totalPatients", String.valueOf(patientRepo.count()),
            "totalAppointments", String.valueOf(appointmentRepo.count()),
            
            "byStatus", Map.of(
                "scheduled", String.valueOf(appointmentRepo.countByStatus("BOOKED")),
                "completed", String.valueOf(appointmentRepo.countByStatus("COMPLETED")),
                "cancelled", String.valueOf(appointmentRepo.countByStatus("CANCELLED"))
            )
        );
    }
}
