package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewAnimalsForHabitatRequest;
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

public class ViewAnimalsForHabitatActivityTest {
    @Mock
    private HabitatDao habitatDao;
    private ViewAnimalsForHabitatActivity viewAnimalsForHabitatActivity;

    @BeforeEach
    public void setup() {
        initMocks(this);
        viewAnimalsForHabitatActivity = new ViewAnimalsForHabitatActivity(habitatDao);
    }

    @Test
    public void handleRequest_savedHabitatFound_returnsHabitatListOfAnimalsInResult() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitatWithNEnrichments(3);
        habitat.setAnimalsInHabitat(List.of("Dwight", "Jim", "Pam"));

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);

        ViewAnimalsForHabitatRequest request = ViewAnimalsForHabitatRequest.builder()
                .withHabitatId(habitat.getHabitatId())
                .build();

        // WHEN
        ViewAnimalsForHabitatResult result = viewAnimalsForHabitatActivity.handleRequest(request);

        // THEN
        assertEquals(habitat.getAnimalsInHabitat(), result.getAnimalsInHabitat());
    }

    @Test
    public void handleRequest_habitatWithNoAnimals_returnsEmptyList() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();
        habitat.setAnimalsInHabitat(new ArrayList<>());

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);

        ViewAnimalsForHabitatRequest request = ViewAnimalsForHabitatRequest.builder()
                .withHabitatId(habitat.getHabitatId())
                .build();

        // WHEN
        ViewAnimalsForHabitatResult result = viewAnimalsForHabitatActivity.handleRequest(request);

        // THEN
        assertNotNull(result.getAnimalsInHabitat());
        assertEquals(habitat.getAnimalsInHabitat(), result.getAnimalsInHabitat());
    }
}
