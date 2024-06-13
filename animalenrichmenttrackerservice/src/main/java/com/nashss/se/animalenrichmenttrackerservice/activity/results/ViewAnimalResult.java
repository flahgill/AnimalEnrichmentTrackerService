package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.AnimalModel;

/**
 * ViewAnimalResult object class.
 */
public class ViewAnimalResult {
    private final AnimalModel animal;

    /**
     * creates ViewAnimalResult object for persisting the animal retrieved.
     *
     * @param animal animal to be retrieved.
     */
    private ViewAnimalResult(AnimalModel animal) {
        this.animal = animal;
    }

    public AnimalModel getAnimal() {
        return animal;
    }

    @Override
    public String toString() {
        return "ViewAnimalResult{" +
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
        public ViewAnimalResult build() {
            return new ViewAnimalResult(animal);
        }
    }
}
