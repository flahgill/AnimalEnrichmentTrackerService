package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewAcceptableEnrichmentIdsRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewAnimalsForHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewAcceptableEnrichmentIdsResult;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewAnimalsForHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.helper.HabitatTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ViewAcceptableEnrichmentIdsActivityTest {
    @Mock
    private HabitatDao habitatDao;
    private ViewAcceptableEnrichmentIdsActivity viewAcceptableEnrichmentIdsActivity;
    @BeforeEach
    public void setup() {
        initMocks(this);
        viewAcceptableEnrichmentIdsActivity = new ViewAcceptableEnrichmentIdsActivity(habitatDao);
    }

    @Test
    public void handleRequest_savedHabitatFound_returnsHabitatListOfAcceptableIdsInResult() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitatWithNEnrichments(3);
        habitat.setAcceptableEnrichmentIds(List.of("01", "02", "03"));

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);

        ViewAcceptableEnrichmentIdsRequest request = ViewAcceptableEnrichmentIdsRequest.builder()
                .withHabitatId(habitat.getHabitatId())
                .build();

        // WHEN
        ViewAcceptableEnrichmentIdsResult result = viewAcceptableEnrichmentIdsActivity.handleRequest(request);

        // THEN
        assertEquals(habitat.getAcceptableEnrichmentIds(), result.getAcceptableEnrichmentIds());
    }

    @Test
    public void handleRequest_habitatWithNoAcceptableIds_returnsEmptyList() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();
        habitat.setCompletedEnrichments(new ArrayList<>());

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);

        ViewAcceptableEnrichmentIdsRequest request = ViewAcceptableEnrichmentIdsRequest.builder()
                .withHabitatId(habitat.getHabitatId())
                .build();

        // WHEN
        ViewAcceptableEnrichmentIdsResult result = viewAcceptableEnrichmentIdsActivity.handleRequest(request);

        // THEN
        assertNotNull(result.getAcceptableEnrichmentIds());
        assertEquals(habitat.getAcceptableEnrichmentIds(), result.getAcceptableEnrichmentIds());
    }
}
