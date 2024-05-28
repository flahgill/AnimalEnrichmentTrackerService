package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewAllHabitatsRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewUserHabitatsRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewAllHabitatsResult;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewUserHabitatsResult;
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

public class ViewAllHabitatsActivityTest {
    @Mock
    private HabitatDao habitatDao;
    private ViewAllHabitatsActivity viewAllHabitatsActivity;

    @BeforeEach
    public void setup() {
        initMocks(this);
        viewAllHabitatsActivity = new ViewAllHabitatsActivity(habitatDao);
    }

    @Test
    public void handleRequest_defaultActiveHabitatsRequest_returnsListOfHabitatModelInResult() {
        // GIVEN
        Habitat habitat1 = HabitatTestHelper.generateHabitatWithNEnrichments(5);
        Habitat habitat2 = HabitatTestHelper.generateHabitatWithNEnrichments(10);

        List<Habitat> expectedHabitats = List.of(habitat1, habitat2);

        when(habitatDao.getAllHabitats("active")).thenReturn(expectedHabitats);

        ViewAllHabitatsRequest request = ViewAllHabitatsRequest.builder()
                .build();

        // WHEN
        ViewAllHabitatsResult result = viewAllHabitatsActivity.handleRequest(request);

        // THEN
        assertEquals(2, result.getHabitats().size());
        assertNotNull(result);
        assertEquals(result.getHabitats().get(0).getTotalAnimals(), habitat1.getTotalAnimals());
        assertEquals(result.getHabitats().get(0).getIsActive(), habitat1.getIsActive());
        assertEquals(result.getHabitats().get(1).getTotalAnimals(), habitat2.getTotalAnimals());
        assertEquals(result.getHabitats().get(1).getIsActive(), habitat2.getIsActive());
    }

    @Test
    public void handleRequest_InactiveHabitatsRequestWithAllActiveHabitats_returnsListOfHabitatModelInResult() {
        // GIVEN
        Habitat habitat1 = HabitatTestHelper.generateHabitatWithNEnrichments(5);
        Habitat habitat2 = HabitatTestHelper.generateHabitatWithNEnrichments(10);

        List<Habitat> expectedHabitats = List.of(habitat1, habitat2);

        when(habitatDao.getAllHabitats("inactive")).thenReturn(new ArrayList<>());

        ViewAllHabitatsRequest request = ViewAllHabitatsRequest.builder()
                .withIsActive("inactive")
                .build();

        // WHEN
        ViewAllHabitatsResult result = viewAllHabitatsActivity.handleRequest(request);

        // THEN
        assertEquals(0, result.getHabitats().size());
        assertNotNull(result);
    }

    @Test
    public void handleRequest_InactiveHabitatsRequestWithAOneInactiveHabitat_returnsListOfHabitatModelInResult() {
        // GIVEN
        Habitat habitat1 = HabitatTestHelper.generateHabitatWithNEnrichments(5);
        Habitat habitat2 = HabitatTestHelper.generateHabitatWithNEnrichments(10);
        habitat2.setIsActive("inactive");

        List<Habitat> expectedHabitats = List.of(habitat1, habitat2);

        when(habitatDao.getAllHabitats("inactive")).thenReturn(List.of(habitat2));

        ViewAllHabitatsRequest request = ViewAllHabitatsRequest.builder()
                .withIsActive("inactive")
                .build();

        // WHEN
        ViewAllHabitatsResult result = viewAllHabitatsActivity.handleRequest(request);

        // THEN
        assertEquals(1, result.getHabitats().size());
        assertNotNull(result);
        assertEquals(result.getHabitats().get(0).getIsActive(), habitat2.getIsActive());
    }
}
