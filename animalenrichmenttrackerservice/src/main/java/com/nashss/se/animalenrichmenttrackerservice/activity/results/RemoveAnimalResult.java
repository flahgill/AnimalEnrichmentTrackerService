package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.AnimalModel;

/**
 * RemoveAnimalResult object class.
 */
public class RemoveAnimalResult {
    private final AnimalModel removedAnimal;

    /**
     * creates RemoveAnimalResult object containing the animal that was removed.
     *
     * @param removedAnimal animal that was removed.
     */
    private RemoveAnimalResult(AnimalModel removedAnimal) {
        this.removedAnimal = removedAnimal;
    }

    public AnimalModel getRemovedAnimal() {
        return removedAnimal;
    }

    @Override
    public String toString() {
        return "RemoveAnimalResult{" +
                "removedAnimal=" + removedAnimal +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private AnimalModel removedAnimal;
        public Builder withRemovedAnimal(AnimalModel removedAnimal) {
            this.removedAnimal = removedAnimal;
            return this;
        }
        public RemoveAnimalResult build() {
            return new RemoveAnimalResult(removedAnimal);
        }
    }
}
