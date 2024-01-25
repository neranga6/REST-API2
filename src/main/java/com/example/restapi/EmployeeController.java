package com.example.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Value("${custom.header.content-type}")
    private String contentType;

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable Long id) {
        try {

            Employee employee = employeeRepository.findById(id).orElse(null);

            if (employee != null) {

                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.set("Application", contentType);
                responseHeaders.set("Authorization", authorizationHeader);

                return new ResponseEntity<>(employee, responseHeaders, HttpStatus.OK);
            } else {

                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee,@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        try {
            Employee savedEmployee = employeeRepository.save(employee);


            HttpHeaders responseHeaders = new HttpHeaders();

            responseHeaders.set("Application", contentType);
            responseHeaders.set("Authorization", authorizationHeader);


            return new ResponseEntity<>(savedEmployee, responseHeaders, HttpStatus.CREATED);
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
