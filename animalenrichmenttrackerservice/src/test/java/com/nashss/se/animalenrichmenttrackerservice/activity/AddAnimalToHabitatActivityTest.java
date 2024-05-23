package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.AddAnimalToHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.AddAnimalToHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.DuplicateAnimalException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.InvalidCharacterException;
import com.nashss.se.animalenrichmenttrackerservice.helper.HabitatTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AddAnimalToHabitatActivityTest {
    @Mock
    private HabitatDao habitatDao;
    private AddAnimalToHabitatActivity addAnimalToHabitatActivity;

    @BeforeEach
    public void setup() {
        initMocks(this);
        addAnimalToHabitatActivity = new AddAnimalToHabitatActivity(habitatDao);
    }

    @Test
    public void handleRequest_acceptableAnimalNoOtherAnimalsInHabitat_addsAnimalToHabitat() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();
        String expectedAnimal = "Benny";

        AddAnimalToHabitatRequest request = AddAnimalToHabitatRequest.builder()
                .withAnimalToAdd(expectedAnimal)
                .withHabitatId(habitat.getHabitatId())
                .withKeeperManagerId(habitat.getKeeperManagerId())
                .build();

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);
        when(habitatDao.saveHabitat(habitat)).thenReturn(habitat);

        // WHEN
        AddAnimalToHabitatResult result = addAnimalToHabitatActivity.handleRequest(request);

        // THEN
        assertEquals(1, result.getAnimalsInHabitat().size());
        assertEquals(expectedAnimal, result.getAnimalsInHabitat().get(0));
        assertEquals(1, habitat.getTotalAnimals());
    }

    @Test
    public void handleRequest_acceptableAnimalOtherAnimalsInHabitat_addsAnimalToHabitat() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();
        habitat.setAnimalsInHabitat(List.of("hello", "world"));
        habitat.setTotalAnimals(2);
        String expectedAnimal = "Benny";

        AddAnimalToHabitatRequest request = AddAnimalToHabitatRequest.builder()
                .withAnimalToAdd(expectedAnimal)
                .withHabitatId(habitat.getHabitatId())
                .withKeeperManagerId(habitat.getKeeperManagerId())
                .build();

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);
        when(habitatDao.saveHabitat(habitat)).thenReturn(habitat);

        // WHEN
        AddAnimalToHabitatResult result = addAnimalToHabitatActivity.handleRequest(request);

        // THEN
        assertEquals(3, result.getAnimalsInHabitat().size());
        assertEquals(expectedAnimal, result.getAnimalsInHabitat().get(2));
        assertEquals(3, habitat.getTotalAnimals());
    }

    @Test
    public void handleRequest_invalidAnimalName_throwsInvalidCharacterException() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();

        AddAnimalToHabitatRequest request = AddAnimalToHabitatRequest.builder()
                .withAnimalToAdd("\"illegal\" name")
                .withHabitatId(habitat.getHabitatId())
                .withKeeperManagerId(habitat.getKeeperManagerId())
                .build();

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);

        // WHEN + THEN
        assertThrows(InvalidCharacterException.class, () -> addAnimalToHabitatActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_duplicateAnimal_throwsDuplicateAnimalException() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();
        habitat.setAnimalsInHabitat(List.of("benny", "world"));
        habitat.setTotalAnimals(2);
        String expectedAnimal = "Benny";

        AddAnimalToHabitatRequest request = AddAnimalToHabitatRequest.builder()
                .withAnimalToAdd(expectedAnimal)
                .withHabitatId(habitat.getHabitatId())
                .withKeeperManagerId(habitat.getKeeperManagerId())
                .build();

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);

        // WHEN + THEN
        assertThrows(DuplicateAnimalException.class, () -> addAnimalToHabitatActivity.handleRequest(request));
    }
}
