package com.nashss.se.animalenrichmenttrackerservice.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.HabitatNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.metrics.MetricsConstants;
import com.nashss.se.animalenrichmenttrackerservice.metrics.MetricsPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class HabitatDaoTest {
    @Mock
    private DynamoDBMapper dynamoDBMapper;
    @Mock
    private MetricsPublisher metricsPublisher;
    @Mock
    private PaginatedQueryList<Habitat> pagQueryList;
    @Captor
    ArgumentCaptor<DynamoDBQueryExpression> queryExpCaptor;
    private HabitatDao habitatDao;

    @BeforeEach
    public void setup() {
        initMocks(this);
        habitatDao = new HabitatDao(dynamoDBMapper, metricsPublisher);
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

    @Test
    public void getHabitat_withHabitatId_callsMapperWithPartitionKey() {
        // GIVEN
        String habitatId = "habitatId";
        String keeperId = "keeperId";
        when(dynamoDBMapper.load(Habitat.class, habitatId)).thenReturn(new Habitat());

        // WHEN
        Habitat habitat = habitatDao.getHabitat(habitatId);

        // THEN
        assertNotNull(habitat);
        verify(dynamoDBMapper).load(Habitat.class, habitatId);
        verify(metricsPublisher).addCount(eq(MetricsConstants.GETHABITAT_HABTITATNOTFOUND), anyDouble());

    }

    @Test
    public void getHabitat_habitatIdNotFound_throwsHabitatNotFoundException() {
        // GIVEN
        String nonexistentId = "nonexistent";
        String keeperId = "keeperId";
        when(dynamoDBMapper.load(Habitat.class, nonexistentId)).thenReturn(null);

        // WHEN + THEN
        assertThrows(HabitatNotFoundException.class, () -> habitatDao.getHabitat(nonexistentId));
        verify(metricsPublisher).addCount(eq(MetricsConstants.GETHABITAT_HABTITATNOTFOUND), anyDouble());
    }

    @Test
    public void getAllHabitatsForKeeper_withKeeperId_returnsListOfHabitats() {
        // GIVEN
        String testId = "testId";
        when(dynamoDBMapper.query(eq(Habitat.class), any(DynamoDBQueryExpression.class))).thenReturn(pagQueryList);

        // WHEN
        List<Habitat> results = habitatDao.getAllHabitatsForKeeper(testId);

        // THEN
        assertNotNull(results);
        verify(dynamoDBMapper).query(eq(Habitat.class), queryExpCaptor.capture());
        assertFalse(results.isEmpty());
    }
}
