package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import java.util.List;

/**
 * RemoveSpeciesResult object class.
 */
public class RemoveSpeciesResult {
    private final List<String> speciesList;

    /**
     * creates RemoveSpeciesResult object to remove from a habitat's list of species.
     *
     * @param speciesList the persisted list of species after the removal.
     */
    private RemoveSpeciesResult(List<String> speciesList) {
        this.speciesList = speciesList;
    }

    public List<String> getSpeciesList() {
        return speciesList;
    }

    @Override
    public String toString() {
        return "RemoveSpeciesResult{" +
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

        public RemoveSpeciesResult build() {
            return new RemoveSpeciesResult(speciesList);
        }
    }
}
