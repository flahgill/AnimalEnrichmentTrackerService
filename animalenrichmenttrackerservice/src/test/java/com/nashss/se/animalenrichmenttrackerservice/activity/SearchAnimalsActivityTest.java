package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.SearchAnimalsRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.SearchAnimalsResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.AnimalDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Animal;
import com.nashss.se.animalenrichmenttrackerservice.models.AnimalModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SearchAnimalsActivityTest {
    @Mock
    private AnimalDao animalDao;
    private SearchAnimalsActivity searchAnimalsActivity;
    @BeforeEach
    public void setup() {
        initMocks(this);
        searchAnimalsActivity = new SearchAnimalsActivity(animalDao);
    }

    @Test
    public void handleRequest_whenAnimalsMatchSearch_returnsAnimalModelListInResult() {
        // GIVEN
        String criteria = "10";
        String[] criteriaArray = {criteria};

        List<Animal> expected = List.of(
                newAnimal("id1", "hello", "giraffe", "M", 10),
                newAnimal("id2", "hellos", "giraffe", "M", 10));

        when(animalDao.searchAnimals(criteriaArray)).thenReturn(expected);

        SearchAnimalsRequest request = SearchAnimalsRequest.builder()
                .withCriteria(criteria)
                .build();

        // WHEN
        SearchAnimalsResult result = searchAnimalsActivity.handleRequest(request);

        // THEN
        List<AnimalModel> resultAnimals = result.getAnimals();
        assertEquals(expected.size(), resultAnimals.size());

        for (int i=0; i<expected.size(); i++) {
            assertEquals(expected.get(i).getAnimalId(), resultAnimals.get(i).getAnimalId());
            assertEquals(expected.get(i).getAnimalName(), resultAnimals.get(i).getAnimalName());
        }
    }

    @Test
    public void handleRequest_withNullCriteria_isIdenticalToEmptyCriteria() {
        // GIVEN
        String criteria = null;
        ArgumentCaptor<String[]> criteriaArray = ArgumentCaptor.forClass(String[].class);

        when(animalDao.searchAnimals(criteriaArray.capture())).thenReturn(List.of());

        SearchAnimalsRequest request = SearchAnimalsRequest.builder()
                .withCriteria(criteria)
                .build();

        // WHEN
        SearchAnimalsResult result = searchAnimalsActivity.handleRequest(request);

        // THEN
        assertEquals(0, criteriaArray.getValue().length, "Criteria Array should be empty");
    }

    private static Animal newAnimal(String animalId, String animalName, String species, String sex, int age) {
        Animal animal = new Animal();

        animal.setAnimalId(animalId);
        animal.setAnimalName(animalName);
        animal.setSpecies(species);
        animal.setSex(sex);
        animal.setAge(age);

        // the test code doesn't need these properties to be distinct.
        animal.setHabitatId("habitatId");
        animal.setIsActive("active");
        animal.setOnHabitat(true);

        return animal;
    }
}
