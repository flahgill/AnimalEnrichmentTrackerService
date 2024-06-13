package com.nashss.se.animalenrichmenttrackerservice.dynamodb;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Animal;
import com.nashss.se.animalenrichmenttrackerservice.metrics.MetricsPublisher;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
