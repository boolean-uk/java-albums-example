package com.booleanuk.music.controller;

import com.booleanuk.music.model.Album;
import com.booleanuk.music.model.Artist;
import com.booleanuk.music.model.RecordCompany;
import com.booleanuk.music.repository.AlbumRepository;
import com.booleanuk.music.repository.ArtistRepository;
import com.booleanuk.music.repository.RecordCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("albums")
public class AlbumController {
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private RecordCompanyRepository recordCompanyRepository;

    @GetMapping
    public List<Album> getAllAlbums() {
        return this.albumRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable int id) {
        Album album = null;
        album = this.albumRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "That ID doesn't match any stored albums")
        );
        return ResponseEntity.ok(album);
    }

    @PostMapping
    public ResponseEntity<Album> createAlbum(@RequestBody Album album) {
        Artist theArtist = this.artistRepository.findById(album.getArtist().getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "I'm sorry that artist doesn't appear to exist.")
        );
        album.setArtist(theArtist);
        RecordCompany theCompany = this.recordCompanyRepository.findById(album.getRecordCompany().getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "I'm sorry that record company doesn't appear to exist.")
        );
        album.setRecordCompany(theCompany);
        return new ResponseEntity<>(this.albumRepository.save(album), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Album> updateAlbum(@PathVariable int id, @RequestBody Album album) {
        Album albumToUpdate = this.albumRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Only existing albums can be updated")
        );
        albumToUpdate.setTitle(album.getTitle());
        albumToUpdate.setYear(album.getYear());
        albumToUpdate.setRating(album.getRating());
        Artist theArtist = this.artistRepository.findById(album.getArtist().getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "I'm sorry that artist doesn't appear to exist.")
        );
        albumToUpdate.setArtist(theArtist);
        RecordCompany theCompany = this.recordCompanyRepository.findById(album.getRecordCompany().getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "I'm sorry that record company doesn't appear to exist.")
        );
        albumToUpdate.setRecordCompany(theCompany);
        return new ResponseEntity<>(this.albumRepository.save(albumToUpdate), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Album> deleteAlbum(@PathVariable int id) {
        Album albumToDelete = this.albumRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Only existing albums can be deleted")
        );
        this.albumRepository.delete(albumToDelete);
        return ResponseEntity.ok(albumToDelete);
    }
}
