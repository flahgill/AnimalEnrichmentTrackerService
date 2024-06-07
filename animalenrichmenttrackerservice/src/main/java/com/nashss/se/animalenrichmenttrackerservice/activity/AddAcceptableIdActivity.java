package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.AddAcceptableIdRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.AddAcceptableIdResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.DuplicateAcceptableIdException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.UserSecurityException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Implementation of the AddAcceptableIdActivity for the AnimalEnrichmentTrackerServices's
 * AddAcceptableId API.
 * <p>
 * This API allows a keeper manager to add an id to a habitat's list of acceptable enrichment ids saved in DDB.
 */
public class AddAcceptableIdActivity {
    private final Logger log = LogManager.getLogger();
    private final HabitatDao habitatDao;

    /**
     * Instantiates a new AddAcceptableIdActivity object.
     *
     * @param habitatDao data access object to access the habitats DDB table.
     */
    @Inject
    public AddAcceptableIdActivity(HabitatDao habitatDao) {
        this.habitatDao = habitatDao;
    }

    /**
     * handles the incoming request by retrieving a habitat's list of acceptable enrichment ids, adding the new id,
     * and saving the new list within the habitat.
     * <p>
     * returns the saved habitat's updated list of acceptable enrichment ids.
     * <p>
     * If the id is already present in the habitat's list of acceptable enrichment ids, throws a
     * DuplicateAnimalException.
     * <p>
     * If the keeper adding the id is not the owner of the habitat, throws a UserSecurityException.
     *
     * @param addAcceptableIdRequest request object containing the habitatId, keeperManagerId, and id to be added.
     * @return addAcceptableIdResult result object containing the persisted list of acceptable enrichment ids.
     */
    public AddAcceptableIdResult handleRequest(final AddAcceptableIdRequest addAcceptableIdRequest) {
        log.info("Recieved AddAcceptableIdRequest {}", addAcceptableIdRequest);

        Habitat habitat = habitatDao.getHabitat(addAcceptableIdRequest.getHabitatId());

        if (!habitat.getKeeperManagerId().equals(addAcceptableIdRequest.getKeeperManagerId())) {
            throw new UserSecurityException("You must own this habitat to add a new acceptable id to it.");
        }

        List<String> currIds = habitat.getAcceptableEnrichmentIds();

        if (currIds == null) {
            currIds = new ArrayList<>();
        }

        List<String> updatedIds = new ArrayList<>(currIds);
        String idToAdd = addAcceptableIdRequest.getIdToAdd();

        if (containsIgnoreCase(currIds, idToAdd)) {
            throw new DuplicateAcceptableIdException("Id [" + idToAdd + "] is already listed as an acceptable Id " +
                    "for the habitat.");
        }

        updatedIds.add(idToAdd);
        habitat.setAcceptableEnrichmentIds(updatedIds);
        habitat = habitatDao.saveHabitat(habitat);

        return AddAcceptableIdResult.builder()
                .withAcceptableEnrichmentIds(updatedIds)
                .build();
    }

    /**
     * helper method to determine if the id being added is already present in the habitat's current
     * list of acceptable enrichment Ids. Case Insensitive.
     *
     * @param acceptableEnrichmentIds list of acceptable enrichment ids in a habitat.
     * @param searchTerm id to be added and searched in the current list.
     * @return boolean determining if id is already present, ignoring casing.
     */
    private boolean containsIgnoreCase(List<String> acceptableEnrichmentIds, String searchTerm) {
        String lowerCaseSearchTerm = searchTerm.toLowerCase();
        for (String id : acceptableEnrichmentIds) {
            if (id.toLowerCase().equals(lowerCaseSearchTerm)) {
                return true;
            }
        }
        return false;
    }
}
