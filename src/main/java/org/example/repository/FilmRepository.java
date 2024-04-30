package org.example.repository;

import org.example.model.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

@Repository
public interface FilmRepository extends JpaRepository<Film, Integer>, JpaSpecificationExecutor<Film> {

    Page<Film> findAllByGenresName(String genreName, Pageable pageable);

    Page<Film> findAllByActorsName(String genreName, Pageable pageable);

    Page<Film> findAllByDirectorName(String directorName, Pageable pageable);

}