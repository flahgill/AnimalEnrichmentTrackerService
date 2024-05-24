package com.nashss.se.animalenrichmenttrackerservice.dynamodb;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.HabitatNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.metrics.MetricsConstants;
import com.nashss.se.animalenrichmenttrackerservice.metrics.MetricsPublisher;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Accesses data for a habitat using {@link Habitat} to represent the model in DynamoDB.
 */
@Singleton
public class HabitatDao {
    private final DynamoDBMapper dynamoDBMapper;
    private final MetricsPublisher metricsPublisher;

    /**
     * Instantiates a new HabitatDao object.
     *
     * @param dynamoDBMapper the {@link DynamoDBMapper} used to interact with the Habitats table.
     * @param metricsPublisher the {@link MetricsPublisher} used to record metrics.
     */
    @Inject
    public HabitatDao(DynamoDBMapper dynamoDBMapper, MetricsPublisher metricsPublisher) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.metricsPublisher = metricsPublisher;
    }

    /**
     * Saves (creates/updates) given habitat.
     *
     * @param habitat the habitat to save/update
     * @return the habitat object that was saved/updated
     */
    public Habitat saveHabitat(Habitat habitat) {
        this.dynamoDBMapper.save(habitat);
        return habitat;
    }

    /**
     * Retrieves (loads) a given habitat based on the habitatId.
     *
     * @param habitatId the habitatId to load the given habitat
     * @return the habitat object that was retrieved/loaded
     */
    public Habitat getHabitat(String habitatId) {
        Habitat habitat = this.dynamoDBMapper.load(Habitat.class, habitatId);

        if (Objects.isNull(habitat)) {
            metricsPublisher.addCount(MetricsConstants.GETHABITAT_HABTITATNOTFOUND, 1);
            throw new HabitatNotFoundException("Could not find habitat with id [" + habitatId + "].");
        }

        metricsPublisher.addCount(MetricsConstants.GETHABITAT_HABTITATNOTFOUND, 0);

        return habitat;
    }

    /**
     * Hard deletes the {@link Habitat} associated with the habitatId.
     *
     * @param habitatId the habitatId to load and remove the given habitat.
     * @return the habitat object that was removed.
     */
    public Habitat removeHabitat(String habitatId) {
        Habitat habitat = this.dynamoDBMapper.load(Habitat.class, habitatId);

        if (Objects.isNull(habitat)) {
            metricsPublisher.addCount(MetricsConstants.GETHABITAT_HABTITATNOTFOUND, 1);
            throw new HabitatNotFoundException("Could not find habitat with id [" + habitatId + "].");
        }

        this.dynamoDBMapper.delete(habitat);
        metricsPublisher.addCount(MetricsConstants.GETHABITAT_HABTITATNOTFOUND, 0);

        return habitat;
    }

    /**
     * Perform a search (via the GSI) of the habitat table for habitats matching the keeperManagerId.
     *
     * @param keeperManagerId the Id associated with list of habitats to be returned
     * @return a list of keeper's habitats
     */
    public List<Habitat> getAllHabitatsForKeeper(String keeperManagerId) {
        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":keeperManagerId", new AttributeValue().withS(keeperManagerId));
        valueMap.put(":isActive", new AttributeValue().withS("active"));

        DynamoDBQueryExpression<Habitat> queryExpression = new DynamoDBQueryExpression<Habitat>()
                .withIndexName("HabitatsForKeeperManagerIdIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("keeperManagerId = :keeperManagerId")
                .withFilterExpression("isActive = :isActive")
                .withExpressionAttributeValues(valueMap);

        return this.dynamoDBMapper.query(Habitat.class, queryExpression);
    }

    /**
     * Perform a search (via a "scan") of the habitats table for habitats matching the given criteria.
     *
     * "habitatName", "animalsInHabitat" and "species" attributes are searched.
     * The criteria are an array of Strings. Each element of the array is search individually.
     * ALL elements of the criteria array must appear in the habitatName, animalsInHabitat, or the species (or all).
     * Searches are CASE SENSITIVE.
     *
     * @param criteria an array of String containing search criteria.
     * @return a List of Habitat objects that match the search criteria.
     */
    public List<Habitat> searchHabitats(String[] criteria) {
        DynamoDBScanExpression dynamoDBScanExpression = new DynamoDBScanExpression();

        if (criteria.length > 0) {
            Map<String, AttributeValue> valueMap = new HashMap<>();
            String valueMapNamePrefix = ":c";

            StringBuilder nameFilterExpression = new StringBuilder();
            StringBuilder speciesFilterExpression = new StringBuilder();
            StringBuilder animalsFilterExpression = new StringBuilder();

            for (int i = 0; i < criteria.length; i++) {
                valueMap.put(valueMapNamePrefix + i,
                        new AttributeValue().withS(criteria[i]));
                nameFilterExpression.append(
                        filterExpressionPart("habitatName", valueMapNamePrefix, i));
                speciesFilterExpression.append(
                        filterExpressionPart("species", valueMapNamePrefix, i));
                animalsFilterExpression.append(
                        filterExpressionPart("animalsInHabitat", valueMapNamePrefix, i));
            }

            dynamoDBScanExpression.setExpressionAttributeValues(valueMap);
            dynamoDBScanExpression.setFilterExpression(
                    "(" + nameFilterExpression + ") or (" + speciesFilterExpression +
                            ") or (" + animalsFilterExpression + ")");
        }

        return this.dynamoDBMapper.scan(Habitat.class, dynamoDBScanExpression);
    }

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
