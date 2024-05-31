package com.nashss.se.animalenrichmenttrackerservice.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Enrichment;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.EnrichmentNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.metrics.MetricsConstants;
import com.nashss.se.animalenrichmenttrackerservice.metrics.MetricsPublisher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class EnrichmentDaoTest {

    @Mock
    private DynamoDBMapper dynamoDBMapper;
    @Mock
    private MetricsPublisher metricsPublisher;
    private EnrichmentDao enrichmentDao;
    @BeforeEach
    public void setup() {
        initMocks(this);
        enrichmentDao = new EnrichmentDao(dynamoDBMapper, metricsPublisher);
    }

    @Test
    public void getHabitat_withHabitatId_callsMapperWithPartitionKey() {
        // GIVEN
        String id = "enrichId";
        String keeperId = "keeperId";
        when(dynamoDBMapper.load(Enrichment.class, id)).thenReturn(new Enrichment());

        // WHEN
        Enrichment enrichment = enrichmentDao.getEnrichment(id);

        // THEN
        assertNotNull(enrichment);
        verify(dynamoDBMapper).load(Enrichment.class, id);
        verify(metricsPublisher).addCount(eq(MetricsConstants.GETENRICHMENT_ENRICHMENTNOTFOUND), anyDouble());

    }

    @Test
    public void getHabitat_habitatIdNotFound_throwsHabitatNotFoundException() {
        // GIVEN
        String nonexistentId = "nonexistent";
        String keeperId = "keeperId";
        when(dynamoDBMapper.load(Enrichment.class, nonexistentId)).thenReturn(null);

        // WHEN + THEN
        assertThrows(EnrichmentNotFoundException.class, () -> enrichmentDao.getEnrichment(nonexistentId));
        verify(metricsPublisher).addCount(eq(MetricsConstants.GETENRICHMENT_ENRICHMENTNOTFOUND), anyDouble());
    }
}
