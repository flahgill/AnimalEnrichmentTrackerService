package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewAllAnimalsRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewAllAnimalsResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.AnimalDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Animal;
import com.nashss.se.animalenrichmenttrackerservice.helper.AnimalTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ViewAllAnimalsActivityTest {
    @Mock
    private AnimalDao animalDao;
    private ViewAllAnimalsActivity viewAllAnimalsActivity;
    @BeforeEach
    public void setup() {
        initMocks(this);
        viewAllAnimalsActivity = new ViewAllAnimalsActivity(animalDao);
    }

    @Test
    public void handleRequest_defaultActiveAnimalsRequest_returnsListOfAnimalModels() {
        // GIVEN
        Animal animal1 = AnimalTestHelper.generateAnimal();
        animal1.setAnimalName("hello");
        Animal animal2 = AnimalTestHelper.generateAnimal();
        animal2.setAnimalName("world");

        List<Animal> expAnimals = List.of(animal1, animal2);

        when(animalDao.getAllAnimals("active")).thenReturn(expAnimals);

        ViewAllAnimalsRequest request = ViewAllAnimalsRequest.builder()
                .build();

        // WHEN
        ViewAllAnimalsResult result = viewAllAnimalsActivity.handleRequest(request);

        // THEN
        assertEquals(2, result.getAnimals().size());
        assertNotNull(result);
        assertEquals(result.getAnimals().get(0).getAnimalName(), animal1.getAnimalName());
        assertEquals(result.getAnimals().get(1).getAnimalName(), animal2.getAnimalName());
    }

    @Test
    public void handleRequest_InactiveAnimalsRequestWithAllActiveAnimals_returnsListOfAnimalModels() {
        // GIVEN
        Animal animal1 = AnimalTestHelper.generateAnimal();
        animal1.setAnimalName("hello");
        Animal animal2 = AnimalTestHelper.generateAnimal();
        animal2.setAnimalName("world");

        List<Animal> expAnimals = List.of(animal1, animal2);

        when(animalDao.getAllAnimals("inactive")).thenReturn(new ArrayList<>());

        ViewAllAnimalsRequest request = ViewAllAnimalsRequest.builder()
                .withIsActive("inactive")
                .build();

        // WHEN
        ViewAllAnimalsResult result = viewAllAnimalsActivity.handleRequest(request);

        // THEN
        assertEquals(0, result.getAnimals().size());
        assertNotNull(result);
    }

    @Test
    public void handleRequest_InactiveAnimalsRequestWithOneActive_returnsListOfAnimalModels() {
        // GIVEN
        Animal animal1 = AnimalTestHelper.generateAnimal();
        animal1.setAnimalName("hello");
        animal1.setIsActive("inactive");
        Animal animal2 = AnimalTestHelper.generateAnimal();
        animal2.setAnimalName("world");

        List<Animal> expAnimals = List.of(animal1, animal2);

        when(animalDao.getAllAnimals("inactive")).thenReturn(List.of(animal1));

        ViewAllAnimalsRequest request = ViewAllAnimalsRequest.builder()
                .withIsActive("inactive")
                .build();

        // WHEN
        ViewAllAnimalsResult result = viewAllAnimalsActivity.handleRequest(request);

        // THEN
        assertEquals(1, result.getAnimals().size());
        assertNotNull(result);
        assertEquals(animal1.getAnimalName(), result.getAnimals().get(0).getAnimalName());
    }

}
