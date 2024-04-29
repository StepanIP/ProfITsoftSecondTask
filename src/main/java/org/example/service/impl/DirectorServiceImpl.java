package org.example.service.impl;

import org.example.model.Director;
import org.example.repository.DirectorRepository;
import org.example.service.DirectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DirectorServiceImpl implements DirectorService {

    private final DirectorRepository directorRepository;

    @Autowired
    public DirectorServiceImpl(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    @Override
    public Director saveDirector(Director director) {
        return directorRepository.save(director);
    }

    @Override
    public List<Director> getAllDirectors() {
        return directorRepository.findAll();
    }

    @Override
    public Optional<Director> getDirectorById(int id) {
        return directorRepository.findById(id);
    }

    @Override
    public Director updateDirector(Director director) {
        return directorRepository.save(director);
    }

    @Override
    public void deleteDirector(int id) {
        directorRepository.deleteById(id);
    }

    @Override
    public boolean isDirectorNameUnique(String name) {
        return directorRepository.findByName(name) == null;
    }
}