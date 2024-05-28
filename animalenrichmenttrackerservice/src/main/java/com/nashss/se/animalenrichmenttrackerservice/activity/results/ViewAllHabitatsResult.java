package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.HabitatModel;

import java.util.List;

/**
 * ViewInactiveHabitatsResult object class.
 */
public class ViewAllHabitatsResult {
    private final List<HabitatModel> habitats;

    /**
     * creates ViewInactiveHabitatsResult object containing a list of deactivated habitats.
     *
     * @param habitats the list of deactivated habitats.
     */
    private ViewAllHabitatsResult(List<HabitatModel> habitats) {
        this.habitats = habitats;
    }

    public List<HabitatModel> getHabitats() {
        return habitats;
    }

    @Override
    public String toString() {
        return "ViewInactiveHabitatsResult{" +
                "inactiveHabitats=" + habitats +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private List<HabitatModel> habitats;
        public Builder withHabitats(List<HabitatModel> inactiveHabitats) {
            this.habitats = inactiveHabitats;
            return this;
        }
        public ViewAllHabitatsResult build() {
            return new ViewAllHabitatsResult(habitats);
        }
    }
}
