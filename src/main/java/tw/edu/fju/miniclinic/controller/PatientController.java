package tw.edu.fju.miniclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

//import tw.edu.fju.miniclinic.model.Doctor;
//import tw.edu.fju.miniclinic.model.DoctorRepository;
import tw.edu.fju.miniclinic.model.Patient;
import tw.edu.fju.miniclinic.model.PatientRepository;

import java.util.List;
//import java.util.Optional;

@Controller
public class PatientController {

    @Autowired
    private PatientRepository patientRepo;

    @GetMapping("/patients")
    public String listPatients(
        @RequestParam(required = false) String gender, Model model) {

        List<Patient> patients;
        patients = patientRepo.findAll();

        model.addAttribute("patients", patients);
        model.addAttribute("selectedGender", gender);

        return "patients";
    }
    @GetMapping("/api/patients")
    @ResponseBody
    public List<Patient> getPatients(

        @RequestParam(required = false) String chartNo) {
        //if (chartNo == null || chartNo.isBlank()) {
            return patientRepo.findAll();
        //}
       // return patientRepo.findByChartNo(chartNo);
    }
    /*@GetMapping("/api/doctors")
    public List<Doctor> getDoctors(
            @RequestParam(required = false) String department) {
        if (department == null || department.isBlank()) {
            return doctorRepo.findAll();
        }
        return doctorRepo.findByDepartment(department);
    }*/
}