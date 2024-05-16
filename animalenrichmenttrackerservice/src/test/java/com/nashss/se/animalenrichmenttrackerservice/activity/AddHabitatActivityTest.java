package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.AddHabitatActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.requests.AddHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.AddHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.InvalidCharacterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class AddHabitatActivityTest {
    @Mock
    private HabitatDao habitatDao;
    private AddHabitatActivity addHabitatActivity;

    @BeforeEach
    public void setup() {
        initMocks(this);
        addHabitatActivity = new AddHabitatActivity(habitatDao);
    }

    @Test
    public void handleRequest_withSpecies_createsAndSavesHabitatWithTags() {
        // GIVEN
        String expectedName = "expectedName";
        String expectedKeeperId = "expectedCustomerId";
        int expectedAnimalCount = 0;
        List<String> expectedSpecies = List.of("giraffe");

        AddHabitatRequest request = AddHabitatRequest.builder()
                .withHabitatName(expectedName)
                .withKeeperManagerId(expectedKeeperId)
                .withSpecies(expectedSpecies)
                .build();

        // WHEN
        AddHabitatResult result = addHabitatActivity.handleRequest(request);

        // THEN
        verify(habitatDao).saveHabitat(any(Habitat.class));

        assertNotNull(result.getHabitat().getHabitatId());
        assertEquals(expectedName, result.getHabitat().getHabitatName());
        assertEquals(expectedKeeperId, result.getHabitat().getKeeperManagerId());
        assertEquals(expectedAnimalCount, result.getHabitat().getTotalAnimals());
        assertEquals(expectedSpecies, result.getHabitat().getSpecies());
    }

    @Test
    public void handleRequest_noSpecies_createsAndSavesHabitatWithoutTags() {
        // GIVEN
        String expectedName = "expectedName";
        String expectedKeeperId = "expectedCustomerId";
        int expectedAnimalCount = 0;

        AddHabitatRequest request = AddHabitatRequest.builder()
                .withHabitatName(expectedName)
                .withKeeperManagerId(expectedKeeperId)
                .build();

        // WHEN
        AddHabitatResult result = addHabitatActivity.handleRequest(request);

        // THEN
        verify(habitatDao).saveHabitat(any(Habitat.class));

        assertNotNull(result.getHabitat().getHabitatId());
        assertEquals(expectedName, result.getHabitat().getHabitatName());
        assertEquals(expectedKeeperId, result.getHabitat().getKeeperManagerId());
        assertEquals(expectedAnimalCount, result.getHabitat().getTotalAnimals());
        assertNull(result.getHabitat().getSpecies());
    }

    @Test
    public void handleRequest_invalidName_throwsInvalidCharacterException() {
        // GIVEN
        AddHabitatRequest request = AddHabitatRequest.builder()
                .withHabitatName("I'm Illegal")
                .withKeeperManagerId("expectedKeeperId")
                .build();

        // WHEN + THEN
        assertThrows(InvalidCharacterException.class, () -> addHabitatActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_invalidKepperId_throwsInvalidCharacterException() {
        // GIVEN
        AddHabitatRequest request = AddHabitatRequest.builder()
                .withHabitatName("goodName")
                .withKeeperManagerId("\"illegal\" id")
                .build();

        // WHEN + THEN
        assertThrows(InvalidCharacterException.class, () -> addHabitatActivity.handleRequest(request));
    }
}
