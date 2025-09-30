package com.fitness.activityservice.controller;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.services.ActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping
    public ResponseEntity<ActivityResponse> trackActivity( @RequestBody ActivityRequest request){
        return ResponseEntity.ok(activityService.trackActivity(request));
    }

    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getUserActivity(@RequestHeader("X-User-ID")String userId){
        return ResponseEntity.ok(activityService.getUserActivity(userId));
    }

    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityResponse> getUserActivityById(@PathVariable String activityId){
        return ResponseEntity.ok(activityService.getActivityById(activityId));
    }
}
