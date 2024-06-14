package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.UpdateAnimalRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.requests.UpdateHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.UpdateAnimalResult;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.UpdateHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.AnimalDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Animal;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
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

public class UpdateAnimalActivityTest {
    @Mock
    private HabitatDao habitatDao;
    @Mock
    private AnimalDao animalDao;
    private UpdateAnimalActivity updateAnimalActivity;
    @BeforeEach
    public void setup() {
        initMocks(this);
        updateAnimalActivity = new UpdateAnimalActivity(habitatDao, animalDao);
    }

    @Test
    public void handleRequest_goodNameRequestOnHabitat_updatesAnimal() {
        // GIVEN
        String id = "id";
        String expectedKeeperId = "expectedKeeperId";
        String expectedName = "new name";

        UpdateAnimalRequest request = UpdateAnimalRequest.builder()
                .withAnimalId(id)
                .withKeeperManagerId(expectedKeeperId)
                .withAnimalName(expectedName)
                .build();

        Habitat startingHabitat = HabitatTestHelper.generateHabitat();
        startingHabitat.setAnimalsInHabitat(List.of("old name", "another"));
        startingHabitat.setKeeperManagerId(expectedKeeperId);

        Animal startingAnimal = AnimalTestHelper.generateAnimal();
        startingAnimal.setAnimalName("old name");
        startingAnimal.setAnimalId(id);
        startingAnimal.setHabitatId(startingHabitat.getHabitatId());

        when(animalDao.getAnimal(id)).thenReturn(startingAnimal);
        when(habitatDao.getHabitat(startingAnimal.getHabitatId())).thenReturn(startingHabitat);
        when(animalDao.saveAnimal(startingAnimal)).thenReturn(startingAnimal);
        when(habitatDao.saveHabitat(startingHabitat)).thenReturn(startingHabitat);

        // WHEN
        UpdateAnimalResult result = updateAnimalActivity.handleRequest(request);

        // THEN
        assertEquals(expectedName, result.getAnimal().getAnimalName());
        assertEquals(2, startingHabitat.getAnimalsInHabitat().size());
        System.out.println(startingHabitat.getAnimalsInHabitat().toString());
        assertFalse(startingHabitat.getAnimalsInHabitat().contains("old name"));
        assertTrue(startingHabitat.getAnimalsInHabitat().contains(expectedName));
    }

    @Test
    public void handleRequest_goodNameRequestNotOnHabitat_updatesAnimal() {
        // GIVEN
        String id = "id";
        String expectedKeeperId = "expectedKeeperId";
        String expectedName = "new name";

        UpdateAnimalRequest request = UpdateAnimalRequest.builder()
                .withAnimalId(id)
                .withKeeperManagerId(expectedKeeperId)
                .withAnimalName(expectedName)
                .build();


        Animal startingAnimal = AnimalTestHelper.generateAnimal();
        startingAnimal.setAnimalName("old name");
        startingAnimal.setAnimalId(id);
        startingAnimal.setOnHabitat(false);

        when(animalDao.getAnimal(id)).thenReturn(startingAnimal);
        when(animalDao.saveAnimal(startingAnimal)).thenReturn(startingAnimal);

        // WHEN
        UpdateAnimalResult result = updateAnimalActivity.handleRequest(request);

        // THEN
        assertEquals(expectedName, result.getAnimal().getAnimalName());
    }

    @Test
    public void handleRequest_goodSexRequest_updatesAnimal() {
        // GIVEN
        String id = "id";
        String expectedKeeperId = "expectedKeeperId";
        String expectedSex = "female";

        UpdateAnimalRequest request = UpdateAnimalRequest.builder()
                .withAnimalId(id)
                .withKeeperManagerId(expectedKeeperId)
                .withSex(expectedSex)
                .build();


        Animal startingAnimal = AnimalTestHelper.generateAnimal();
        startingAnimal.setSex("male");
        startingAnimal.setAnimalId(id);
        startingAnimal.setOnHabitat(false);

        when(animalDao.getAnimal(id)).thenReturn(startingAnimal);
        when(animalDao.saveAnimal(startingAnimal)).thenReturn(startingAnimal);

        // WHEN
        UpdateAnimalResult result = updateAnimalActivity.handleRequest(request);

        // THEN
        assertEquals(expectedSex, result.getAnimal().getSex());
    }

    @Test
    public void handleRequest_goodSpeciesRequest_updatesAnimal() {
        // GIVEN
        String id = "id";
        String expectedKeeperId = "expectedKeeperId";
        String expectedSpecies = "giraffe";

        UpdateAnimalRequest request = UpdateAnimalRequest.builder()
                .withAnimalId(id)
                .withKeeperManagerId(expectedKeeperId)
                .withSpecies(expectedSpecies)
                .build();


        Animal startingAnimal = AnimalTestHelper.generateAnimal();
        startingAnimal.setSpecies("gazelle");
        startingAnimal.setAnimalId(id);
        startingAnimal.setOnHabitat(false);

        when(animalDao.getAnimal(id)).thenReturn(startingAnimal);
        when(animalDao.saveAnimal(startingAnimal)).thenReturn(startingAnimal);

        // WHEN
        UpdateAnimalResult result = updateAnimalActivity.handleRequest(request);

        // THEN
        assertEquals(expectedSpecies, result.getAnimal().getSpecies());
    }

    @Test
    public void handleRequest_differentKeeperThanOwner_throwsUserSecurityException() {
        // GIVEN
        String id = "id";
        String expectedKeeperId = "expectedKeeperId";
        String expectedName = "new name";

        UpdateAnimalRequest request = UpdateAnimalRequest.builder()
                .withAnimalId(id)
                .withKeeperManagerId(expectedKeeperId)
                .withAnimalName(expectedName)
                .build();

        Habitat startingHabitat = HabitatTestHelper.generateHabitat();
        startingHabitat.setAnimalsInHabitat(List.of("old name", "another"));

        Animal startingAnimal = AnimalTestHelper.generateAnimal();
        startingAnimal.setAnimalName("old name");
        startingAnimal.setAnimalId(id);
        startingAnimal.setHabitatId(startingHabitat.getHabitatId());

        when(animalDao.getAnimal(id)).thenReturn(startingAnimal);
        when(habitatDao.getHabitat(startingAnimal.getHabitatId())).thenReturn(startingHabitat);


        // WHEN + THEN
        assertThrows(UserSecurityException.class, () -> updateAnimalActivity.handleRequest(request));
    }
}
