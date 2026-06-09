package tw.edu.fju.miniclinic.controller;

import tw.edu.fju.miniclinic.model.Appointment;
import tw.edu.fju.miniclinic.model.AppointmentRepository;
import tw.edu.fju.miniclinic.model.DoctorRepository;

//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

//import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;
import java.time.LocalDate;
import java.util.List;

@RestController
public class AppointmentApiController {
    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @PostMapping("/api/appointments/{apptId}/status")
    public ResponseEntity<Appointment> updateStatus(
            @PathVariable Long apptId,
            @RequestParam String status,
            HttpSession session) {

        String loggedInDoctorId = (String) session.getAttribute("loggedInDoctorId");

        Appointment appt = appointmentRepo.findById(apptId).orElse(null);
        if (appt == null) {
            return ResponseEntity.notFound().build();
        }

        // 只能修改自己的掛號
        if (!appt.getDoctor().getDoctorId().equals(loggedInDoctorId)) {
            return ResponseEntity.status(403).build();
        }

        if (!List.of("BOOKED", "COMPLETED", "CANCELLED").contains(status)) {
            return ResponseEntity.badRequest().build();
        }

        appt.setStatus(status);
        return ResponseEntity.ok(appointmentRepo.save(appt));
    }

    @GetMapping("/api/appointments/count")
    public Map<String, String> countAppointments() {
        return Map.of(
            "status", "ok",
            "appointments", String.valueOf(appointmentRepo.count())
        );
    }

    @GetMapping(value = "/api/appointments", params = "date")
    public Map<String, String> appointmentsBydate(@RequestParam String date) {
        return Map.of(
            "status", "ok",
            "appointments", String.valueOf(appointmentRepo.findByApptDate(LocalDate.parse(date)).size())
        );
    }

    @GetMapping(value = "/api/appointments", params = "doctorId")
    public Map<String, String> appointmentsBydoctor(@RequestParam String doctorId) {
        return Map.of(
            "status", "ok",
            "appointments", String.valueOf(appointmentRepo.findByDoctor(doctorRepo.findById(doctorId).get()).size())
        );
    }
}