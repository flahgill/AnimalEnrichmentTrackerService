package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.AnimalModel;

import java.util.List;

/**
 * SearchAnimalsResult object class.
 */
public class SearchAnimalsResult {
    private final List<AnimalModel> animals;

    /**
     * creates SearchAnimalsResult object to search through the saved DDB of Animals.
     *
     * @param animals the returned list of animals.
     */
    private SearchAnimalsResult(List<AnimalModel> animals) {
        this.animals = animals;
    }

    public List<AnimalModel> getAnimals() {
        return animals;
    }

    @Override
    public String toString() {
        return "SearchAnimalsResult{" +
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
        public SearchAnimalsResult build() {
            return new SearchAnimalsResult(animals);
        }
    }
}
