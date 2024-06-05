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
     * Hard deletes the associated {@link EnrichmentActivity} with associated activityId.
     *
     * @param activityId the activityId to designate which EnrichmentActivity to delete.
     * @return the enrichmentActivity being removed from DDB.
     */
    public EnrichmentActivity removeEnrichmentActivity(String activityId) {
        EnrichmentActivity enrichmentActivity = this.dynamoDBMapper.load(EnrichmentActivity.class, activityId);

        if (Objects.isNull(enrichmentActivity)) {
            throw new EnrichmentActivityNotFoundException("Could not find activity with id [" + activityId + "].");
        }

        this.dynamoDBMapper.delete(enrichmentActivity);
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

    /**
     * Performs a search (via a "scan") of the EnrichmentActivities table for activities matching the given criteria.
     *
     * "name", "id" and "description" attributes are searched.
     * The criteria are an array of Strings. each element of the array is searched individually.
     * ALL elements of the criteria array must appear in the name, id, or description (or all).
     * Searches are CASE SENSITIVE.
     *
     * @param criteria an array of String containing search criteria.
     * @return a List of EnrichmentActivity objects that match the search criteria.
     */
    public List<EnrichmentActivity> searchEnrichmentActivities(String[] criteria) {
        DynamoDBScanExpression dynamoDBScanExpression = new DynamoDBScanExpression();

        if (criteria.length > 0) {
            Map<String, AttributeValue> valueMap = new HashMap<>();
            String valueMapPrefix = ":c";

            StringBuilder nameFilterExpression = new StringBuilder();
            StringBuilder idFilterExpression = new StringBuilder();
            StringBuilder descFilterExpression = new StringBuilder();

            for (int i = 0; i < criteria.length; i++) {
                valueMap.put(valueMapPrefix + i,
                        new AttributeValue().withS(criteria[i]));
                nameFilterExpression.append(
                        filterExpressionPart("activityName", valueMapPrefix, i));
                idFilterExpression.append(
                        filterExpressionPart("enrichmentId", valueMapPrefix, i));
                descFilterExpression.append(
                        filterExpressionPart("description", valueMapPrefix, i));
            }

            dynamoDBScanExpression.setExpressionAttributeValues(valueMap);
            dynamoDBScanExpression.setFilterExpression(
                    "(" + nameFilterExpression + ") or (" + idFilterExpression +
                            ") or (" + descFilterExpression + ")");
        }

        return this.dynamoDBMapper.scan(EnrichmentActivity.class, dynamoDBScanExpression);
    }

    /**
     * private helper method to parse filter expressions.
     *
     * @param target the targeted attribute of the searched table.
     * @param valueMapNamePrefix prefix of the value map.
     * @param position position to parse.
     * @return StringBuilder to append to the filter expression.
     */
    private StringBuilder filterExpressionPart(String target, String valueMapNamePrefix, int position) {
        String possiblyAnd = position == 0 ? "" : "and ";
        return new StringBuilder()
                .append(possiblyAnd)
                .append("contains(")
                .append(target)
                .append(", ")
                .append(valueMapNamePrefix).append(position)
                .append(") ");
    }
}
