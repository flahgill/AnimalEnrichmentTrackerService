package com.nashss.se.animalenrichmenttrackerservice.dynamodb;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Accesses data for a habitat using {@link Habitat} to represent the model in DynamoDB.
 */
@Singleton
public class HabitatDao {
    private final DynamoDBMapper dynamoDBMapper;

    /**
     * Instantiates a new HabitatDao object.
     *
     * @param dynamoDBMapper the {@link DynamoDBMapper} used to interact with the Habitats table.
     */
    @Inject
    public HabitatDao(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
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
}
