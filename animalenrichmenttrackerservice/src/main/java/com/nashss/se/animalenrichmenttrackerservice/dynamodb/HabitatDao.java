package com.nashss.se.animalenrichmenttrackerservice.dynamodb;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.HabitatNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.metrics.MetricsConstants;
import com.nashss.se.animalenrichmenttrackerservice.metrics.MetricsPublisher;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
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
     * @param keeperManagerId the keeperManagerId to load the given habitat
     * @return the habitat object that was retrieved/loaded
     */
    public Habitat getHabitat(String habitatId, String keeperManagerId) {
        Habitat habitat = this.dynamoDBMapper.load(Habitat.class, habitatId, keeperManagerId);

        if (Objects.isNull(habitat)) {
            metricsPublisher.addCount(MetricsConstants.GETHABITAT_HABTITATNOTFOUND, 1);
            throw new HabitatNotFoundException("Could not find habitat with id [" + habitatId + "].");
        }

        metricsPublisher.addCount(MetricsConstants.GETHABITAT_HABTITATNOTFOUND, 0);

        return habitat;
    }

    /**
     * Removes the {@link Habitat} associated with the habitatId and keeperManagerId.
     *
     * @param habitatId the habitatId to load and remove the given habitat.
     * @param keeperManagerId the keeperManagerId to load and remove the given habitat.
     * @return the habitat object that was removed.
     */
    public Habitat removeHabitat(String habitatId, String keeperManagerId) {
        Habitat habitat = this.dynamoDBMapper.load(Habitat.class, habitatId, keeperManagerId);

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

        DynamoDBQueryExpression<Habitat> queryExpression = new DynamoDBQueryExpression<Habitat>()
                .withIndexName("HabitatsForKeeperManagerIdIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("keeperManagerId = :keeperManagerId")
                .withExpressionAttributeValues(valueMap);

        return this.dynamoDBMapper.query(Habitat.class, queryExpression);
    }
}
