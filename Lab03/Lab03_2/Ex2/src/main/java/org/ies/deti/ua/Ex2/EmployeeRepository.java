package org.ies.deti.ua.Ex2;

import org.ies.deti.ua.Ex2.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{
    Employee findByEmail(String email);
}
