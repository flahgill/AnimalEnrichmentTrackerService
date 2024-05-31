package com.nashss.se.animalenrichmenttrackerservice.dynamodb;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Enrichment;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.EnrichmentNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.metrics.MetricsConstants;
import com.nashss.se.animalenrichmenttrackerservice.metrics.MetricsPublisher;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Accesses data for an enrichment using {@link Enrichment} to represent the model in DynamoDB.
 */
@Singleton
public class EnrichmentDao {
    private final DynamoDBMapper dynamoDBMapper;
    private final MetricsPublisher metricsPublisher;

    /**
     * Instantiates a new EnrichmentDao object.
     *
     * @param dynamoDBMapper the {@link DynamoDBMapper} used to interact with the Habitats table.
     * @param metricsPublisher the {@link MetricsPublisher} used to record metrics.
     */
    @Inject
    public EnrichmentDao(DynamoDBMapper dynamoDBMapper, MetricsPublisher metricsPublisher) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.metricsPublisher = metricsPublisher;
    }

    /**
     * Retrieves (loads) a given enrichment based on the enrichmentId.
     *
     * @param enrichmentId the enrichmentId to load the given enrichment
     * @return the enrichment object that was retrieved/loaded.
     */
    public Enrichment getEnrichment(String enrichmentId) {
        Enrichment enrichment = this.dynamoDBMapper.load(Enrichment.class, enrichmentId);

        if (Objects.isNull(enrichment)) {
            metricsPublisher.addCount(MetricsConstants.GETENRICHMENT_ENRICHMENTNOTFOUND, 1);
            throw new EnrichmentNotFoundException("Could not find Enrichment with id [" + enrichmentId + "].");
        }

        metricsPublisher.addCount(MetricsConstants.GETENRICHMENT_ENRICHMENTNOTFOUND, 0);

        return enrichment;
    }
}
