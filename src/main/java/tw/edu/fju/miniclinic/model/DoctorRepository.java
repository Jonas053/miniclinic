package tw.edu.fju.miniclinic.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

//import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, String> {

    List<Doctor> findAll() ;

    Optional<Doctor> findById(String doctorId) ;

    List<Doctor> findByDepartment(String department) ;
    
    @Query("SELECT DISTINCT d.department FROM Doctor d ORDER BY d.department")
    List<String> findAllDepartments() ;
}
