package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewAllEnrichmentActivitiesRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewAllEnrichmentActivitiesResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.EnrichmentActivityDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.helper.EnrichmentActivityTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ViewAllEnrichmentActivitiesActivityTest {
    @Mock
    EnrichmentActivityDao enrichmentActivityDao;
    private ViewAllEnrichmentActivitiesActivity viewAllEnrichmentActivitiesActivity;
    @BeforeEach
    public void setup() {
        initMocks(this);
        viewAllEnrichmentActivitiesActivity = new ViewAllEnrichmentActivitiesActivity(enrichmentActivityDao);
    }

    @Test
    public void handleRequest_defaultCompleteEAsRequest_returnsListOfEnrichmentActivitityModelInResult() {
        // GIVEN
        EnrichmentActivity ea1 = EnrichmentActivityTestHelper.generateEnrichmentActivity(1);
        EnrichmentActivity ea2 = EnrichmentActivityTestHelper.generateEnrichmentActivity(2);

        List<EnrichmentActivity> expectedEAs = List.of(ea1, ea2);

        when(enrichmentActivityDao.getAllEnrichmentActivities("complete")).thenReturn(expectedEAs);

        ViewAllEnrichmentActivitiesRequest request = ViewAllEnrichmentActivitiesRequest.builder()
                .build();

        // WHEN
        ViewAllEnrichmentActivitiesResult result = viewAllEnrichmentActivitiesActivity.handleRequest(request);

        // THEN
        assertEquals(2, result.getEnrichmentActivities().size());
        assertNotNull(result);
        assertEquals(ea1.getActivityId(), result.getEnrichmentActivities().get(0).getActivityId());
        assertEquals(ea2.getActivityId(), result.getEnrichmentActivities().get(1).getActivityId());
    }

    @Test
    public void handleRequest_incompleteRequestWithAllCompleteEAs_returnsEmptyList() {
        // GIVEN
        EnrichmentActivity ea1 = EnrichmentActivityTestHelper.generateEnrichmentActivity(1);
        EnrichmentActivity ea2 = EnrichmentActivityTestHelper.generateEnrichmentActivity(2);

        List<EnrichmentActivity> expectedEAs = List.of(ea1, ea2);

        when(enrichmentActivityDao.getAllEnrichmentActivities("incomplete")).thenReturn(new ArrayList<>());

        ViewAllEnrichmentActivitiesRequest request = ViewAllEnrichmentActivitiesRequest.builder()
                .withIsComplete("incomplete")
                .build();

        // WHEN
        ViewAllEnrichmentActivitiesResult result = viewAllEnrichmentActivitiesActivity.handleRequest(request);

        // THEN
        assertEquals(0, result.getEnrichmentActivities().size());
        assertNotNull(result);
    }

    @Test
    public void handleRequest_incompleteRequestWithOneIncompleteEA_returnsListOfEnrichmentActivitityModelInResult() {
        // GIVEN
        EnrichmentActivity ea1 = EnrichmentActivityTestHelper.generateEnrichmentActivity(1);
        EnrichmentActivity ea2 = EnrichmentActivityTestHelper.generateEnrichmentActivity(2);
        ea2.setIsComplete("incomplete");

        List<EnrichmentActivity> expectedEAs = List.of(ea2);

        when(enrichmentActivityDao.getAllEnrichmentActivities("incomplete")).thenReturn(expectedEAs);

        ViewAllEnrichmentActivitiesRequest request = ViewAllEnrichmentActivitiesRequest.builder()
                .withIsComplete("incomplete")
                .build();

        // WHEN
        ViewAllEnrichmentActivitiesResult result = viewAllEnrichmentActivitiesActivity.handleRequest(request);

        // THEN
        assertEquals(1, result.getEnrichmentActivities().size());
        assertNotNull(result);
        assertEquals(ea1.getActivityId(), result.getEnrichmentActivities().get(0).getActivityId());
    }
}
