package com.nashss.se.animalenrichmenttrackerservice.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Animal;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.AnimalNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.metrics.MetricsPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AnimalDaoTest {
    @Mock
    private DynamoDBMapper dynamoDBMapper;
    @Mock
    private MetricsPublisher metricsPublisher;
    @Mock
    private PaginatedScanList<Animal> pagScanList;
    @Captor
    private ArgumentCaptor<DynamoDBScanExpression> scanExpCaptor;
    private AnimalDao animalDao;
    @BeforeEach
    public void setup() {
        initMocks(this);
        animalDao = new AnimalDao(dynamoDBMapper, metricsPublisher);
    }

    @Test
    public void getHabitatAnimals_withHabitatId_returnsListOfAnimals() {
        // GIVEN
        String habitatId = "123";
        when(dynamoDBMapper.scan(eq(Animal.class), any(DynamoDBScanExpression.class))).thenReturn(pagScanList);

        // WHEN
        List<Animal> result = animalDao.getHabitatAnimals(habitatId);

        // THEN
        assertNotNull(result);
        verify(dynamoDBMapper).scan(eq(Animal.class), scanExpCaptor.capture());
        assertFalse(result.isEmpty());
    }

    @Test
    public void saveHabitat_callsMapperWithHabitat() {
        // GIVEN
        Animal animal = new Animal();

        // WHEN
        Animal result = animalDao.saveAnimal(animal);

        // THEN
        verify(dynamoDBMapper).save(animal);
        assertEquals(animal, result);
    }

    @Test
    public void getAnimal_withAnimalId_callsMapper() {
        // GIVEN
        String animalId = "id";
        when(dynamoDBMapper.load(Animal.class, animalId)).thenReturn(new Animal());

        // WHEN
        Animal animal = animalDao.getAnimal(animalId);

        // THEN
        assertNotNull(animal);
        verify(dynamoDBMapper).load(Animal.class, animalId);
    }

    @Test
    public void getAnimal_animalIdNotFound_throwsAnimalNotFoundException() {
        // GIVEN
        String nonexistentId = "fake";
        when(dynamoDBMapper.load(Animal.class, nonexistentId)).thenReturn(null);

        // WHEN + THEN
        assertThrows(AnimalNotFoundException.class, ()-> animalDao.getAnimal(nonexistentId));
    }
}
