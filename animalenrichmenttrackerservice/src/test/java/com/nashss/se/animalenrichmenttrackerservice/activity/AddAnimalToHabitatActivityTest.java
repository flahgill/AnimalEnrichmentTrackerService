package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.AddAnimalToHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.AddAnimalToHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.AnimalDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Animal;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.DuplicateAnimalException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.InvalidCharacterException;
import com.nashss.se.animalenrichmenttrackerservice.helper.AnimalTestHelper;
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
    @Mock
    private AnimalDao animalDao;
    private AddAnimalToHabitatActivity addAnimalToHabitatActivity;

    @BeforeEach
    public void setup() {
        initMocks(this);
        addAnimalToHabitatActivity = new AddAnimalToHabitatActivity(habitatDao, animalDao);
    }

    @Test
    public void handleRequest_acceptableAnimalNoOtherAnimalsInHabitat_addsAnimalToHabitat() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();
        Animal expectedAnimal = AnimalTestHelper.generateAnimal();


        AddAnimalToHabitatRequest request = AddAnimalToHabitatRequest.builder()
                .withAnimalName(expectedAnimal.getAnimalName())
                .withAge(expectedAnimal.getAge())
                .withSpecies(expectedAnimal.getSpecies())
                .withSex(expectedAnimal.getSex())
                .withHabitatId(habitat.getHabitatId())
                .withKeeperManagerId(habitat.getKeeperManagerId())
                .build();

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);
        when(habitatDao.saveHabitat(habitat)).thenReturn(habitat);
        when(animalDao.saveAnimal(expectedAnimal)).thenReturn(expectedAnimal);

        // WHEN
        AddAnimalToHabitatResult result = addAnimalToHabitatActivity.handleRequest(request);

        // THEN
        assertEquals(expectedAnimal.getAnimalName(), result.getAddedAnimal().getAnimalName());
        assertEquals(result.getAddedAnimal().getAnimalName(), habitat.getAnimalsInHabitat().get(0));
    }

    @Test
    public void handleRequest_acceptableAnimalOtherAnimalsInHabitat_addsAnimalToHabitat() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();
        habitat.setAnimalsInHabitat(List.of("hello", "world"));
        Animal hello = AnimalTestHelper.generateAnimal();
        hello.setAnimalName("hello");
        hello.setHabitatId(habitat.getHabitatId());
        Animal world = AnimalTestHelper.generateAnimal();
        world.setAnimalName("world");
        world.setHabitatId(habitat.getHabitatId());
        habitat.setTotalAnimals(2);

        Animal expectedAnimal = AnimalTestHelper.generateAnimal();

        AddAnimalToHabitatRequest request = AddAnimalToHabitatRequest.builder()
                .withAnimalName(expectedAnimal.getAnimalName())
                .withAge(expectedAnimal.getAge())
                .withSex(expectedAnimal.getSex())
                .withSpecies(expectedAnimal.getSpecies())
                .withHabitatId(habitat.getHabitatId())
                .withKeeperManagerId(habitat.getKeeperManagerId())
                .build();

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);
        when(habitatDao.saveHabitat(habitat)).thenReturn(habitat);
        when(animalDao.saveAnimal(expectedAnimal)).thenReturn(expectedAnimal);

        // WHEN
        AddAnimalToHabitatResult result = addAnimalToHabitatActivity.handleRequest(request);

        // THEN
        assertEquals(expectedAnimal.getAnimalName(), result.getAddedAnimal().getAnimalName());
        assertEquals(result.getAddedAnimal().getAnimalName(), habitat.getAnimalsInHabitat().get(0));
        assertEquals(3, habitat.getTotalAnimals());
    }

    @Test
    public void handleRequest_invalidAnimalName_throwsInvalidCharacterException() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();

        AddAnimalToHabitatRequest request = AddAnimalToHabitatRequest.builder()
                .withAnimalName("\"illegal\" name")
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
                .withAnimalName(expectedAnimal)
                .withHabitatId(habitat.getHabitatId())
                .withKeeperManagerId(habitat.getKeeperManagerId())
                .build();

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);

        // WHEN + THEN
        assertThrows(DuplicateAnimalException.class, () -> addAnimalToHabitatActivity.handleRequest(request));
    }
}
