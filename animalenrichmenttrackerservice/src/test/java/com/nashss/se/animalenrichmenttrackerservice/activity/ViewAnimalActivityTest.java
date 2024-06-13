package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewAnimalRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewAnimalResult;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.AnimalDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Animal;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.helper.EnrichmentActivityTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ViewAnimalActivityTest {
    @Mock
    private AnimalDao animalDao;
    private ViewAnimalActivity viewAnimalActivity;
    @BeforeEach
    public void setup() {
        initMocks(this);
        viewAnimalActivity = new ViewAnimalActivity(animalDao);
    }

    @Test
    public void handleRequest_savedAnimalFound_returnsAnimalModelInResult() {
        // GIVEN
        String animalId = "expectedId";
        String name = "expectedName";
        int age = 0;
        String sex = "F";
        String species = "Giraffe";
        String isActive = "active";
        Boolean onHabitat = true;

        Animal animal = new Animal();
        animal.setAnimalId(animalId);
        animal.setAnimalName(name);
        animal.setAge(age);
        animal.setSpecies(species);
        animal.setSex(sex);
        animal.setIsActive(isActive);
        animal.setOnHabitat(onHabitat);

        when(animalDao.getAnimal(animalId)).thenReturn(animal);

        ViewAnimalRequest request = ViewAnimalRequest.builder()
                .withAnimalId(animalId)
                .build();

        // WHEN
        ViewAnimalResult result = viewAnimalActivity.handleRequest(request);

        // THEN
        assertEquals(animalId, result.getAnimal().getAnimalId());
        assertEquals(name, result.getAnimal().getAnimalName());
        assertEquals(age, result.getAnimal().getAge());
        assertEquals(sex, result.getAnimal().getSex());
        assertEquals(species, result.getAnimal().getSpecies());
        assertEquals(isActive, result.getAnimal().getIsActive());
        assertEquals(onHabitat, result.getAnimal().getOnHabitat());
    }
}
