package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.AnimalModel;

/**
 * UpdateAnimalResult object class.
 */
public class UpdateAnimalResult {
    private final AnimalModel animal;

    /**
     * creates UpdateAnimalResult object for updating an animal using the animalId.
     *
     * @param animal updated saved animal.
     */
    private UpdateAnimalResult(AnimalModel animal) {
        this.animal = animal;
    }

    public AnimalModel getAnimal() {
        return animal;
    }

    @Override
    public String toString() {
        return "UpdateAnimalResult{" +
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
        public UpdateAnimalResult build() {
            return new UpdateAnimalResult(animal);
        }
    }
}
