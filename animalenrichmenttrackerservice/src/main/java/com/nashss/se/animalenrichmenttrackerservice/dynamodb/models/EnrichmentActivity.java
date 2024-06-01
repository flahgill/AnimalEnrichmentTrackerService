package com.nashss.se.animalenrichmenttrackerservice.dynamodb.models;

import com.nashss.se.animalenrichmenttrackerservice.converters.LocalDateToStringConverter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a record in the EnrichmentActivities table.
 */

@DynamoDBTable(tableName = "EnrichmentActivities")
public class EnrichmentActivity {

    private String activityId;
    private String enrichmentId;
    private String name;
    private int keeperRating;
    private String description;
    private LocalDate dateCompleted;

    @DynamoDBHashKey(attributeName = "activityId")
    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    @DynamoDBAttribute(attributeName = "enrichmentId")
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "KeeperRatingsForEnrichmentIdsIndex",
            attributeName = "enrichementId")
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

    @DynamoDBAttribute(attributeName = "getDescription")
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
        EnrichmentActivity that = (EnrichmentActivity) o;
        return keeperRating == that.keeperRating &&
                Objects.equals(activityId, that.activityId) &&
                Objects.equals(enrichmentId, that.enrichmentId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(dateCompleted, that.dateCompleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activityId, enrichmentId, name, keeperRating, description, dateCompleted);
    }

    @Override
    public String toString() {
        return "EnrichmentActivity{" +
                "activityId='" + activityId + '\'' +
                ", enrichmentId='" + enrichmentId + '\'' +
                ", name='" + name + '\'' +
                ", keeperRating=" + keeperRating +
                ", description='" + description + '\'' +
                ", dateCompleted=" + dateCompleted +
                '}';
    }
}
