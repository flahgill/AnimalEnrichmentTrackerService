package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.SearchEnrichmentsRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.SearchEnrichmentsResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.EnrichmentDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Enrichment;
import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SearchEnrichmentsActivityTest {
    @Mock
    private EnrichmentDao enrichmentDao;
    private SearchEnrichmentsActivity searchEnrichmentsActivity;
    @BeforeEach
    public void setup() {
        initMocks(this);
        searchEnrichmentsActivity = new SearchEnrichmentsActivity(enrichmentDao);
    }

    @Test
    public void handleRequest_enrichmentMatchesSearch_returnsEnrichmentModelListInResult() {
        // GIVEN
        String criteria = "enrich";
        String[] criteriaArray = {criteria};

        List<Enrichment> expected = List.of(
                newEnrich("id1", "a good enrich", "description"),
                newEnrich("id2", "another enrich", "description"));

        when(enrichmentDao.searchEnrichments(criteriaArray)).thenReturn(expected);

        SearchEnrichmentsRequest request = SearchEnrichmentsRequest.builder()
                .withCriteria(criteria)
                .build();

        // WHEN
        SearchEnrichmentsResult result = searchEnrichmentsActivity.handleRequest(request);

        // THEN
        List<EnrichmentModel> resultModels = result.getEnrichments();
        assertEquals(expected.size(), resultModels.size());

        for (int i=0; i<expected.size(); i++) {
            assertEquals(expected.get(i).getEnrichmentId(), resultModels.get(i).getEnrichmentId());
            assertEquals(expected.get(i).getActivityName(), resultModels.get(i).getActivityName());
        }
    }

    @Test
    public void handleRequest_nullCriteria_returnsEmptyResultList() {
        // GIVEN
        String criteria = null;
        ArgumentCaptor<String[]> criteriaArray = ArgumentCaptor.forClass(String[].class);

        when(enrichmentDao.searchEnrichments(criteriaArray.capture())).thenReturn(List.of());

        SearchEnrichmentsRequest request = SearchEnrichmentsRequest.builder()
                .withCriteria(criteria)
                .build();

        // WHEN
        SearchEnrichmentsResult result = searchEnrichmentsActivity.handleRequest(request);

        // THEN
        assertEquals(0, criteriaArray.getValue().length);
    }

    private static Enrichment newEnrich(String id, String name, String description) {
        Enrichment enrich = new Enrichment();

        enrich.setActivityName(name);
        enrich.setEnrichmentId(id);
        enrich.setDescription(description);

        return enrich;
    }
}
