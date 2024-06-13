package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.AnimalModel;

import java.util.List;

/**
 * ViewAllAnimalsResult object class.
 */
public class ViewAllAnimalsResult {
    private final List<AnimalModel> animals;

    /**
     * creates ViewAllAnimalsResult object containing a list of animals.
     *
     * @param animals reutrned list of animals.
     */
    private ViewAllAnimalsResult(List<AnimalModel> animals) {
        this.animals = animals;
    }

    public List<AnimalModel> getAnimals() {
        return animals;
    }

    @Override
    public String toString() {
        return "ViewAllAnimalsResult{" +
                "animals=" + animals +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private List<AnimalModel> animals;
        public Builder withAnimals(List<AnimalModel> animals) {
            this.animals = animals;
            return this;
        }
        public ViewAllAnimalsResult build() {
            return new ViewAllAnimalsResult(animals);
        }
    }
}
