package tw.edu.fju.miniclinic.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import tw.edu.fju.miniclinic.model.Doctor;
import tw.edu.fju.miniclinic.model.DoctorRepository;
import tw.edu.fju.miniclinic.model.PasswordForm;

@Controller
public class PasswordController {

    @Autowired
    private DoctorRepository doctorRepo;

    @GetMapping("/password")
    public String showPasswordForm(Model model) {
        if (!model.containsAttribute("passwordForm")) {
            model.addAttribute("passwordForm", new PasswordForm());
        }
        return "password";
    }

    @PostMapping("/password")
    public String updatePassword(
            @Valid @ModelAttribute("passwordForm") PasswordForm form,
            BindingResult result,
            HttpSession session,
            Model model) {

        // 1. 檢查基本格式驗證 (如：是否空白、新密碼長度是否至少 8 碼)
        if (result.hasErrors()) {
            return "password";
        }

        // 2. 檢查兩次新密碼是否一致，若不一致則手動加入錯誤訊息
        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.confirmPassword", "兩次輸入的新密碼不一致");
            return "password";
        }

        // 3. 驗證舊密碼是否正確
        String doctorId = (String) session.getAttribute("loggedInDoctorId");
        Doctor doctor = doctorRepo.findById(doctorId).orElse(null);
        
        if (doctor == null || !BCrypt.checkpw(form.getOldPassword(), doctor.getPasswordHash())) {
            result.rejectValue("oldPassword", "error.oldPassword", "舊密碼錯誤");
            return "password";
        }

        // 4. 驗證通過，將新密碼進行雜湊並儲存
        doctor.setPasswordHash(BCrypt.hashpw(form.getNewPassword(), BCrypt.gensalt()));
        doctorRepo.save(doctor);

        return "redirect:/dashboard";
    }
}