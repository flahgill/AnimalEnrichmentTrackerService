package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.HabitatModel;

import java.util.List;

/**
 * ViewUserHabitatsResult object class.
 */
public class ViewUserHabitatsResult {
    private final List<HabitatModel> habitats;

    /**
     * creates ViewUserHabitatsResult object containing a keeper's list of habitats.
     *
     * @param habitats the list of habitats associated with a certain keeper's Id
     */
    public ViewUserHabitatsResult(List<HabitatModel> habitats) {
        this.habitats = habitats;
    }

    public List<HabitatModel> getHabitats() {
        return habitats;
    }

    @Override
    public String toString() {
        return "ViewUserHabitatsResult{" +
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

        public ViewUserHabitatsResult build() {
            return new ViewUserHabitatsResult(habitats);
        }

    }
}
