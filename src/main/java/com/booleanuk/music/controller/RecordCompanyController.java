package com.booleanuk.music.controller;

import com.booleanuk.music.model.RecordCompany;
import com.booleanuk.music.repository.RecordCompanyRepository;
import com.booleanuk.music.response.ErrorResponse;
import com.booleanuk.music.response.RecordCompanyListResponse;
import com.booleanuk.music.response.RecordCompanyResponse;
import com.booleanuk.music.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("companies")
public class RecordCompanyController {
    @Autowired
    private RecordCompanyRepository recordCompanyRepository;

    @GetMapping
    public ResponseEntity<RecordCompanyListResponse> getAllRecordCompanies() {
        RecordCompanyListResponse recordCompanyListResponse = new RecordCompanyListResponse();
        recordCompanyListResponse.set(this.recordCompanyRepository.findAll());
        return ResponseEntity.ok(recordCompanyListResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getRecordCompanyById(@PathVariable int id) {
        RecordCompany company = this.recordCompanyRepository.findById(id).orElse(null);
        if (company == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        RecordCompanyResponse recordCompanyResponse = new RecordCompanyResponse();
        recordCompanyResponse.set(company);
        return ResponseEntity.ok(recordCompanyResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createRecordCompany(@RequestBody RecordCompany company) {
        RecordCompanyResponse recordCompanyResponse = new RecordCompanyResponse();
        try {
            recordCompanyResponse.set(this.recordCompanyRepository.save(company));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(recordCompanyResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateRecordCompany(@PathVariable int id, @RequestBody RecordCompany company) {
        RecordCompany companyToUpdate = null;
        try {
            companyToUpdate = this.recordCompanyRepository.findById(id).orElse(null);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        if (companyToUpdate == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        companyToUpdate.setName(company.getName());
        companyToUpdate.setLocation(company.getLocation());
        companyToUpdate.setEmail(company.getEmail());
        companyToUpdate = this.recordCompanyRepository.save(companyToUpdate);
        RecordCompanyResponse recordCompanyResponse = new RecordCompanyResponse();
        recordCompanyResponse.set(companyToUpdate);
        return new ResponseEntity<>(recordCompanyResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteRecordCompany(@PathVariable int id) {
        RecordCompany companyToDelete = this.recordCompanyRepository.findById(id).orElse(null);
        if (companyToDelete == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        this.recordCompanyRepository.delete(companyToDelete);
        RecordCompanyResponse recordCompanyResponse = new RecordCompanyResponse();
        recordCompanyResponse.set(companyToDelete);
        return ResponseEntity.ok(recordCompanyResponse);
    }
}
