package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.HabitatModel;

import java.util.List;

/**
 * ViewInactiveHabitatsResult object class.
 */
public class ViewInactiveHabitatsResult {
    private final List<HabitatModel> inactiveHabitats;

    /**
     * creates ViewInactiveHabitatsResult object containing a list of deactivated habitats.
     *
     * @param inactiveHabitats the list of deactivated habitats.
     */
    private ViewInactiveHabitatsResult(List<HabitatModel> inactiveHabitats) {
        this.inactiveHabitats = inactiveHabitats;
    }

    public List<HabitatModel> getInactiveHabitats() {
        return inactiveHabitats;
    }

    @Override
    public String toString() {
        return "ViewInactiveHabitatsResult{" +
                "inactiveHabitats=" + inactiveHabitats +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private List<HabitatModel> inactiveHabitats;
        public Builder withInactiveHabitats(List<HabitatModel> inactiveHabitats) {
            this.inactiveHabitats = inactiveHabitats;
            return this;
        }
        public ViewInactiveHabitatsResult build() {
            return new ViewInactiveHabitatsResult(inactiveHabitats);
        }
    }
}
