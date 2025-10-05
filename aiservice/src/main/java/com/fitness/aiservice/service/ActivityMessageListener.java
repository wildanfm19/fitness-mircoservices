package com.fitness.aiservice.service;

import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {

//    @Value("${rabbitmq.queue.name}")
//    private String queueName;

    private final ActivityAIService activityAIService;

    private final RecommendationRepository recommendationRepository;

    @RabbitListener(queues = "activity.queue")
    public void processActivity(Activity activity){
        log.info("Received activity for processing: {}" , activity);
//        log.info("Generated Recommendation: {}" , activityAIService.generateRecommendation(activity) );
        Recommendation recommendation = activityAIService.generateRecommendation(activity);
        log.info("Recommendation: " + recommendation);
        recommendationRepository.save(recommendation);
    }
}
