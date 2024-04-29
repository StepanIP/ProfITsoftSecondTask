package org.example.controller;

import org.example.model.Director;
import org.example.service.DirectorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class DirectorController {

    private final DirectorService directorService;

    public DirectorController(DirectorService entity2Service) {
        this.directorService = entity2Service;
    }

    @GetMapping("/director")
    public ResponseEntity<List<Director>> getAllDirectors() {
        List<Director> entities = directorService.getAllDirectors();
        return new ResponseEntity<>(entities, HttpStatus.OK);
    }

    @PostMapping("/director")
    public ResponseEntity<Director> createDirector(@RequestBody Director entityData) {
        if (directorService.isDirectorNameUnique(entityData.getName())) {
            Director createdEntity = directorService.saveDirector(entityData);
            return new ResponseEntity<>(createdEntity, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/director/{id}")
    public ResponseEntity<Director> updateDirector(@PathVariable int id, @RequestBody Director entityData) {
        Optional<Director> existingEntity = directorService.getDirectorById(id);
        if (existingEntity.isPresent()) {
            if (directorService.isDirectorNameUnique(entityData.getName())) {
                entityData.setId(id);
                Director updatedEntity = directorService.updateDirector(entityData);
                return new ResponseEntity<>(updatedEntity, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/director/{id}")
    public ResponseEntity<Void> deleteDirector(@PathVariable int id) {
        Optional<Director> existingEntity = directorService.getDirectorById(id);
        if (existingEntity.isPresent()) {
            directorService.deleteDirector(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
