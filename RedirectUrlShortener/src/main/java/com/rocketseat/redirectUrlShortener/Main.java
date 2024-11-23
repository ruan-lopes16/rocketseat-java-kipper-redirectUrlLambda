package com.rocketseat.redirectUrlShortener;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Main implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    private final S3Client s3Client = S3Client.builder().build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context){

        String pathParameteres = (String) input.get("rawPath");
        String shortUrlCode = pathParameteres.replace("/", "");

        if (shortUrlCode.isEmpty()) {
            throw new IllegalArgumentException("Invalid input: 'shortUrlCode' is required.");
        }

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket( "url-shortener-storage-bucket-lambda" )
                .key( shortUrlCode + ".json" )
                .build();

        InputStream s3ObjectStream;

        try {
            s3ObjectStream = s3Client.getObject( getObjectRequest );

        } catch (Exception e) {
            throw new RuntimeException( "Error fetching data from S3 " + e.getMessage(), e );

        }

        UrlData urlData;
        try {
            urlData = objectMapper.readValue(s3ObjectStream, UrlData.class);

        } catch (Exception e) {
            throw new RuntimeException( "Error deserializing URL data: " + e.getMessage(), e );

        }

        long currentTimeInSeconds = System.currentTimeMillis() / 1000;

        // cenario onde a URL expirou
        Map<String, Object> response = new HashMap<>();
        if ( urlData.getExpirationTimeInSeconds() < currentTimeInSeconds ) {
            response.put( "statusCode", 410 );
            response.put( "body", "This URL has expired." );
            return response;

        }
        // cenario onde a URL ainda é válida
        response.put( "statusCode", 302 );
        Map<String, String> headers = new HashMap<>();
        headers.put( "Location", urlData.getOriginalUrl() );
        response.put( "headers" , headers );

        return response;
    }

}