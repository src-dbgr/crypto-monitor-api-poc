package com.sam.coin.api;

import lombok.*;

/**
 * Represents the response format for the SAM API.
 *
 * @param <T> The type of data contained in the response.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class SamApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
}
