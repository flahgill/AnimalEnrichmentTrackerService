package com.nashss.se.animalenrichmenttrackerservice.dynamodb;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.EnrichmentActivityNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.metrics.MetricsPublisher;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    /**
     * Perform a scan of the EnrichmentActivities table for activities matching the completion status in query.
     *
     * @param isComplete the completion status of the activity.
     * @return a list of enrichment activities matching the status requested.
     */
    public List<EnrichmentActivity> getAllEnrichmentActivities(String isComplete) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":isComplete", new AttributeValue().withS(isComplete));

        scanExpression.setExpressionAttributeValues(valueMap);
        scanExpression.setFilterExpression("isComplete = :isComplete");

        return this.dynamoDBMapper.scan(EnrichmentActivity.class, scanExpression);
    }
}
