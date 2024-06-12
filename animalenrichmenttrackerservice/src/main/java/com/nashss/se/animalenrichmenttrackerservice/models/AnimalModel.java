package com.nashss.se.animalenrichmenttrackerservice.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;
import java.util.Objects;

public class AnimalModel {
    private final String animalId;
    private final String animalName;
    private final int age;
    private final String sex;
    private final String species;
    private final String isActive;
    private final String habitatId;
    private final Boolean onHabitat;

    private AnimalModel(String animalId, String animalName, int age, String sex, String species, String isActive,
                        String habitatId, Boolean onHabitat) {
        this.animalId = animalId;
        this.animalName = animalName;
        this.age = age;
        this.sex = sex;
        this.species = species;
        this.isActive = isActive;
        this.habitatId = habitatId;
        this.onHabitat = onHabitat;
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

    public String getIsActive() {
        return isActive;
    }

    public String getHabitatId() {
        return habitatId;
    }

    public Boolean getOnHabitat() {
        return onHabitat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimalModel that = (AnimalModel) o;
        return age == that.age &&
                Objects.equals(animalId, that.animalId) &&
                Objects.equals(animalName, that.animalName) &&
                Objects.equals(sex, that.sex) &&
                Objects.equals(species, that.species) &&
                Objects.equals(isActive, that.isActive) &&
                Objects.equals(habitatId, that.habitatId) &&
                Objects.equals(onHabitat, that.onHabitat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(animalId, animalName, age, sex, species, isActive, habitatId, onHabitat);
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private String animalId;
        private String animalName;
        private int age;
        private String sex;
        private String species;
        private String isActive;
        private String habitatId;
        private Boolean onHabitat;
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
        public Builder withIsActive(String isActive) {
            this.isActive = isActive;
            return this;
        }
        public Builder withHabitatId(String habitatId) {
            this.habitatId = habitatId;
            return this;
        }
        public Builder withOnHabitat(Boolean onHabitat) {
            this.onHabitat = onHabitat;
            return this;
        }
        public AnimalModel build() {
            return new AnimalModel(animalId, animalName, age, sex, species, isActive, habitatId, onHabitat);
        }
    }
}
