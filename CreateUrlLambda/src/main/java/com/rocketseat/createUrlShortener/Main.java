package com.rocketseat.createUrlShortener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class Main implements RequestHandler<Map<String, Object>, Map<String, String>> { // mapeamento de tipos que a classe vai receber

    private final ObjectMapper objectMapper = new ObjectMapper();   // dependencia
    private final S3Client s3Client = S3Client.builder().build(); // dependencia de Cliente

    @Override
    public Map<String, String> handleRequest(Map<String, Object> input, Context context) { // retorna map String para String
        String body = input.get("body").toString(); // busque a chave body no input - transforma em String

        Map<String, String> bodyMap;

        try {                    // pode ser que os campos venham errados, etc - try-catch para evitar que a aplicação "quebre"-crash
            bodyMap = objectMapper.readValue(body, Map.class);

        } catch (Exception exception) {
            throw new RuntimeException("Error parsing JSON body: " + exception.getMessage(), exception);

        }

        // extrair campos do body
        String originalUrl = bodyMap.get("originalUrl");
        String expirationTime = bodyMap.get("expirationTime");
        long expirationTimeInSeconds = Long.parseLong(expirationTime); // 20:55 - 3

        // ID de url encurtada - down - gera ID randomico e o transforma em String e vai "cortar" a String para ter apenas 8 caracteres
        String shortUrlCode = UUID.randomUUID().toString().substring(0, 8); // shortUrlCode - nomear no arquivo json // ID para identificar cada URL encurtada

        UrlData urlData = new UrlData(originalUrl, expirationTimeInSeconds);

        try { //subir dados p/ Bucket
            String urlDataJson = objectMapper.writeValueAsString(urlData); // transforma urlData em .json

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket("url-shortener-storage-bucket-lambda") // nome do bucket
                    .key(shortUrlCode + ".json") // nome do arquivo ID gerado + .json
                    .build(); // transforma num request - "builda"

            s3Client.putObject(request, RequestBody.fromString(urlDataJson)); // request + conteudo do arquivo

        } catch (Exception exception) {
            throw new RuntimeException("Error saving data to S3: " + exception.getMessage(), exception);

        }

        Map<String, String> response = new HashMap<>();
        response.put("code", shortUrlCode);

        return response;
    }
}