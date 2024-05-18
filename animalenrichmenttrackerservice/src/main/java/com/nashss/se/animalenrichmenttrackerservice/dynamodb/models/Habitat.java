package com.nashss.se.animalenrichmenttrackerservice.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.List;
import java.util.Objects;

/**
 * Represents a record in the habitats table.
 */
@DynamoDBTable(tableName = "Habitats")
public class Habitat {

    private String habitatId;
    private String habitatName;
    private List<String> species;
    private String keeperManagerId;
    private int totalAnimals;
    private List<String> animalsInHabitat;
    private List<String> acceptableEnrichmentIds;
    private List<Enrichment> completedEnrichments;

    @DynamoDBHashKey(attributeName = "habitatId")
    public String getHabitatId() {
        return habitatId;
    }

    public void setHabitatId(String habitatId) {
        this.habitatId = habitatId;
    }

    @DynamoDBAttribute(attributeName = "habitatName")
    public String getHabitatName() {
        return habitatName;
    }

    public void setHabitatName(String habitatName) {
        this.habitatName = habitatName;
    }

    @DynamoDBAttribute(attributeName = "species")
    public List<String> getSpecies() {
        return species;
    }

    public void setSpecies(List<String> species) {
        this.species = species;
    }

    @DynamoDBRangeKey(attributeName = "keeperManagerId")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "HabitatsForKeeperManagerIdIndex",
            attributeName = "keeperManagerId")
    public String getKeeperManagerId() {
        return keeperManagerId;
    }

    public void setKeeperManagerId(String keeperManagerId) {
        this.keeperManagerId = keeperManagerId;
    }

    @DynamoDBAttribute(attributeName = "totalAnimals")
    public int getTotalAnimals() {
        return totalAnimals;
    }

    public void setTotalAnimals(int totalAnimals) {
        this.totalAnimals = totalAnimals;
    }

    @DynamoDBAttribute(attributeName = "animalsInHabitat")
    public List<String> getAnimalsInHabitat() {
        return animalsInHabitat;
    }

    public void setAnimalsInHabitat(List<String> animalsInHabitat) {
        this.animalsInHabitat = animalsInHabitat;
    }

    @DynamoDBAttribute(attributeName = "acceptableEnrichmentIds")
    public List<String> getAcceptableEnrichmentIds() {
        return acceptableEnrichmentIds;
    }

    public void setAcceptableEnrichmentIds(List<String> acceptableEnrichmentIds) {
        this.acceptableEnrichmentIds = acceptableEnrichmentIds;
    }

    @DynamoDBAttribute(attributeName = "completedEnrichmentIds")
    public List<Enrichment> getCompletedEnrichments() {
        return completedEnrichments;
    }

    public void setCompletedEnrichments(List<Enrichment> completedEnrichments) {
        this.completedEnrichments = completedEnrichments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Habitat habitat = (Habitat) o;
        return Objects.equals(habitatId, habitat.habitatId) && Objects.equals(habitatName, habitat.habitatName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(habitatId, habitatName);
    }

    @Override
    public String toString() {
        return "Habitat{" +
                "habitatId='" + habitatId + '\'' +
                ", habitatName='" + habitatName + '\'' +
                ", species=" + species +
                ", keeperManagerId='" + keeperManagerId + '\'' +
                ", totalAnimals=" + totalAnimals +
                ", animalsInHabitat=" + animalsInHabitat +
                ", acceptableEnrichmentIds=" + acceptableEnrichmentIds +
                ", completedEnrichments=" + completedEnrichments +
                '}';
    }
}
