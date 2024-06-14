package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import java.util.List;

/**
 * ViewSpeciesListResult object class.
 */
public class ViewSpeciesListResult {
    private final List<String> speciesList;

    /**
     * creates ViewSpeciesListResult object for retrieving a habitat's list of species using the habitatId.
     *
     * @param speciesList a habitat's list of species to be returned.
     */
    private ViewSpeciesListResult(List<String> speciesList) {
        this.speciesList = speciesList;
    }

    public List<String> getSpeciesList() {
        return speciesList;
    }

    @Override
    public String toString() {
        return "ViewSpeciesListResult{" +
                "speciesList=" + speciesList +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private List<String> speciesList;
        public Builder withSpeciesList(List<String> speciesList) {
            this.speciesList = speciesList;
            return this;
        }
        public ViewSpeciesListResult build() {
            return new ViewSpeciesListResult(speciesList);
        }
    }
}
