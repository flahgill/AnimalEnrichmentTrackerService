package com.nashss.se.animalenrichmenttrackerservice.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class HabitatDaoTest {
    @Mock
    private DynamoDBMapper dynamoDBMapper;
    private HabitatDao habitatDao;

    @BeforeEach
    public void setup() {
        initMocks(this);
        habitatDao = new HabitatDao(dynamoDBMapper);
    }

    @Test
    public void saveHabitat_callsMapperWithHabitat() {
        // GIVEN
        Habitat habitat = new Habitat();

        // WHEN
        Habitat result = habitatDao.saveHabitat(habitat);

        // THEN
        verify(dynamoDBMapper).save(habitat);
        assertEquals(habitat, result);
    }
}
