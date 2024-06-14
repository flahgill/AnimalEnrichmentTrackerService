package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import java.util.List;

/**
 * AddSpeciesResult object class.
 */
public class AddSpeciesResult {
    private final List<String> speciesList;

    /**
     * creates AddSpeciesResult object to add to a habitat's list of species.
     *
     * @param speciesList the persisted list of species after the new addition.
     */
    private AddSpeciesResult(List<String> speciesList) {
        this.speciesList = speciesList;
    }

    public List<String> getSpeciesList() {
        return speciesList;
    }

    @Override
    public String toString() {
        return "AddSpeciesResult{" +
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
        public AddSpeciesResult build() {
            return new AddSpeciesResult(speciesList);
        }
    }
}
