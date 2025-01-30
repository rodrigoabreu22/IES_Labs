package org.ies.deti.ua.Ex2;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/employees")
public class EmployeeController {
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee emp){
        Employee savedEmp = employeeService.createEmployee(emp);
        return new ResponseEntity<>(savedEmp, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<Employee>> getAllEmployees(@RequestParam(required=false) String email){
        List<Employee> employees;
        if (email==null){
            employees = employeeService.getAllEmployees();
        }
        else {
            employees = new ArrayList<>();
            employees.add(employeeService.findByEmail(email));
        }
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") long id){
        Employee emp = employeeService.getEmployeeById(id);
        return new ResponseEntity<>(emp, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") long id,  @RequestBody Employee emp){
        emp.setId(id);
        Employee updatedEmp = employeeService.updateEmployee(emp);
        return new ResponseEntity<>(updatedEmp, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable("id") long id){
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
