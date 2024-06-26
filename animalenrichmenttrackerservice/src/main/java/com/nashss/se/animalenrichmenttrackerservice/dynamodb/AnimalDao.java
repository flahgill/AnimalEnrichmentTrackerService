package com.nashss.se.animalenrichmenttrackerservice.dynamodb;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Animal;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.AnimalNotFoundException;
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
     * Perform a query of the animals table (GSI) for animals matching the active status in query.
     *
     * @param isActive the activity status of an animal, requested by the user.
     * @return a list of animals matching the active status requested.
     */
    public List<Animal> getAllAnimals(String isActive) {
        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":isActive", new AttributeValue().withS(isActive));

        DynamoDBQueryExpression<Animal> queryExpression = new DynamoDBQueryExpression<Animal>()
                .withIndexName("AnimalsStatusIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("isActive = :isActive")
                .withExpressionAttributeValues(valueMap);

        return this.dynamoDBMapper.query(Animal.class, queryExpression);
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

    /**
     * Perform a search (via a "scan") of the animals table for animals matching the given criteria.
     *
     * "animalName", "species", "animalId", and "sex" attributes are searched.
     * The criteria are an array of Strings. Each element of the array is search individually.
     * ALL elements of the criteria array must appear in the animalName, species, animalId, or sex (or all).
     * Searches are CASE SENSITIVE.
     *
     * @param criteria an array of String containing search criteria.
     * @return a List of Animal objects that match the search criteria.
     */
    public List<Animal> searchAnimals(String[] criteria) {
        DynamoDBScanExpression dynamoDBScanExpression = new DynamoDBScanExpression();

        if (criteria.length > 0) {
            Map<String, AttributeValue> valueMap = new HashMap<>();
            String valueMapNamePrefix = ":c";

            StringBuilder nameFilterExpression = new StringBuilder();
            StringBuilder speciesFilterExpression = new StringBuilder();
            StringBuilder idFilterExpression = new StringBuilder();
            StringBuilder sexFilterExpression = new StringBuilder();

            for (int i = 0; i < criteria.length; i++) {
                valueMap.put(valueMapNamePrefix + i,
                        new AttributeValue().withS(criteria[i]));
                nameFilterExpression.append(
                        filterExpressionPart("animalName", valueMapNamePrefix, i));
                speciesFilterExpression.append(
                        filterExpressionPart("species", valueMapNamePrefix, i));
                idFilterExpression.append(
                        filterExpressionPart("animalId", valueMapNamePrefix, i));
                sexFilterExpression.append(
                        filterExpressionPart("sex", valueMapNamePrefix, i));
            }

            dynamoDBScanExpression.setExpressionAttributeValues(valueMap);
            dynamoDBScanExpression.setFilterExpression(
                    "(" + nameFilterExpression +
                            ") or (" + speciesFilterExpression +
                            ") or (" + sexFilterExpression +
                            ") or (" + idFilterExpression + ")");
        }

        return this.dynamoDBMapper.scan(Animal.class, dynamoDBScanExpression);
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
