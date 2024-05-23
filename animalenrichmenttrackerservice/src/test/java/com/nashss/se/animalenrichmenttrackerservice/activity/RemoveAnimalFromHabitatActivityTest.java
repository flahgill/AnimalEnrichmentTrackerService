package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.AddAnimalToHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.requests.RemoveAnimalFromHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.RemoveAnimalFromHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.AnimalNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.helper.HabitatTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RemoveAnimalFromHabitatActivityTest {
    @Mock
    private HabitatDao habitatDao;
    private RemoveAnimalFromHabitatActivity removeAnimalFromHabitatActivity;

    @BeforeEach
    public void setup() {
        initMocks(this);
        removeAnimalFromHabitatActivity = new RemoveAnimalFromHabitatActivity(habitatDao);
    }

    @Test
    public void handleRequest_animalInHabitat_removesAnimal() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();
        habitat.setAnimalsInHabitat(List.of("Bad", "Bunny"));
        habitat.setTotalAnimals(2);
        String animaltoRemove = "Bad";

        RemoveAnimalFromHabitatRequest request = RemoveAnimalFromHabitatRequest.builder()
                .withAnimalToRemove(animaltoRemove)
                .withHabitatId(habitat.getHabitatId())
                .withKeeperManagerId(habitat.getKeeperManagerId())
                .build();

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);
        when(habitatDao.saveHabitat(habitat)).thenReturn(habitat);

        // WHEN
        RemoveAnimalFromHabitatResult result = removeAnimalFromHabitatActivity.handleRequest(request);

        // THEN
        assertEquals(1, result.getAnimalsInHabitat().size());
        assertEquals(1, habitat.getTotalAnimals());
        assertFalse(result.getAnimalsInHabitat().contains(animaltoRemove));
    }

    @Test
    public void handleRequest_animalNotInHabitat_throwsAnimalNotFoundException() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();
        habitat.setAnimalsInHabitat(List.of("Bad", "Bunny"));
        habitat.setTotalAnimals(2);
        String animaltoRemove = "Beach";

        RemoveAnimalFromHabitatRequest request = RemoveAnimalFromHabitatRequest.builder()
                .withAnimalToRemove(animaltoRemove)
                .withHabitatId(habitat.getHabitatId())
                .withKeeperManagerId(habitat.getKeeperManagerId())
                .build();

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);

        // WHEN + THEN
        assertThrows(AnimalNotFoundException.class, () -> removeAnimalFromHabitatActivity.handleRequest(request));
    }
}
