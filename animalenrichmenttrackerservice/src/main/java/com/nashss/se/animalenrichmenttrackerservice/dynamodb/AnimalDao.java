package com.nashss.se.animalenrichmenttrackerservice.dynamodb;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Animal;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.AnimalNotFoundException;
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
 * Accesses data for an Animal using {@link Animal} to represent the model in DynamoDB.
 */
@Singleton
public class AnimalDao {
    private final DynamoDBMapper dynamoDBMapper;
    private final MetricsPublisher metricsPublisher;

    /**
     * Instantiates a new AnimalDao object.
     *
     * @param dynamoDBMapper the {@link DynamoDBMapper} used to interact with the Animals table.
     * @param metricsPublisher the {@link MetricsPublisher} used to record metrics.
     */
    @Inject
    public AnimalDao(DynamoDBMapper dynamoDBMapper, MetricsPublisher metricsPublisher) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.metricsPublisher = metricsPublisher;
    }

    /**
     * Saves (creates/updates) given animal.
     *
     * @param animal the animal object to save/update.
     * @return the animal object that was saved/updated.
     */
    public Animal saveAnimal(Animal animal) {
        this.dynamoDBMapper.save(animal);
        return animal;
    }

    /**
     * Retrieves (loads) a given animal based on the animalId.
     *
     * @param animalId the animalId to load the given animal.
     * @return the animal object that was retrieved/loaded.
     */
    public Animal getAnimal(String animalId) {
        Animal animal = this.dynamoDBMapper.load(Animal.class, animalId);

        if (Objects.isNull(animal)) {
            throw new AnimalNotFoundException("Could not find animal with id [" + animalId + "].");
        }

        return animal;
    }

    /**
     * Hard deletes the {@link Animal} based on the animalId.
     *
     * @param animalId the animalId to load and remove the given animal.
     * @return the animal object that was removed.
     */
    public Animal removeAnimal(String animalId) {
        Animal animal = getAnimal(animalId);

        this.dynamoDBMapper.delete(animal);

        return animal;
    }

    /**
     * Perform a search (via a scan) of the animals table for animals matching the habitatId.
     *
     * @param habitatId the habitatId to search for in the animals table.
     * @return list of Animals matching the habitatId.
     */
    public List<Animal> getHabitatAnimals(String habitatId) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":habitatId", new AttributeValue().withS(habitatId));

        scanExpression.setExpressionAttributeValues(valueMap);
        scanExpression.setFilterExpression("habitatId = :habitatId");

        return this.dynamoDBMapper.scan(Animal.class, scanExpression);
    }
}
