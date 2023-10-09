package com.booleanuk.music.controller;

import com.booleanuk.music.model.Artist;
import com.booleanuk.music.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("artists")
public class ArtistController {
    @Autowired
    private ArtistRepository artistRepository;

    @GetMapping
    public List<Artist> getAllArtists() {
        return this.artistRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable int id) {
        Artist artist = null;
        artist = this.artistRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "That ID doesn't match any stored artists")
        );
        return ResponseEntity.ok(artist);
    }

    @PostMapping
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist) {
        return new ResponseEntity<>(this.artistRepository.save(artist), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artist> updateArtist(@PathVariable int id, @RequestBody Artist artist) {
        Artist artistToUpdate = this.artistRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Only existing artists can be updated")
        );
        artistToUpdate.setName(artistToUpdate.getName());
        artistToUpdate.setNumberOfMembers(artist.getNumberOfMembers());
        artistToUpdate.setStillPerforming(artist.isStillPerforming());
        return new ResponseEntity<>(this.artistRepository.save(artistToUpdate), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Artist> deleteArtist(@PathVariable int id) {
        Artist artistToDelete = this.artistRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Only existing artists can be deleted")
        );
        this.artistRepository.delete(artistToDelete);
        return ResponseEntity.ok(artistToDelete);
    }
}
