package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.AnimalModel;

import java.util.List;

/**
 * ViewAnimalsForHabitatResult object class.
 */
public class ViewAnimalsForHabitatResult {

    private final List<AnimalModel> animalsInHabitat;

    /**
     * creates ViewAnimalsForHabitatResult object for retrieving a habitat's list of animals using the habitatId.
     *
     * @param animalsInHabitat list of animals to be returned.
     */
    private ViewAnimalsForHabitatResult(List<AnimalModel> animalsInHabitat) {
        this.animalsInHabitat = animalsInHabitat;
    }

    public List<AnimalModel> getAnimalsInHabitat() {
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
        private List<AnimalModel> animalsInHabitat;
        public Builder withAnimalsInHabitat(List<AnimalModel> animalsInHabitat) {
            this.animalsInHabitat = animalsInHabitat;
            return this;
        }
        public ViewAnimalsForHabitatResult build() {
            return new ViewAnimalsForHabitatResult(animalsInHabitat);
        }
    }
}
