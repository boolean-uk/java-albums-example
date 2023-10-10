package com.booleanuk.music.controller;

import com.booleanuk.music.model.Artist;
import com.booleanuk.music.repository.ArtistRepository;
import com.booleanuk.music.response.*;
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
    public ResponseEntity<ArtistListResponse> getAllArtists() {
        ArtistListResponse artistListResponse = new ArtistListResponse();
        artistListResponse.set(this.artistRepository.findAll());
        return ResponseEntity.ok(artistListResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getArtistById(@PathVariable int id) {
        Artist artist = this.artistRepository.findById(id).orElse(null);
        if (artist == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        ArtistResponse artistResponse = new ArtistResponse();
        artistResponse.set(artist);
        return ResponseEntity.ok(artistResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createArtist(@RequestBody Artist artist) {
        ArtistResponse artistResponse = new ArtistResponse();
        try {
            artistResponse.set(this.artistRepository.save(artist));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(artistResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateArtist(@PathVariable int id, @RequestBody Artist artist) {
        Artist artistToUpdate = null;
        try {
            artistToUpdate = this.artistRepository.findById(id).orElse(null);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        if (artistToUpdate == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        artistToUpdate.setName(artistToUpdate.getName());
        artistToUpdate.setNumberOfMembers(artist.getNumberOfMembers());
        artistToUpdate.setStillPerforming(artist.isStillPerforming());
        artistToUpdate = this.artistRepository.save(artistToUpdate);
        ArtistResponse artistResponse = new ArtistResponse();
        artistResponse.set(artistToUpdate);
        return new ResponseEntity<>(artistResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteArtist(@PathVariable int id) {
        Artist artistToDelete = this.artistRepository.findById(id).orElse(null);
        if (artistToDelete == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        this.artistRepository.delete(artistToDelete);
        ArtistResponse artistResponse = new ArtistResponse();
        artistResponse.set(artistToDelete);
        return ResponseEntity.ok(artistResponse);
    }
}
