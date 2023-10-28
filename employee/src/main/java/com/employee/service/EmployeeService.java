package com.employee.service;

import com.employee.entity.Employee;
import com.employee.repo.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepo repo;

    Object target;

    @Async
    public CompletableFuture<List<Employee>> saveEmployees(MultipartFile file) throws Exception {
        long start = System.currentTimeMillis();
        List<Employee> Employees = parseCSVFile(file);
        Employees = repo.saveAll(Employees);
        long end = System.currentTimeMillis();
        return CompletableFuture.completedFuture(Employees);
    }
    @Async
    public CompletableFuture<List<Employee>> findAllEmployees(){
        List<Employee> Employees = repo.findAll();
        return CompletableFuture.completedFuture(Employees);
    }

    private List<Employee> parseCSVFile(final MultipartFile file) throws Exception {
        final List<Employee> Employees = new ArrayList<>();
        try {
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    final String[] data = line.split(",");
                    final Employee Employee = new Employee();
                    Employee.setName(data[0]);
                    Employee.setEmail(data[1]);
                    Employee.setGender(data[2]);
                    Employees.add(Employee);
                }
                return Employees;
            }
        } catch (final IOException e) {
            throw new Exception("Failed to parse CSV file {}", e);
        }
    }
    
}
