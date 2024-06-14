package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.RemoveSpeciesRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.RemoveSpeciesResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.SpeciesNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.UserSecurityException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Implementation of the RemoveSpeciesActivity for the AnimalEnrichmentTrackerServices's
 * RemoveSpecies API.
 * <p>
 * This API allows a keeper manager to remove a species from a habitat's list of species saved in DDB.
 */
public class RemoveSpeciesActivity {
    private final Logger log = LogManager.getLogger();
    private final HabitatDao habitatDao;

    /**
     * Instantiates new RemoveSpeciesActivity object.
     *
     * @param habitatDao HabitatDao to access the DDB Habitat's table.
     */
    @Inject
    public RemoveSpeciesActivity(HabitatDao habitatDao) {
        this.habitatDao = habitatDao;
    }

    /**
     * handles the incoming request by retrieving a habitat's list of species, removing the requested
     * species, and saving the updated list within the habitat.
     * <p>
     * returns the saved habitat's updated list of species.
     * <p>
     * If the requested species is not present in the habitat's existing list of species, throws an
     * SpeciesNotFoundException.
     * <p>
     * If the keeper removing the species is not the owner of the habitat, throws a UserSecurityException.
     *
     * @param removeSpeciesRequest request object containing the habitatId, keeperManagerId, and species to be removed.
     * @return RemoveSpeciesResult result object containing the updated list of species.
     */
    public RemoveSpeciesResult handleRequest(final RemoveSpeciesRequest removeSpeciesRequest) {
        log.info("Recieved RemoveSpeciesResult {}", removeSpeciesRequest);

        Habitat habitat = habitatDao.getHabitat(removeSpeciesRequest.getHabitatId());

        if (!habitat.getKeeperManagerId().equals(removeSpeciesRequest.getKeeperManagerId())) {
            throw new UserSecurityException("You must own this habitat to remove species from it.");
        }

        List<String> currSpecies = habitat.getSpecies();

        if (currSpecies == null) {
            currSpecies = new ArrayList<>();
        }

        List<String> updateSpecies = new ArrayList<>(currSpecies);

        String speciesToRemove = removeSpeciesRequest.getSpeciesToRemove();

        if (!containsIgnoreCase(currSpecies, speciesToRemove)) {
            throw new SpeciesNotFoundException("[" + speciesToRemove + "] not found in habitat's current " +
                    "species list.");
        }

        updateSpecies.remove(speciesToRemove);
        Collections.sort(updateSpecies);
        habitat.setSpecies(updateSpecies);
        habitat = habitatDao.saveHabitat(habitat);

        return RemoveSpeciesResult.builder()
                .withSpeciesList(updateSpecies)
                .build();
    }

    /**
     * helper method to determine if the species being removed is present in the habitat's current
     * list of species. Case Insensitive.
     *
     * @param species list of species in a habitat.
     * @param searchTerm species to be removed and searched in the current list.
     * @return boolean determining if id is present, ignoring casing.
     */
    private boolean containsIgnoreCase(List<String> species, String searchTerm) {
        String lowerCaseSearchTerm = searchTerm.toLowerCase();
        for (String s : species) {
            if (s.toLowerCase().equals(lowerCaseSearchTerm)) {
                return true;
            }
        }
        return false;
    }
}
