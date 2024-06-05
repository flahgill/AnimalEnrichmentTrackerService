package com.nashss.se.animalenrichmenttrackerservice.dynamodb;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Enrichment;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.EnrichmentNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.metrics.MetricsConstants;
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

    /**
     * Performs a search (via a "scan") of the Enrichments table for enrichments matching the given criteria.
     *
     * "activityName", "enrichmentId" and "description" attributes are searched.
     * The criteria are an array of Strings. each element of the array is searched individually.
     * ALL elements of the criteria array must appear in the name, id, or description (or all).
     * Searches are CASE SENSITIVE.
     *
     * @param criteria an array of String containing the search criteria.
     * @return a list of Enrichment objects that match the search criteria.
     */
    public List<Enrichment> searchEnrichments(String[] criteria) {
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

        return this.dynamoDBMapper.scan(Enrichment.class, dynamoDBScanExpression);
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
