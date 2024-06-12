package com.nashss.se.animalenrichmenttrackerservice.helper;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Animal;

public class AnimalTestHelper {
    private AnimalTestHelper() {}

    public static Animal generateAnimal() {
        Animal animal = new Animal();
        animal.setAnimalId("123");
        animal.setAnimalName("Benny");
        animal.setAge(5);
        animal.setSex("M");
        animal.setSpecies("Giraffe");
        animal.setIsActive("active");
        animal.setOnHabitat(true);
        animal.setHabitatId("12345");

        return animal;
    }

}
