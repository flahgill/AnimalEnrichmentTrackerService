package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * AddAnimalToHabitatRequest object class.
 */
@JsonDeserialize(builder = AddAnimalToHabitatRequest.Builder.class)
public class AddAnimalToHabitatRequest {
    private final String habitatId;
    private final String keeperManagerId;
    private final String animalName;
    private final int age;
    private final String sex;
    private final String species;


    /**
     * creates AddAnimalToHabitatRequest object to add to a new animal to a specified habitat.
     *
     * @param habitatId the habitatId to identify which habitat to add the animal to.
     * @param animalName the name of the animal to add.
     * @param age the age of the animal to add.
     * @param sex the sex of the animal to add.
     * @param species the species of the animal to add.
     */
    private AddAnimalToHabitatRequest(String habitatId, String keeperManagerId, String animalName, int age,
                                      String sex, String species) {
        this.animalName = animalName;
        this.habitatId = habitatId;
        this.keeperManagerId = keeperManagerId;
        this.age = age;
        this.sex = sex;
        this.species = species;
    }

    public String getHabitatId() {
        return habitatId;
    }

    public String getAnimalName() {
        return animalName;
    }

    public String getKeeperManagerId() {
        return keeperManagerId;
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

    @Override
    public String toString() {
        return "AddAnimalToHabitatRequest{" +
                "habitatId='" + habitatId + '\'' +
                ", keeperManagerId='" + keeperManagerId + '\'' +
                ", animalName='" + animalName + '\'' +
                ", age='" + age + '\'' +
                ", sex='" + sex + '\'' +
                ", species='" + species + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String habitatId;
        private String keeperManagerId;
        private String animalName;
        private int age;
        private String sex;
        private String species;
        public Builder withHabitatId(String habitatId) {
            this.habitatId = habitatId;
            return this;
        }
        public Builder withKeeperManagerId(String keeperManagerId) {
            this.keeperManagerId = keeperManagerId;
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
        public AddAnimalToHabitatRequest build() {
            return new AddAnimalToHabitatRequest(habitatId, keeperManagerId, animalName, age, sex, species);
        }
    }
}
