package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewUserHabitatsRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewUserHabitatsResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.helper.HabitatTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ViewUserHabitatsActivityTest {

    @Mock
    private HabitatDao habitatDao;

    private ViewUserHabitatsActivity viewUserHabitatsActivity;

    @BeforeEach
    public void setup() {
        initMocks(this);
        viewUserHabitatsActivity = new ViewUserHabitatsActivity(habitatDao);
    }

    @Test
    public void handleRequest_savedHabitatsFound_returnsListOfHabitatModelInResult() {
        // GIVEN
        Habitat habitat1 = HabitatTestHelper.generateHabitatWithNEnrichments(5);
        Habitat habitat2 = HabitatTestHelper.generateHabitatWithNEnrichments(10);


        List<Habitat> expectedHabitats = List.of(habitat1, habitat2);

        when(habitatDao.getAllHabitatsForKeeper(habitat1.getKeeperManagerId())).thenReturn(expectedHabitats);

        ViewUserHabitatsRequest request = ViewUserHabitatsRequest.builder()
                .withKeeperManagerId(habitat1.getKeeperManagerId())
                .build();

        // WHEN
        ViewUserHabitatsResult result = viewUserHabitatsActivity.handleRequest(request);

        // THEN
        assertEquals(2, result.getHabitats().size());
        assertNotNull(result);
        assertEquals(result.getHabitats().get(0).getTotalAnimals(), habitat1.getTotalAnimals());
        assertEquals(result.getHabitats().get(1).getTotalAnimals(), habitat2.getTotalAnimals());
    }
}
