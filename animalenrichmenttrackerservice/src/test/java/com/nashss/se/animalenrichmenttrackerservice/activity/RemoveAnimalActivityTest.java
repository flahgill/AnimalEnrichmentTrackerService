package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.RemoveAnimalRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.RemoveAnimalResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.AnimalDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Animal;
import com.nashss.se.animalenrichmenttrackerservice.helper.AnimalTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RemoveAnimalActivityTest {
    @Mock
    private AnimalDao animalDao;
    private RemoveAnimalActivity removeAnimalActivity;
    @BeforeEach
    public void setup() {
        initMocks(this);
        removeAnimalActivity = new RemoveAnimalActivity(animalDao);
    }

    @Test
    public void handleRequest_savedAnimal_returnsRemovedAnimalModelInResult() {
        // GIVEN
        Animal expAnimal = AnimalTestHelper.generateAnimal();
        expAnimal.setOnHabitat(false);
        String animalId = expAnimal.getAnimalId();

        when(animalDao.getAnimal(animalId)).thenReturn(expAnimal);
        when(animalDao.removeAnimal(animalId)).thenReturn(expAnimal);

        RemoveAnimalRequest request = RemoveAnimalRequest.builder()
                .withAnimalId(animalId)
                .build();

        // WHEN
        RemoveAnimalResult result = removeAnimalActivity.handleRequest(request);

        // THEN
        assertEquals(animalId, result.getRemovedAnimal().getAnimalId());
    }
}
