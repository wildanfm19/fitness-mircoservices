package com.fitness.aiservice.service;

import com.fitness.aiservice.model.Recomendation;
import com.fitness.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;

    public List<Recomendation> getUserRecommendation(String userId) {
        return recommendationRepository.findByUserId(userId);
    }

    public Recomendation getActivityRecommendation(String activityId) {
        return recommendationRepository.findByActivityId(activityId)
                .orElseThrow(() -> new RuntimeException("No Recommendation found for this activity with id: " + activityId ));
    }

    public Recomendation createRecommendation(Recomendation recomendation){
        return recommendationRepository.save(recomendation);
    }
}
