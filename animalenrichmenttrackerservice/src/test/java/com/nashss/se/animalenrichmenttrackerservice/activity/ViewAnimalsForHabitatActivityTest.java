package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewAnimalsForHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewAnimalsForHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.AnimalDao;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Animal;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.helper.AnimalTestHelper;
import com.nashss.se.animalenrichmenttrackerservice.helper.HabitatTestHelper;
import com.nashss.se.animalenrichmenttrackerservice.models.AnimalModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ViewAnimalsForHabitatActivityTest {
    @Mock
    private AnimalDao animalDao;
    private ViewAnimalsForHabitatActivity viewAnimalsForHabitatActivity;

    @BeforeEach
    public void setup() {
        initMocks(this);
        viewAnimalsForHabitatActivity = new ViewAnimalsForHabitatActivity(animalDao);
    }

    @Test
    public void handleRequest_savedHabitatFound_returnsHabitatListOfAnimalsInResult() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitatWithNEnrichments(3);
        Animal a1 = AnimalTestHelper.generateAnimal();
        a1.setHabitatId(habitat.getHabitatId());
        a1.setAnimalName("Dwight");
        Animal a2 = AnimalTestHelper.generateAnimal();
        a2.setHabitatId(habitat.getHabitatId());
        a2.setAnimalName("Jim");
        Animal a3 = AnimalTestHelper.generateAnimal();
        a3.setHabitatId(habitat.getHabitatId());
        a3.setAnimalName("Pam");
        habitat.setAnimalsInHabitat(List.of("Dwight", "Jim", "Pam"));

        List<Animal> expAnimals = List.of(a1, a2, a3);

        when(animalDao.getHabitatAnimals(habitat.getHabitatId())).thenReturn(expAnimals);

        ViewAnimalsForHabitatRequest request = ViewAnimalsForHabitatRequest.builder()
                .withHabitatId(habitat.getHabitatId())
                .build();

        // WHEN
        ViewAnimalsForHabitatResult result = viewAnimalsForHabitatActivity.handleRequest(request);

        // THEN
        assertEquals(expAnimals.size(), result.getAnimalsInHabitat().size());
        assertEquals(a1.getAnimalName(), result.getAnimalsInHabitat().get(0).getAnimalName());
        assertEquals(a2.getAnimalName(), result.getAnimalsInHabitat().get(1).getAnimalName());
        assertEquals(a3.getAnimalName(), result.getAnimalsInHabitat().get(2).getAnimalName());
    }

    @Test
    public void handleRequest_habitatWithNoAnimals_returnsEmptyList() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();
        habitat.setAnimalsInHabitat(new ArrayList<>());

        when(animalDao.getHabitatAnimals(habitat.getHabitatId())).thenReturn(new ArrayList<>());

        ViewAnimalsForHabitatRequest request = ViewAnimalsForHabitatRequest.builder()
                .withHabitatId(habitat.getHabitatId())
                .build();

        // WHEN
        ViewAnimalsForHabitatResult result = viewAnimalsForHabitatActivity.handleRequest(request);

        // THEN
        assertNotNull(result.getAnimalsInHabitat());
        assertEquals(habitat.getAnimalsInHabitat().size(), result.getAnimalsInHabitat().size());
    }
}
