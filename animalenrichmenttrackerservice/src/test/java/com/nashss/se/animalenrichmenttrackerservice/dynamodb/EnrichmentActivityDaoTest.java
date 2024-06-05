package com.nashss.se.animalenrichmenttrackerservice.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.EnrichmentActivityNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.metrics.MetricsPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class EnrichmentActivityDaoTest {

    @Mock
    private DynamoDBMapper dynamoDBMapper;
    @Mock
    private MetricsPublisher metricsPublisher;
    private EnrichmentActivityDao enrichmentActivityDao;

    @BeforeEach
    public void setup() {
        initMocks(this);
        enrichmentActivityDao = new EnrichmentActivityDao(dynamoDBMapper, metricsPublisher);
    }

    @Test
    public void saveEnrichmentActivity_callsMapperWithEnrichmentActivity() {
        // GIVEN
        EnrichmentActivity activity = new EnrichmentActivity();

        // WHEN
        EnrichmentActivity result = enrichmentActivityDao.saveEnrichmentActivity(activity);

        // THEN
        verify(dynamoDBMapper).save(activity);
        assertEquals(activity, result);
    }

    @Test
    public void getEnrichmentActivity_withActivityId_callsMapperWithPartitionKey() {
        // GIVEN
        String activityId = "activityId";
        when(dynamoDBMapper.load(EnrichmentActivity.class, activityId)).thenReturn(new EnrichmentActivity());

        // WHEN
        EnrichmentActivity activity = enrichmentActivityDao.getEnrichmentActivity(activityId);

        // THEN
        assertNotNull(activity);
        verify(dynamoDBMapper).load(EnrichmentActivity.class, activityId);
    }

    @Test
    public void getEnrichmentActivity_activityIdNotFound_throwsEnrichmentActivityNotFoundException() {
        // GIVEN
        String nonexistentId = "nonexistent";
        when(dynamoDBMapper.load(EnrichmentActivity.class, nonexistentId)).thenReturn(null);

        // WHEN + THEN
        assertThrows(EnrichmentActivityNotFoundException.class, () -> enrichmentActivityDao.getEnrichmentActivity(nonexistentId));
    }
}
