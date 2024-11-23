package com.rocketseat.redirectUrlShortener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class UrlData {
    private String originalUrl;
    private long expirationTimeInSeconds; //17:24 - Generate sem InSeconds
}