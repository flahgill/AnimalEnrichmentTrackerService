package com.nashss.se.animalenrichmenttrackerservice.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

/**
 * Represents a record in the Enrichments table.
 */
@DynamoDBTable(tableName = "Enrichments")
public class Enrichment {
    private String enrichmentId;
    private String name;
    private String description;

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

    @DynamoDBAttribute(attributeName = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
                ", description='" + description + '\'' +
                '}';
    }
}
