package org.ies.deti.ua.Ex2;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee createEmployee(Employee emp) {
        return employeeRepository.save(emp);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        return optionalEmployee.get();
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee updateEmployee(Employee emp) {
        Employee existingEmp = employeeRepository.findById(emp.getId()).get();
        existingEmp.setName(emp.getName());
        existingEmp.setEmail(emp.getEmail());
        Employee updatedEmp = employeeRepository.save(existingEmp);
        return updatedEmp;
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public Employee findByEmail(String email){
        return employeeRepository.findByEmail(email);
    }
}

