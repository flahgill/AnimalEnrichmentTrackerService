package com.nashss.se.animalenrichmenttrackerservice.dynamodb.models;

import com.nashss.se.animalenrichmenttrackerservice.converters.LocalDateToStringConverter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a record in the habitats table.
 */
@DynamoDBTable(tableName = "Enrichments")
public class Enrichment {
    private String enrichmentId;
    private String name;
    private int keeperRating;
    private String description;
    private LocalDate dateCompleted;

    @DynamoDBHashKey(attributeName = "enrichmentId")
    public String getEnrichmentId() {
        return enrichmentId;
    }

    public void setEnrichmentId(String enrichmentId) {
        this.enrichmentId = enrichmentId;
    }

    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "keeperRating")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "KeeperRatingsForEnrichmentIdsIndex",
        attributeName = "keeperRating")
    public int getKeeperRating() {
        return keeperRating;
    }

    public void setKeeperRating(int keeperRating) {
        this.keeperRating = keeperRating;
    }

    @DynamoDBAttribute(attributeName = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDBAttribute(attributeName = "dateCompleted")
    @DynamoDBTypeConverted(converter = LocalDateToStringConverter.class)
    public LocalDate getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(LocalDate dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Enrichment that = (Enrichment) o;
        return Objects.equals(enrichmentId, that.enrichmentId) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enrichmentId, name);
    }

    @Override
    public String toString() {
        return "Enrichment{" +
                "enrichmentId='" + enrichmentId + '\'' +
                ", name='" + name + '\'' +
                ", keeperRating=" + keeperRating +
                ", description='" + description + '\'' +
                ", dateCompleted=" + dateCompleted +
                '}';
    }
}
