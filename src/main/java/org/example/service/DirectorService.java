package org.example.service;

import org.example.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorService {
    Director saveDirector(Director director);
    List<Director> getAllDirectors();
    Optional<Director> getDirectorById(int id);
    Director updateDirector(Director director);
    void deleteDirector(int id);
    Optional<Director> findByName(String name);
}