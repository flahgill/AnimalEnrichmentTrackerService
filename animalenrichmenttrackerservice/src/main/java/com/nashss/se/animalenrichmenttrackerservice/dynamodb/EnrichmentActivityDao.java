package com.nashss.se.animalenrichmenttrackerservice.dynamodb;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.EnrichmentActivityNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.metrics.MetricsPublisher;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Accesses data for a EnrichmentActivity using {@link EnrichmentActivity} to represent the model in DynamoDB.
 */
@Singleton
public class EnrichmentActivityDao {
    private final DynamoDBMapper dynamoDBMapper;
    private final MetricsPublisher metricsPublisher;

    /**
     * Instantiates a new EnrichmentActivityDao object.
     *
     * @param dynamoDBMapper the {@link DynamoDBMapper} used to interact with the EnrichmentActivities table.
     * @param metricsPublisher the {@link MetricsPublisher} used to record metrics.
     */
    @Inject
    public EnrichmentActivityDao(DynamoDBMapper dynamoDBMapper, MetricsPublisher metricsPublisher) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.metricsPublisher = metricsPublisher;
    }

    /**
     * Saves (creates/updates) given enrichmentActivity.
     *
     * @param activity EnrichmentActivity to save/update.
     * @return the enrichmentActivity object that was saved/updated.
     */
    public EnrichmentActivity saveEnrichmentActivity(EnrichmentActivity activity) {
        this.dynamoDBMapper.save(activity);
        return activity;
    }

    /**
     * Retrieves (loads) a given EnrichmentActivity based on the activityId.
     *
     * @param activityId the activityId to load the given EnrichmentActivity.
     * @return the enrichmentActivity that was retrieved/loaded.
     */
    public EnrichmentActivity getEnrichmentActivity(String activityId) {
        EnrichmentActivity enrichmentActivity = this.dynamoDBMapper.load(EnrichmentActivity.class, activityId);

        if (Objects.isNull(enrichmentActivity)) {
            throw new EnrichmentActivityNotFoundException("Could not find activity with id [" + activityId + "].");
        }

        return enrichmentActivity;
    }
}
