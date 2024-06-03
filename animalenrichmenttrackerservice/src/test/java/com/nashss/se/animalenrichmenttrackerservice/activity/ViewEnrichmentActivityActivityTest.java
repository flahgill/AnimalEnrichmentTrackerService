package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewEnrichmentActivityRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewEnrichmentActivityResult;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.EnrichmentActivityDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.helper.EnrichmentActivityTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ViewEnrichmentActivityActivityTest {
    @Mock
    EnrichmentActivityDao enrichmentActivityDao;
    private ViewEnrichmentActivityActivity viewEnrichmentActivityActivity;
    @BeforeEach
    public void setup() {
        initMocks(this);
        viewEnrichmentActivityActivity = new ViewEnrichmentActivityActivity(enrichmentActivityDao);
    }
    @Test
    public void handleRequest_savedAcivityFound_returnsResultInEnrichmentActivityModel() {
        // GIVEN
        String activityId = "123";
        String habitatId = "habitatId";
        LocalDate date = LocalDate.of(2024, 06, 03);
        String complete = "complete";
        String enrichId = "01";
        int rating = 5;
        String desc = "hello world";
        String name = "name";

        EnrichmentActivity activity = new EnrichmentActivity();
        activity.setActivityId(activityId);
        activity.setHabitatId(habitatId);
        activity.setDateCompleted(date);
        activity.setOnHabitat(true);
        activity.setIsComplete(complete);
        activity.setEnrichmentId(enrichId);
        activity.setKeeperRating(rating);
        activity.setDescription(desc);
        activity.setName(name);

        when(enrichmentActivityDao.getEnrichmentActivity(activityId)).thenReturn(activity);

        ViewEnrichmentActivityRequest request = ViewEnrichmentActivityRequest.builder()
                .withActivityId(activityId)
                .build();

        // WHEN
        ViewEnrichmentActivityResult result = viewEnrichmentActivityActivity.handleRequest(request);

        // THEN
        assertEquals(activityId, result.getEnrichmentActivity().getActivityId());
        assertEquals(habitatId, result.getEnrichmentActivity().getHabitatId());
        assertEquals(date, result.getEnrichmentActivity().getDateCompleted());
        assertEquals(complete, result.getEnrichmentActivity().getIsComplete());
        assertEquals(enrichId, result.getEnrichmentActivity().getEnrichmentId());
        assertEquals(rating, result.getEnrichmentActivity().getKeeperRating());
        assertEquals(desc, result.getEnrichmentActivity().getDescription());
        assertEquals(name, result.getEnrichmentActivity().getName());
    }
}
