package org.ies.deti.ua.Ex2;

import java.util.List;

public interface EmployeeService {
    Employee createEmployee(Employee emp);

    Employee getEmployeeById(Long id);

    List<Employee> getAllEmployees();

    Employee updateEmployee(Employee emp);

    void deleteEmployee(Long id);

    Employee findByEmail(String email);
}
