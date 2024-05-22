package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import java.util.List;

/**
 * ViewAnimalsForHabitatResult object class.
 */
public class ViewAnimalsForHabitatResult {

    private final List<String> animalsInHabitat;

    /**
     * creates ViewAnimalsForHabitatResult object for retrieving a habitat's list of animals using the habitatId.
     *
     * @param animalsInHabitat list of animals to be returned.
     */
    private ViewAnimalsForHabitatResult(List<String> animalsInHabitat) {
        this.animalsInHabitat = animalsInHabitat;
    }

    public List<String> getAnimalsInHabitat() {
        return animalsInHabitat;
    }

    @Override
    public String toString() {
        return "ViewAnimalsForHabitatResult{" +
                "animalsInHabitat=" + animalsInHabitat +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<String> animalsInHabitat;
        public Builder withAnimalsInHabitat(List<String> animalsInHabitat) {
            this.animalsInHabitat = animalsInHabitat;
            return this;
        }
        public ViewAnimalsForHabitatResult build() {
            return new ViewAnimalsForHabitatResult(animalsInHabitat);
        }
    }
}
