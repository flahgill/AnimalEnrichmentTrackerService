package com.nashss.se.animalenrichmenttrackerservice.dynamodb;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.HabitatNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.metrics.MetricsConstants;
import com.nashss.se.animalenrichmenttrackerservice.metrics.MetricsPublisher;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

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
}
