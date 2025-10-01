package com.fitness.aiservice.repository;

import com.fitness.aiservice.model.Recomendation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendationRepository extends MongoRepository<Recomendation , String> {
    List<Recomendation> findByUserId(String userId);

    Optional<Recomendation> findByActivityId(String activityId);
}
