package com.booleanuk.music.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "albums")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "year")
    private int year;

    @Column(name = "rating")
    private int rating;

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    @JsonIgnoreProperties("albums")
    private Artist artist;

    @ManyToOne
    @JoinColumn(name = "record_company_id", nullable = false)
    @JsonIgnoreProperties("recordCompany")
    private RecordCompany recordCompany;


    public Album(String title, int year, int rating) {
        this.title = title;
        this.year = year;
        this.rating = rating;
    }
}
