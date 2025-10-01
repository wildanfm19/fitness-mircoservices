package com.fitness.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserActivityService {

    private final WebClient activityWebClient;

    public List <Object> getUserActivity(String userId){

        try{
            return activityWebClient.get()
                    .uri("/api/activity") // endpoint di activity-service
                    .header("X-User-ID", userId) // kirim userId lewat header
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();
        } catch (WebClientResponseException e){
            if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                throw  new RuntimeException("User no Found: " + userId);
            }
            else if(e.getStatusCode() == HttpStatus.BAD_REQUEST){
                throw  new RuntimeException("Invalid Request: " + userId);
            }
            throw new RuntimeException("Unexpected Error: " + e.getMessage());
        }
    }
}
