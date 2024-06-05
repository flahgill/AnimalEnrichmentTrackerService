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
    private String activityName;
    private String description;

    @DynamoDBHashKey(attributeName = "enrichmentId")
    public String getEnrichmentId() {
        return enrichmentId;
    }

    public void setEnrichmentId(String enrichmentId) {
        this.enrichmentId = enrichmentId;
    }

    @DynamoDBAttribute(attributeName = "activityName")
    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
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
        return Objects.equals(enrichmentId, that.enrichmentId) && Objects.equals(activityName, that.activityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enrichmentId, activityName);
    }

    @Override
    public String toString() {
        return "Enrichment{" +
                "enrichmentId='" + enrichmentId + '\'' +
                ", activityName='" + activityName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
