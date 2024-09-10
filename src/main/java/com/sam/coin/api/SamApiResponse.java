package com.sam.coin.api;

import lombok.*;

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
