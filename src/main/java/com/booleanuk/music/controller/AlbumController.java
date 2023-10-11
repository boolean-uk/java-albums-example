package com.booleanuk.music.controller;

import com.booleanuk.music.model.Album;
import com.booleanuk.music.model.Artist;
import com.booleanuk.music.model.RecordCompany;
import com.booleanuk.music.repository.AlbumRepository;
import com.booleanuk.music.repository.ArtistRepository;
import com.booleanuk.music.repository.RecordCompanyRepository;
import com.booleanuk.music.response.AlbumListResponse;
import com.booleanuk.music.response.AlbumResponse;
import com.booleanuk.music.response.ErrorResponse;
import com.booleanuk.music.response.Response;
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
    public ResponseEntity<AlbumListResponse> getAllAlbums() {
        AlbumListResponse albumListResponse = new AlbumListResponse();
        albumListResponse.set(this.albumRepository.findAll());
        return ResponseEntity.ok(albumListResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getAlbumById(@PathVariable int id) {
        Album album = this.albumRepository.findById(id).orElse(null);
        if (album == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        AlbumResponse albumResponse = new AlbumResponse();
        albumResponse.set(album);
        return ResponseEntity.ok(albumResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createAlbum(@RequestBody Album album) {
        Artist theArtist = this.artistRepository.findById(album.getArtist().getId()).orElse(null);
        if (theArtist == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("artist not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        album.setArtist(theArtist);
        RecordCompany theCompany = this.recordCompanyRepository.findById(album.getRecordCompany().getId()).orElse(null);
        if (theCompany == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("record company not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        album.setRecordCompany(theCompany);
        AlbumResponse albumResponse = new AlbumResponse();
        try {
            albumResponse.set(this.albumRepository.save(album));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(albumResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateAlbum(@PathVariable int id, @RequestBody Album album) {
        Album albumToUpdate = this.albumRepository.findById(id).orElse(null);
        if (albumToUpdate == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

        }
        albumToUpdate.setTitle(album.getTitle());
        albumToUpdate.setYear(album.getYear());
        albumToUpdate.setRating(album.getRating());

        Artist theArtist = null;
        try {
            theArtist = this.artistRepository.findById(album.getArtist().getId()).orElse(null);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        if (theArtist == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("artist not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        albumToUpdate.setArtist(theArtist);

        RecordCompany theCompany = null;
        try {
            theCompany = this.recordCompanyRepository.findById(album.getRecordCompany().getId()).orElse(null);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        if (theCompany == null){
            ErrorResponse error = new ErrorResponse();
            error.set("company not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        albumToUpdate.setRecordCompany(theCompany);
        try {
            albumToUpdate = this.albumRepository.save(albumToUpdate);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        AlbumResponse albumResponse = new AlbumResponse();
        albumResponse.set(albumToUpdate);
        return new ResponseEntity<>(albumResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteAlbum(@PathVariable int id) {
        Album albumToDelete = this.albumRepository.findById(id).orElse(null);
        if (albumToDelete == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        this.albumRepository.delete(albumToDelete);
        AlbumResponse albumResponse = new AlbumResponse();
        albumResponse.set(albumToDelete);
        return ResponseEntity.ok(albumResponse);
    }
}
