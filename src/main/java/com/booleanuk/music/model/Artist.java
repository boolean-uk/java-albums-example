package com.booleanuk.music.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "artists")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "number_of_members")
    private int numberOfMembers;

    @Column(name = "still_performing")
    private boolean stillPerforming;

    @OneToMany(mappedBy = "artist")
    @JsonIgnoreProperties({"artist"})
    private List<Album> albums;

    public Artist(String name, int numberOfMembers, boolean stillPerforming) {
        this.name = name;
        this.numberOfMembers = numberOfMembers;
        this.stillPerforming = stillPerforming;
    }

    public Artist(int id) {
        this.id = id;
    }
}
