package com.fitness.activityservice.dto;

import com.fitness.activityservice.model.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityRequest {

   private String userId;
   private ActivityType type;
   private Integer duration;
   private Integer caloriesBurned;
   private LocalDateTime startTime;
   private Map<String , Object> additionalMetrics;
}
