package org.example.dto.request;

import lombok.*;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FilmReportRequest {
    private String director;
    private List<String> actors;
    private List<String> genres;
}
