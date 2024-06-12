package com.nashss.se.animalenrichmenttrackerservice.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

/**
 * Represents a record in the animals table.
 */
@DynamoDBTable(tableName = "Animals")
public class Animal {
    private String animalId;
    private String animalName;
    private int age;
    private String sex;
    private String species;
    private String isActive;
    private String habitatId;
    private Boolean onHabitat;

    @DynamoDBHashKey(attributeName = "animalId")
    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }

    @DynamoDBAttribute(attributeName = "animalName")
    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    @DynamoDBAttribute(attributeName = "age")
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @DynamoDBAttribute(attributeName = "sex")
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @DynamoDBAttribute(attributeName = "species")
    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    @DynamoDBAttribute(attributeName = "isActive")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "AnimalsStatusIndex", attributeName = "isActive")
    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    @DynamoDBAttribute(attributeName = "habitatId")
    public String getHabitatId() {
        return habitatId;
    }

    public void setHabitatId(String habitatId) {
        this.habitatId = habitatId;
    }

    @DynamoDBAttribute(attributeName = "onHabitat")
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
        Animal animal = (Animal) o;
        return age == animal.age &&
                Objects.equals(animalId, animal.animalId) &&
                Objects.equals(animalName, animal.animalName) &&
                Objects.equals(sex, animal.sex) &&
                Objects.equals(species, animal.species) &&
                Objects.equals(isActive, animal.isActive) &&
                Objects.equals(habitatId, animal.habitatId) &&
                Objects.equals(onHabitat, animal.onHabitat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(animalId, animalName, age, sex, species, isActive, habitatId, onHabitat);
    }

    @Override
    public String toString() {
        return "Animal{" +
                "animalId='" + animalId + '\'' +
                ", animalName='" + animalName + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", species='" + species + '\'' +
                ", isActive='" + isActive + '\'' +
                ", habitatId='" + habitatId + '\'' +
                ", onHabitat=" + onHabitat +
                '}';
    }
}
