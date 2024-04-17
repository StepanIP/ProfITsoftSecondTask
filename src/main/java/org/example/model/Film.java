package org.example.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Film {
    @Id
    private int id;
    private String title;
    private int year;
    private String director;

    @ElementCollection
    @CollectionTable(name="actors", joinColumns=@JoinColumn(name="actor_id"))
    @Column(name="actors")
    private List<String> actors;

    @ElementCollection
    @CollectionTable(name="genres", joinColumns=@JoinColumn(name="genre_id"))
    @Column(name="genres")
    private List<String> genres;

    public Film(String title, int year, String director, List<String> actors, List<String> genres) {
        this.title = title;
        this.year = year;
        this.director = director;
        this.actors = actors;
        this.genres = genres;
    }

    public Film() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
}
