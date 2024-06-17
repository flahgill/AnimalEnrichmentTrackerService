package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.AnimalModel;

/**
 * ReactivateAnimalResult object class.
 */
public class ReactivateAnimalResult {
    private final AnimalModel animal;

    /**
     * creates ReactivateAnimalResult object for re-adding a removed animal to a habitat.
     *
     * @param animal the returned animal after updating.
     */
    private ReactivateAnimalResult(AnimalModel animal) {
        this.animal = animal;
    }

    public AnimalModel getAnimal() {
        return animal;
    }

    @Override
    public String toString() {
        return "ReactivateAnimalResult{" +
                "animal=" + animal +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private AnimalModel animal;
        public Builder withAnimal(AnimalModel animal) {
            this.animal = animal;
            return this;
        }
        public ReactivateAnimalResult build() {
            return new ReactivateAnimalResult(animal);
        }
    }
}
