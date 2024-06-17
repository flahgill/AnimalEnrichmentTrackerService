package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ReactivateAnimalRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ReactivateAnimalResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.AnimalDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Animal;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.AnimalCurrentlyOnHabitatException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.IncompatibleSpeciesException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.UserSecurityException;
import com.nashss.se.animalenrichmenttrackerservice.helper.AnimalTestHelper;
import com.nashss.se.animalenrichmenttrackerservice.helper.HabitatTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReactivateAnimalActivityTest {
    @Mock
    private HabitatDao habitatDao;
    @Mock
    private AnimalDao animalDao;
    private ReactivateAnimalActivity reactivateAnimalActivity;
    @BeforeEach
    public void setup() {
        initMocks(this);
        reactivateAnimalActivity = new ReactivateAnimalActivity(habitatDao, animalDao);
    }

    @Test
    public void handleRequest_goodRequest_AddsAnimalToHabitat() {
        //GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();
        habitat.setSpecies(List.of("giraffe"));
        habitat.setAnimalsInHabitat(List.of("Benny", "Gino"));
        habitat.setTotalAnimals(2);
        habitat.setKeeperManagerId("123");

        Animal animal = AnimalTestHelper.generateAnimal();
        animal.setSpecies("giraffe");
        animal.setAnimalName("Gerald");
        animal.setOnHabitat(false);
        animal.setIsActive("inactive");
        animal.setHabitatId(" ");

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);
        when(animalDao.getAnimal(animal.getAnimalId())).thenReturn(animal);
        when(habitatDao.saveHabitat(habitat)).thenReturn(habitat);
        when(animalDao.saveAnimal(animal)).thenReturn(animal);
        when(habitatDao.getAllHabitatsForKeeper("123")).thenReturn(List.of(habitat));

        ReactivateAnimalRequest request = ReactivateAnimalRequest.builder()
                .withAnimalId(animal.getAnimalId())
                .withHabitatId(habitat.getHabitatId())
                .withKeeperManagerId("123")
                .build();

        // WHEN
        ReactivateAnimalResult result = reactivateAnimalActivity.handleRequest(request);

        // THEN
        assertEquals("active", result.getAnimal().getIsActive());
        assertTrue(result.getAnimal().getOnHabitat());
        assertEquals(habitat.getHabitatId(), result.getAnimal().getHabitatId());
        assertEquals(3, habitat.getTotalAnimals());
        assertTrue(habitat.getAnimalsInHabitat().contains(animal.getAnimalName()));
    }

    @Test
    public void handleRequest_animalOnHabitatAlready_throwsAnimalCurrentlyOnHabitatException() {
        //GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();

        Animal animal = AnimalTestHelper.generateAnimal();


        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);
        when(animalDao.getAnimal(animal.getAnimalId())).thenReturn(animal);

        ReactivateAnimalRequest request = ReactivateAnimalRequest.builder()
                .withAnimalId(animal.getAnimalId())
                .withHabitatId(habitat.getHabitatId())
                .withKeeperManagerId("123")
                .build();

        // WHEN + THEN
        assertThrows(AnimalCurrentlyOnHabitatException.class, () -> reactivateAnimalActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_speciesMismatch_throwsIncompatibleSpeciesException() {
        //GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();
        habitat.setSpecies(List.of("giraffe"));
        habitat.setAnimalsInHabitat(List.of("Benny", "Gino"));
        habitat.setTotalAnimals(2);
        habitat.setKeeperManagerId("123");

        Animal animal = AnimalTestHelper.generateAnimal();
        animal.setSpecies("takin");
        animal.setAnimalName("Gerald");
        animal.setOnHabitat(false);
        animal.setIsActive("inactive");
        animal.setHabitatId(" ");

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);
        when(animalDao.getAnimal(animal.getAnimalId())).thenReturn(animal);
        when(habitatDao.getAllHabitatsForKeeper("123")).thenReturn(List.of(habitat));

        ReactivateAnimalRequest request = ReactivateAnimalRequest.builder()
                .withAnimalId(animal.getAnimalId())
                .withHabitatId(habitat.getHabitatId())
                .withKeeperManagerId("123")
                .build();

        // WHEN + THEN
        assertThrows(IncompatibleSpeciesException.class, () -> reactivateAnimalActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_keeperMismatch_throwsUserSecurityException() {
        //GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();
        habitat.setSpecies(List.of("giraffe"));
        habitat.setAnimalsInHabitat(List.of("Benny", "Gino"));
        habitat.setTotalAnimals(2);
        habitat.setKeeperManagerId("123");

        Animal animal = AnimalTestHelper.generateAnimal();
        animal.setSpecies("giraffe");
        animal.setAnimalName("Gerald");
        animal.setOnHabitat(false);
        animal.setIsActive("inactive");
        animal.setHabitatId(" ");

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);
        when(animalDao.getAnimal(animal.getAnimalId())).thenReturn(animal);
        when(habitatDao.saveHabitat(habitat)).thenReturn(habitat);
        when(animalDao.saveAnimal(animal)).thenReturn(animal);

        ReactivateAnimalRequest request = ReactivateAnimalRequest.builder()
                .withAnimalId(animal.getAnimalId())
                .withHabitatId(habitat.getHabitatId())
                .withKeeperManagerId("123")
                .build();

        // WHEN + THEN
        assertThrows(UserSecurityException.class, () -> reactivateAnimalActivity.handleRequest(request));
    }
}
