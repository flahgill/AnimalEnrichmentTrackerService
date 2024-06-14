package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.AddSpeciesRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.AddSpeciesResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.DuplicateSpeciesException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.UserSecurityException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Implementation of the AddSpeciesActivity for the AnimalEnrichmentTrackerServices's
 * AddSpecies API.
 * <p>
 * This API allows a keeper manager to add a species to a habitat's list of species saved in DDB.
 */
public class AddSpeciesActivity {
    private final Logger log = LogManager.getLogger();
    private final HabitatDao habitatDao;

    /**
     * Instantiates a new AddSpeciesActivity object.
     *
     * @param habitatDao the HabitatDao to access the habitats DDB table.
     */
    @Inject
    public AddSpeciesActivity(HabitatDao habitatDao) {
        this.habitatDao = habitatDao;
    }

    /**
     * handles the incoming request by retrieving a habitat's list of species, adding the requested new species,
     * and saving the new list within the habitat.
     * <p>
     * returns the saved habitat's updated list of species.
     * <p>
     * If the species is already present in the habitat's list of species, throws a DuplicateSpeciesException.
     * <p>
     * If the keeper adding the species is not the owner of the habitat, throws a UserSecurityException.
     *
     * @param addSpeciesRequest request object containing the habitatId, keeperManagerId, and new species to add.
     * @return AddSpeciesResult result object containing the persisted list of species after the new addition.
     */
    public AddSpeciesResult handleRequest(final AddSpeciesRequest addSpeciesRequest) {
        log.info("Recieved AddSpeciesRequest {}", addSpeciesRequest);

        Habitat habitat = habitatDao.getHabitat(addSpeciesRequest.getHabitatId());

        if (!habitat.getKeeperManagerId().equals(addSpeciesRequest.getKeeperManagerId())) {
            throw new UserSecurityException("You must own this habitat to add new species to it.");
        }

        List<String> currSpecies = habitat.getSpecies();

        if (currSpecies == null) {
            currSpecies = new ArrayList<>();
        }

        List<String> updateSpecies = new ArrayList<>(currSpecies);
        String speciesToAdd = addSpeciesRequest.getSpeciesToAdd();

        if (containsIgnoreCase(currSpecies, speciesToAdd)) {
            throw new DuplicateSpeciesException("[" + speciesToAdd + "] is already listed as a species " +
                    "for the habitat.");
        }

        updateSpecies.add(speciesToAdd);
        Collections.sort(updateSpecies);
        habitat.setSpecies(updateSpecies);
        habitat = habitatDao.saveHabitat(habitat);

        return AddSpeciesResult.builder()
                .withSpeciesList(updateSpecies)
                .build();
    }

    /**
     * helper method to determine if the species being added is already present in the habitat's current
     * list of species. Case Insensitive.
     *
     * @param species list of species in a habitat.
     * @param searchTerm id to be added and searched in the current list.
     * @return boolean determining if id is already present, ignoring casing.
     */
    private boolean containsIgnoreCase(List<String> species, String searchTerm) {
        String lowerCaseSearchTerm = searchTerm.toLowerCase();
        for (String sp : species) {
            if (sp.toLowerCase().equals(lowerCaseSearchTerm)) {
                return true;
            }
        }
        return false;
    }
}
