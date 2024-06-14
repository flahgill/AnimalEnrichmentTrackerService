package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewSpeciesListRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewSpeciesListResult;
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

public class ViewSpeciesListActivityTest {
    @Mock
    private HabitatDao habitatDao;
    private ViewSpeciesListActivity viewSpeciesListActivity;
    @BeforeEach
    public void setup() {
        initMocks(this);
        viewSpeciesListActivity = new ViewSpeciesListActivity(habitatDao);
    }

    @Test
    public void handleRequest_savedHabitatFound_returnsHabitatListOfSpeciesInResult() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitatWithNEnrichments(3);
        habitat.setSpecies(List.of("01", "02", "03"));

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);

        ViewSpeciesListRequest request = ViewSpeciesListRequest.builder()
                .withHabitatId(habitat.getHabitatId())
                .build();

        // WHEN
        ViewSpeciesListResult result = viewSpeciesListActivity.handleRequest(request);

        // THEN
        assertEquals(habitat.getSpecies(), result.getSpeciesList());
    }

    @Test
    public void handleRequest_habitatWithSpecies_returnsEmptyList() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();
        habitat.setSpecies(new ArrayList<>());

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);

        ViewSpeciesListRequest request = ViewSpeciesListRequest.builder()
                .withHabitatId(habitat.getHabitatId())
                .build();

        // WHEN
        ViewSpeciesListResult result = viewSpeciesListActivity.handleRequest(request);

        // THEN
        assertNotNull(result.getSpeciesList());
        assertEquals(habitat.getSpecies(), result.getSpeciesList());
        assertEquals(0, result.getSpeciesList().size());
    }
}
