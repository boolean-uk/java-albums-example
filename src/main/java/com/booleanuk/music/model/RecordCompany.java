package com.booleanuk.music.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "record_companies")
public class RecordCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "email")
    private String email;

    public RecordCompany(String name, String location, String email) {
        this.name = name;
        this.location = location;
        this.email = email;
    }

    public RecordCompany(int id) {
        this.id = id;
    }

}
