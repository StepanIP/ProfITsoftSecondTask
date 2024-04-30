package org.example.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FilmListResponse {
    List<FilmResponse> list;
    Integer totalPages;
}
