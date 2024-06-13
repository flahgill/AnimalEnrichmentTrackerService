package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.RemoveAnimalFromHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.RemoveAnimalFromHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.AnimalDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Animal;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.AnimalNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.helper.AnimalTestHelper;
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
    @Mock
    private AnimalDao animalDao;
    private RemoveAnimalFromHabitatActivity removeAnimalFromHabitatActivity;

    @BeforeEach
    public void setup() {
        initMocks(this);
        removeAnimalFromHabitatActivity = new RemoveAnimalFromHabitatActivity(habitatDao, animalDao);
    }

    @Test
    public void handleRequest_animalInHabitat_removesAnimal() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();
        habitat.setAnimalsInHabitat(List.of("Bad", "Bunny"));
        habitat.setTotalAnimals(2);
        String animaltoRemove = "Bad";
        Animal animal = AnimalTestHelper.generateAnimal();
        animal.setAnimalName(animaltoRemove);

        RemoveAnimalFromHabitatRequest request = RemoveAnimalFromHabitatRequest.builder()
                .withAnimalId(animal.getAnimalId())
                .withHabitatId(habitat.getHabitatId())
                .withKeeperManagerId(habitat.getKeeperManagerId())
                .build();

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);
        when(habitatDao.saveHabitat(habitat)).thenReturn(habitat);
        when(animalDao.getAnimal(animal.getAnimalId())).thenReturn(animal);
        when(animalDao.saveAnimal(animal)).thenReturn(animal);

        // WHEN
        RemoveAnimalFromHabitatResult result = removeAnimalFromHabitatActivity.handleRequest(request);

        // THEN
        assertEquals(animal.getAnimalName(), result.getAnimalModel().getAnimalName());
        assertEquals(1, habitat.getTotalAnimals());
        assertFalse(habitat.getAnimalsInHabitat().contains(animaltoRemove));
    }

    @Test
    public void handleRequest_animalNotInHabitat_throwsAnimalNotFoundException() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();
        habitat.setAnimalsInHabitat(List.of("Bad", "Bunny"));
        habitat.setTotalAnimals(2);
        String animaltoRemove = "Beach";
        Animal animal = AnimalTestHelper.generateAnimal();
        animal.setAnimalName(animaltoRemove);

        RemoveAnimalFromHabitatRequest request = RemoveAnimalFromHabitatRequest.builder()
                .withAnimalId(animal.getAnimalId())
                .withHabitatId(habitat.getHabitatId())
                .withKeeperManagerId(habitat.getKeeperManagerId())
                .build();

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);
        when(animalDao.getAnimal(animal.getAnimalId())).thenReturn(animal);

        // WHEN + THEN
        assertThrows(AnimalNotFoundException.class, () -> removeAnimalFromHabitatActivity.handleRequest(request));
    }
}
