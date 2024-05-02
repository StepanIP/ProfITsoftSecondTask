package org.example.dto.request;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FilmPaginationRequest {
    private Map<String, String> filters;
    private int page;
    private int size;
}