package com.fitness.aiservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityAIService {

    private final GeminiService geminiService;

    public Recommendation generateRecommendation(Activity activity) {
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiService.getAnswer(prompt);
        log.info("RESPONSE FROM AI: {}", aiResponse);
        return processAiResponse(activity , aiResponse);
    }

    private Recommendation processAiResponse(Activity activity , String aiResponse){
        try{
            // TO CONVERT JSON STRING TO RECOMENDATION OBJECT

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(aiResponse);

            JsonNode textNode = rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text");

            String jsonContent = textNode.asText()
                    .replaceAll("```json\\n" , "")
                    .replaceAll("\\n```" , "")
                    .trim();

//            log.info("PARSE RESPONSE FROM AI: {}" , jsonContent);

            JsonNode analysisJson = mapper.readTree(jsonContent);
            JsonNode analysisNode = analysisJson.path("analysis");
            StringBuilder fullAnalysis = new StringBuilder();
            addAnalysisSection(fullAnalysis , analysisNode , "overall" , "Overall:");
            addAnalysisSection(fullAnalysis , analysisNode , "pace" , "Pace:");
            addAnalysisSection(fullAnalysis , analysisNode , "heartRate" , "Heart Rate:");
            addAnalysisSection(fullAnalysis , analysisNode , "caloriesBurned" , "Calories:");

            List<String> improvements = extractImprovements(analysisJson.path("improvements"));
            List<String> suggestions = extractSuggestions(analysisJson.path("suggestions"));
            List<String> safety = extractSafetyGuidelines(analysisJson.path("safety"));

            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getType())
                    .recommendation(fullAnalysis.toString().trim())
                    .improvements(improvements)
                    .suggestion(suggestions)
                    .safety(safety)
                    .createdAt(LocalDateTime.now())
                    .build();

        } catch(Exception e){
            e.printStackTrace();
            return createDefaultRecomemendation(activity);
        }
    }

    private Recommendation createDefaultRecomemendation(Activity activity) {
        return Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .activityType(activity.getType())
                .recommendation("Unable to generate detailed analysis")
                .improvements(Collections.singletonList("Continue your current workouts"))
                .suggestion(Collections.singletonList("Consider consulting to a fitness trainer"))
                .safety(Arrays.asList("Always warm up before exercise" , "Always cool down after exercise" , "Always stretch after exercise", "Stay hydrated"))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private List<String> extractSafetyGuidelines(JsonNode safetyNode) {
        List<String> safety = new ArrayList<>();
        if (safetyNode.isArray()) {
            safetyNode.forEach(item -> safety.add(item.asText()));
        }
        return safety.isEmpty() ?
                Collections.singletonList("Follow general safety guidelines") :
                safety;
    }
    private List<String> extractSuggestions(JsonNode suggestionsNode) {
        List<String> suggestions = new ArrayList<>();
        if (suggestionsNode.isArray()) {
            suggestionsNode.forEach(suggestion -> {
                String workout = suggestion.path("workout").asText();
                String description =  suggestion.path("description").asText();
                suggestions.add(String.format("%s: %s" , workout , description));
            });
        }
        return suggestions.isEmpty() ?
                Collections.singletonList("No specific suggestions provided") :
                suggestions;
    }

    private List<String> extractImprovements(JsonNode improvementsNode) {
        List<String> improvements = new ArrayList<>();
        if (improvementsNode.isArray()) {
            improvementsNode.forEach(improvement -> {
                String area = improvement.path("area").asText();
                String detail =  improvement.path("recommendation").asText();
                improvements.add(String.format("%s: %s" , area , detail));
            });
        }
        return improvements.isEmpty() ?
                Collections.singletonList("No specific improvements provided") :
                improvements;
    }

    private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {
        if(!analysisNode.path(key).isMissingNode()){
            fullAnalysis.append(prefix)
                    .append(analysisNode.path(key).asText())
                    .append("\n\n");
        }
    }


    private String createPromptForActivity(Activity activity) {
        return String.format("""
    Analisis aktivitas kebugaran berikut dan berikan rekomendasi secara detail dalam format JSON PERSIS seperti berikut:
    {
      "analysis": {
        "overall": "Analisis keseluruhan di sini",
        "pace": "Analisis kecepatan di sini",
        "heartRate": "Analisis detak jantung di sini",
        "caloriesBurned": "Analisis kalori di sini"
      },
      "improvements": [
        {
          "area": "Nama area peningkatan",
          "recommendation": "Rekomendasi detail"
        }
      ],
      "suggestions": [
        {
          "workout": "Nama latihan",
          "description": "Deskripsi latihan secara detail"
        }
      ],
      "safety": [
        "Poin keselamatan 1",
        "Poin keselamatan 2"
      ]
    }

    Analisis aktivitas berikut:
    Jenis Aktivitas: %s
    Durasi: %d menit
    Kalori Terbakar: %d
    Metrik Tambahan: %s
    
    Berikan analisis yang mendalam dengan fokus pada performa, peningkatan, saran latihan berikutnya, dan panduan keselamatan.
    Pastikan respons mengikuti format JSON PERSIS seperti yang ditunjukkan di atas.
    """,
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics()
        );
    }

}
