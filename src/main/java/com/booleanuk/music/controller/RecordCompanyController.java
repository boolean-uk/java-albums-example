package com.booleanuk.music.controller;

import com.booleanuk.music.model.RecordCompany;
import com.booleanuk.music.repository.RecordCompanyRepository;
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
    public List<RecordCompany> getAllRecordCompanies() {
        return this.recordCompanyRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecordCompany> getRecordCompanyById(@PathVariable int id) {
        RecordCompany company = null;
        company = this.recordCompanyRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "That ID doesn't match any stored record company")
        );
        return ResponseEntity.ok(company);
    }

    @PostMapping
    public ResponseEntity<RecordCompany> createRecordCompany(@RequestBody RecordCompany company) {
        return new ResponseEntity<>(this.recordCompanyRepository.save(company), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecordCompany> updateRecordCompany(@PathVariable int id, @RequestBody RecordCompany company) {
        RecordCompany companyToRename = this.recordCompanyRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Only existing record companies can be updated")
        );
        companyToRename.setName(company.getName());
        companyToRename.setLocation(company.getLocation());
        companyToRename.setEmail(company.getEmail());
        return new ResponseEntity<>(this.recordCompanyRepository.save(companyToRename), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RecordCompany> deleteRecordCompany(@PathVariable int id) {
        RecordCompany companyToDelete = this.recordCompanyRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Only existing record companies can be deleted")
        );
        this.recordCompanyRepository.delete(companyToDelete);
        return ResponseEntity.ok(companyToDelete);
    }
}
