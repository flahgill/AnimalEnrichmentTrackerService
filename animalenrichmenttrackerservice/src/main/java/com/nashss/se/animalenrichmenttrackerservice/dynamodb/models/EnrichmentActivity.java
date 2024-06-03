package com.nashss.se.animalenrichmenttrackerservice.dynamodb.models;

import com.nashss.se.animalenrichmenttrackerservice.converters.LocalDateToStringConverter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;

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
    private String habitatId;
    private String isComplete;
    private Boolean onHabitat;

    @DynamoDBHashKey(attributeName = "activityId")
    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    @DynamoDBAttribute(attributeName = "enrichmentId")
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "KeeperRatingsForEnrichmentIdsIndex",
            attributeName = "enrichmentId")
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

    @DynamoDBAttribute(attributeName = "habitatId")
    public String getHabitatId() {
        return habitatId;
    }

    public void setHabitatId(String habitatId) {
        this.habitatId = habitatId;
    }

    @DynamoDBAttribute(attributeName = "isComplete")
    public String getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(String isComplete) {
        this.isComplete = isComplete;
    }

    @DynamoDBAttribute(attributeName = "onHabitat")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.BOOL)
    public Boolean getOnHabitat() {
        return onHabitat;
    }

    public void setOnHabitat(Boolean onHabitat) {
        this.onHabitat = onHabitat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EnrichmentActivity activity = (EnrichmentActivity) o;
        return keeperRating == activity.keeperRating &&
                Objects.equals(activityId, activity.activityId) &&
                Objects.equals(enrichmentId, activity.enrichmentId) &&
                Objects.equals(name, activity.name) &&
                Objects.equals(description, activity.description) &&
                Objects.equals(dateCompleted, activity.dateCompleted) &&
                Objects.equals(habitatId, activity.habitatId) &&
                Objects.equals(isComplete, activity.isComplete) &&
                Objects.equals(onHabitat, activity.onHabitat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activityId, enrichmentId, name, keeperRating,
                description, dateCompleted, habitatId, isComplete, onHabitat);
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
                ", habitatId='" + habitatId + '\'' +
                ", isComplete='" + isComplete + '\'' +
                ", onHabitat='" + onHabitat + '\'' +
                '}';
    }
}
