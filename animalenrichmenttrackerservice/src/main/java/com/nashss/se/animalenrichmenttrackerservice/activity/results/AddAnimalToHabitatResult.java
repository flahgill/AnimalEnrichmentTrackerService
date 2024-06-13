package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.AnimalModel;

/**
 * AddAnimalToHabitatResult object class.
 */
public class AddAnimalToHabitatResult {
    private final AnimalModel addedAnimal;

    /**
     * creates AddAnimalToHabitatResult object for persisting the new animal added.
     *
     * @param addedAnimal new animal added.
     */
    private AddAnimalToHabitatResult(AnimalModel addedAnimal) {
        this.addedAnimal = addedAnimal;
    }

    public AnimalModel getAddedAnimal() {
        return addedAnimal;
    }

    @Override
    public String toString() {
        return "AddAnimalToHabitatResult{" +
                "addedAnimal=" + addedAnimal +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private AnimalModel addedAnimal;
        public Builder withAddedAnimal(AnimalModel addedAnimal) {
            this.addedAnimal = addedAnimal;
            return this;
        }
        public AddAnimalToHabitatResult build() {
            return new AddAnimalToHabitatResult(addedAnimal);
        }
    }
}
