package com.nashss.se.animalenrichmenttrackerservice.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.nashss.se.animalenrichmenttrackerservice.converters.CompletedEnrichmentsSerializer;
import com.nashss.se.animalenrichmenttrackerservice.converters.EnrichmentConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Enrichment;

import java.util.List;
import java.util.Objects;

public class HabitatModel {
    private final String habitatId;
    private final String isActive;
    private final String habitatName;
    private final List<String> species;
    private final String keeperManagerId;
    private final int totalAnimals;
    private final List<String> animalsInHabitat;
    private final List<String> acceptableEnrichmentIds;

    @JsonSerialize(contentUsing = CompletedEnrichmentsSerializer.class)
    private final List<Enrichment> completedEnrichments;

    private HabitatModel(String habitatId, String isActive, String habitatName, List<String> species,
                         String keeperManagerId, int totalAnimals, List<String> animalsInHabitat,
                         List<String> acceptableEnrichmentIds, List<Enrichment> completedEnrichments) {
        this.habitatId = habitatId;
        this.isActive = isActive;
        this.habitatName = habitatName;
        this.species = species;
        this.keeperManagerId = keeperManagerId;
        this.totalAnimals = totalAnimals;
        this.animalsInHabitat = animalsInHabitat;
        this.acceptableEnrichmentIds = acceptableEnrichmentIds;
        this.completedEnrichments = completedEnrichments;
    }

    public String getHabitatId() {
        return habitatId;
    }

    public String getHabitatName() {
        return habitatName;
    }

    public List<String> getSpecies() {
        return species;
    }

    public String getKeeperManagerId() {
        return keeperManagerId;
    }

    public int getTotalAnimals() {
        return totalAnimals;
    }

    public List<String> getAnimalsInHabitat() {
        return animalsInHabitat;
    }

    public List<String> getAcceptableEnrichmentIds() {
        return acceptableEnrichmentIds;
    }

    public List<Enrichment> getCompletedEnrichments() {
        return completedEnrichments;
    }

    public String getIsActive() {
        return isActive;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HabitatModel that = (HabitatModel) o;
        return Objects.equals(habitatId, that.habitatId) && Objects.equals(habitatName, that.habitatName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(habitatId, habitatName);
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String habitatId;
        private String isActive;
        private String habitatName;
        private List<String> species;
        private String keeperManagerId;
        private int totalAnimals;
        private List<String> animalsInHabitat;
        private List<String> acceptableEnrichmentIds;
        private List<Enrichment> completedEnrichments;

        public Builder withHabitatId(String habitatId) {
            this.habitatId = habitatId;
            return this;
        }

        public Builder withIsActive(String isActive) {
            this.isActive = isActive;
            return this;
        }
        public Builder withHabitatName(String habitatName) {
            this.habitatName = habitatName;
            return this;
        }
        public Builder withSpecies(List<String> species) {
            this.species = species;
            return this;
        }
        public Builder withKeeperManagerId(String keeperManagerId) {
            this.keeperManagerId = keeperManagerId;
            return this;
        }
        public Builder withTotalAnimals(int totalAnimals) {
            this.totalAnimals = totalAnimals;
            return this;
        }
        public Builder withAnimalsInHabitat(List<String> animalsInHabitat) {
            this.animalsInHabitat = animalsInHabitat;
            return this;
        }
        public Builder withAcceptableEnrichmentIds(List<String> acceptableEnrichmentIds) {
            this.acceptableEnrichmentIds = acceptableEnrichmentIds;
            return this;
        }
        public Builder withCompletedEnrichments(List<Enrichment> completedEnrichments) {
            this.completedEnrichments = completedEnrichments;
            return this;
        }
        public HabitatModel build() {
            return new HabitatModel(habitatId, isActive, habitatName, species, keeperManagerId,
                    totalAnimals, animalsInHabitat, acceptableEnrichmentIds,
                    completedEnrichments);
        }
    }
}
