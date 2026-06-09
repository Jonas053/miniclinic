package tw.edu.fju.miniclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.ui.Model;

import tw.edu.fju.miniclinic.model.AppointmentRepository;
import tw.edu.fju.miniclinic.model.DoctorRepository;
import tw.edu.fju.miniclinic.model.PatientRepository;
import tw.edu.fju.miniclinic.model.Appointment;

import java.util.ArrayList;
import java.util.List;


@Controller
public class StatController {
    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @GetMapping("/stats")
    public String stats(Model model) {
        model.addAttribute("doctorCount", doctorRepo.count());
        model.addAttribute("patientCount", patientRepo.count());
        model.addAttribute("appointmentCount", appointmentRepo.count());
        model.addAttribute("departmentStats",departmentStats());//算了吧
        return "stats";
    }

    private List<String> departmentStats() {  //查有那些科別和算出各科目掛號數，並轉成字串陣列輸出
        List<String> departments =doctorRepo.findAllDepartments();
        List<Appointment> appointments =appointmentRepo.findAll();
        int[]counts=new int[departments.size()];
        for(Appointment appt:appointments){
            String dept=appt.getDoctor().getDepartment();
            for(int i=0;i<departments.size();i++){
                if(departments.get(i).equals(dept)){
                    counts[i]++;
                }
            }
        }
        // 將統計結果轉換為字串陣列
        List<String> stats = new ArrayList<>();
        for(int i=0;i<departments.size();i++){
            stats.add(departments.get(i) + ": " + counts[i]);
        }
        return stats;
    }
}

