package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.RemoveEnrichmentActivityRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.RemoveEnrichmentActivityResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.EnrichmentActivityDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.EnrichmentActivityCurrentlyOnHabitatException;
import com.nashss.se.animalenrichmenttrackerservice.helper.EnrichmentActivityTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RemoveEnrichmentActivityActivityTest {
    @Mock
    private EnrichmentActivityDao enrichmentActivityDao;
    private RemoveEnrichmentActivityActivity removeEnrichmentActivityActivity;
    @BeforeEach
    public void setup() {
        initMocks(this);
        removeEnrichmentActivityActivity = new RemoveEnrichmentActivityActivity(enrichmentActivityDao);
    }

    @Test
    public void handleRequest_savedEAFoundAndNotOnHabitat_returnsRemovedEAModelInResult() {
        // GIVEN
        EnrichmentActivity expectedEA = EnrichmentActivityTestHelper.generateEnrichmentActivity(1);
        expectedEA.setOnHabitat(false);
        String activityId = expectedEA.getActivityId();

        when(enrichmentActivityDao.getEnrichmentActivity(activityId)).thenReturn(expectedEA);
        when(enrichmentActivityDao.removeEnrichmentActivity(activityId)).thenReturn(expectedEA);

        RemoveEnrichmentActivityRequest request = RemoveEnrichmentActivityRequest.builder()
                .withActivityId(activityId)
                .build();

        // WHEN
        RemoveEnrichmentActivityResult result = removeEnrichmentActivityActivity.handleRequest(request);

        // THEN
        assertEquals(activityId, result.getActivityModel().getActivityId());
    }

    @Test
    public void handleRequest_savedEAFoundAndOnHabitat_throwsEnrichmentActivityCurrentlyOnHabitatException() {
        // GIVEN
        EnrichmentActivity expectedEA = EnrichmentActivityTestHelper.generateEnrichmentActivity(1);
        String activityId = expectedEA.getActivityId();

        when(enrichmentActivityDao.getEnrichmentActivity(activityId)).thenReturn(expectedEA);
        when(enrichmentActivityDao.removeEnrichmentActivity(activityId)).thenReturn(expectedEA);

        RemoveEnrichmentActivityRequest request = RemoveEnrichmentActivityRequest.builder()
                .withActivityId(activityId)
                .build();

        // WHEN && THEN
        assertThrows(EnrichmentActivityCurrentlyOnHabitatException.class, ()-> removeEnrichmentActivityActivity.handleRequest(request));
    }
}
