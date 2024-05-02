package org.example.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UploadResponse {
    private int successful;
    private int failed;
}
