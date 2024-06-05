package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.SearchEnrichmentActivitiesRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.SearchEnrichmentActivitiesResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.EnrichmentActivityDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentActivityModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SearchEnrichmentActivitiesActivityTest {
    @Mock
    private EnrichmentActivityDao enrichmentActivityDao;
    private SearchEnrichmentActivitiesActivity searchEnrichmentActivitiesActivity;
    @BeforeEach
    public void setup() {
        initMocks(this);
        searchEnrichmentActivitiesActivity = new SearchEnrichmentActivitiesActivity(enrichmentActivityDao);
    }

    @Test
    public void handleRequest_eaMatchesSearch_returnsEnrichmentActivityModelListInResult() {
        // GIVEN
        String criteria = "ea";
        String[] criteriaArray = {criteria};

        List<EnrichmentActivity> expected = List.of(
                newEA("id1", "a good ea", "description"),
                newEA("id2", "another ea", "description"));

        when(enrichmentActivityDao.searchEnrichmentActivities(criteriaArray)).thenReturn(expected);

        SearchEnrichmentActivitiesRequest request = SearchEnrichmentActivitiesRequest.builder()
                .withCriteria(criteria)
                .build();

        // WHEN
        SearchEnrichmentActivitiesResult result = searchEnrichmentActivitiesActivity.handleRequest(request);

        // THEN
        List<EnrichmentActivityModel> resultModels = result.getEnrichmentActivities();
        assertEquals(expected.size(), resultModels.size());

        for (int i=0; i<expected.size(); i++) {
            assertEquals(expected.get(i).getActivityId(), resultModels.get(i).getActivityId());
            assertEquals(expected.get(i).getActivityName(), resultModels.get(i).getActivityName());
        }
    }

    @Test
    public void handleRequest_nullCriteria_returnsEmptyResultList() {
        // GIVEN
        String criteria = null;
        ArgumentCaptor<String[]> criteriaArray = ArgumentCaptor.forClass(String[].class);

        when(enrichmentActivityDao.searchEnrichmentActivities(criteriaArray.capture())).thenReturn(List.of());

        SearchEnrichmentActivitiesRequest request = SearchEnrichmentActivitiesRequest.builder()
                .withCriteria(criteria)
                .build();

        // WHEN
        SearchEnrichmentActivitiesResult result = searchEnrichmentActivitiesActivity.handleRequest(request);

        // THEN
        assertEquals(0, criteriaArray.getValue().length);
    }

    private static EnrichmentActivity newEA(String id, String name, String description) {
        EnrichmentActivity ea = new EnrichmentActivity();

        ea.setActivityName(name);
        ea.setEnrichmentId(id);
        ea.setDescription(description);

        return ea;
    }
}
