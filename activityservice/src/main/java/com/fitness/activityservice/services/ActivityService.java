package com.fitness.activityservice.services;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.exception.ResourceNotFoundException;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.repository.ActivityRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;

    private final ModelMapper modelMapper;


    public ActivityResponse trackActivity(ActivityRequest request) {
        System.out.println("Request : " + request);

        boolean isValidUser = userValidationService.validateUser(request.getUserId());
        if(!isValidUser){
           throw new ResourceNotFoundException("User" , "userId" , request.getUserId());
        }
        Activity activity = modelMapper.map(request , Activity.class);
        activity.setId(null); // <--- pastikan id kosong biar dianggap insert baru

        System.out.println("Activity : " + activity);

        Activity savedActivity = activityRepository.save(activity);
        System.out.println("Saved Activity : " + savedActivity);

        return modelMapper.map(savedActivity , ActivityResponse.class);
    }

    public List<ActivityResponse> getUserActivity(String userId) {
        List<Activity> activities= activityRepository.findByUserId(userId);
        List<ActivityResponse> activityResponses = activities.stream()
                .map(activity -> modelMapper.map(activity , ActivityResponse.class))
                .toList();

        return activityResponses;
    }

    public ActivityResponse getActivityById(String activityId) {
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("Activity not found"));
        return modelMapper.map(activity , ActivityResponse.class);
    }




}
