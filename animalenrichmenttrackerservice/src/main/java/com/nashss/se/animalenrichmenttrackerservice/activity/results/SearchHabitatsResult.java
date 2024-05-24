package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.HabitatModel;

import java.util.List;

/**
 * SearchHabitatsResult object class.
 */
public class SearchHabitatsResult {
    private List<HabitatModel> habitats;

    /**
     * creates SearchHabitatsResult object to search through the saved DDB of Habitats.
     *
     * @param habitats a list of habitats resulting from the search.
     */
    private SearchHabitatsResult(List<HabitatModel> habitats) {
        this.habitats = habitats;
    }

    public List<HabitatModel> getHabitats() {
        return habitats;
    }

    @Override
    public String toString() {
        return "SearchHabitatsResult{" +
                "habitats=" + habitats +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<HabitatModel> habitats;
        public Builder withHabitats(List<HabitatModel> habitats) {
            this.habitats = habitats;
            return this;
        }
        public SearchHabitatsResult build() {
            return new SearchHabitatsResult(habitats);
        }
    }
}
