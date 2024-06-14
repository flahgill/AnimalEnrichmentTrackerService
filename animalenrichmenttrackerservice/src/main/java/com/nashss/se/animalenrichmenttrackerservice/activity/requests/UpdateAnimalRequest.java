package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * UpdateAnimalRequest object class.
 */
@JsonDeserialize(builder = UpdateAnimalRequest.Builder.class)
public class UpdateAnimalRequest {
    private final String animalId;
    private final String animalName;
    private final int age;
    private final String sex;
    private final String species;
    private final String keeperManagerId;

    /**
     * creates UpdateHabitatRequest object for updating an animal using the animalId.
     *
     * @param animalId ID to specify which animal to update.
     * @param animalName requested updated name for animal.
     * @param age requested updated age for animal.
     * @param sex requested updated sex for animal.
     * @param species requested updated species for animal.
     */
    private UpdateAnimalRequest(String animalId, String animalName, int age, String sex,
                                String species, String keeperManagerId) {
        this.animalId = animalId;
        this.animalName = animalName;
        this.age = age;
        this.sex = sex;
        this.species = species;
        this.keeperManagerId = keeperManagerId;
    }

    public String getAnimalId() {
        return animalId;
    }

    public String getAnimalName() {
        return animalName;
    }

    public int getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public String getSpecies() {
        return species;
    }

    public String getKeeperManagerId() {
        return keeperManagerId;
    }

    @Override
    public String toString() {
        return "UpdateAnimalRequest{" +
                "animalId='" + animalId + '\'' +
                ", animalName='" + animalName + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", species='" + species + '\'' +
                ", keeperManagerId='" + keeperManagerId + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    @JsonPOJOBuilder
    public static class Builder {
        private String animalId;
        private String animalName;
        private int age;
        private String sex;
        private String species;
        private String keeperManagerId;
        public Builder withAnimalId(String animalId) {
            this.animalId = animalId;
            return this;
        }
        public Builder withAnimalName(String animalName) {
            this.animalName = animalName;
            return this;
        }
        public Builder withAge(int age) {
            this.age = age;
            return this;
        }
        public Builder withSex(String sex) {
            this.sex = sex;
            return this;
        }
        public Builder withSpecies(String species) {
            this.species = species;
            return this;
        }
        public Builder withKeeperManagerId(String keeperManagerId) {
            this.keeperManagerId = keeperManagerId;
            return this;
        }
        public UpdateAnimalRequest build() {
            return new UpdateAnimalRequest(animalId, animalName, age, sex, species, keeperManagerId);
        }
    }
}
