package com.employee.controller;

import com.employee.entity.Employee;
import com.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @PostMapping(value = "/employees", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ResponseEntity saveEmployees(@RequestParam(value = "files") MultipartFile[] files) throws Exception {
        for (MultipartFile file : files) {
            service.saveEmployees(file);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/employees", produces = "application/json")
    public CompletableFuture<ResponseEntity> findAllEmployees() {
        return  service.findAllEmployees().thenApply(ResponseEntity::ok);
    }


    @GetMapping(value = "/getEmployeesByThread", produces = "application/json")
    public  ResponseEntity getEmployees(){
        CompletableFuture<List<Employee>> Employees1=service.findAllEmployees();
        CompletableFuture<List<Employee>> Employees2=service.findAllEmployees();
        CompletableFuture<List<Employee>> Employees3=service.findAllEmployees();
        CompletableFuture.allOf(Employees1,Employees2,Employees3).join();
        return  ResponseEntity.status(HttpStatus.OK).build();
    }
    
}
