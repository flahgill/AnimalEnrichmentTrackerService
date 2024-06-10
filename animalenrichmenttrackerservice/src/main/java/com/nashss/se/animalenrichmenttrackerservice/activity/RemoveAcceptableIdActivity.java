package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.RemoveAcceptableIdRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.RemoveAcceptableIdResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.AcceptableIdNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.UserSecurityException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Implementation of the RemoveAcceptableIdActivity for the AnimalEnrichmentTrackerServices's
 * RemoveAcceptableId API.
 * <p>
 * This API allows a keeper manager to remove an id from a habitat's list of acceptable enrichment ids saved in DDB.
 */
public class RemoveAcceptableIdActivity {
    private final Logger log = LogManager.getLogger();
    private final HabitatDao habitatDao;

    /**
     * Instantiates a new RemoveAcceptableIdActivity object.
     * @param habitatDao data access object to access the DDB Habitat's table.
     */
    @Inject
    public RemoveAcceptableIdActivity(HabitatDao habitatDao) {
        this.habitatDao = habitatDao;
    }

    /**
     * handles the incoming request by retrieving a habitat's list of acceptable enrichment ids, removing the requested
     * id, and saving the updated list within the habitat.
     * <p>
     * returns the saved habitat's updated list of acceptable enrichment ids.
     * <p>
     * If the requested Id is not present in the habitat's existing list of acceptable enrichment ids, throws an
     * AcceptableIdNotFoundException.
     * <p>
     * If the keeper removing the id is not the owner of the habitat, throws a UserSecurityException.
     *
     * @param removeAcceptableIdRequest request object containing the habitatId, keeperManagerId, and id to be removed.
     * @return RemoveAcceptableIdResult result object containing the updated list of acceptable enrichment ids.
     */
    public RemoveAcceptableIdResult handleRequest(final RemoveAcceptableIdRequest removeAcceptableIdRequest) {
        log.info("Recieved RemoveAcceptableIdRequest {}", removeAcceptableIdRequest);

        Habitat habitat = habitatDao.getHabitat(removeAcceptableIdRequest.getHabitatId());

        if (!habitat.getKeeperManagerId().equals(removeAcceptableIdRequest.getKeeperManagerId())) {
            throw new UserSecurityException("You must own this habitat to remove an acceptable id from it.");
        }

        List<String> currAcceptableIds = habitat.getAcceptableEnrichmentIds();

        if (currAcceptableIds == null) {
            currAcceptableIds = new ArrayList<>();
        }

        List<String> updatedIds = new ArrayList<>(currAcceptableIds);

        String idToRemove = removeAcceptableIdRequest.getIdToRemove();

        if (!containsIgnoreCase(currAcceptableIds, idToRemove)) {
            throw new AcceptableIdNotFoundException("[" + idToRemove + "] not found in habitat's acceptable " +
                    "enrichment ids.");
        }

        updatedIds.remove(idToRemove);
        Collections.sort(updatedIds);
        habitat.setAcceptableEnrichmentIds(updatedIds);
        habitat = habitatDao.saveHabitat(habitat);

        return RemoveAcceptableIdResult.builder()
                .withAcceptableEnrichmentIds(updatedIds)
                .build();
    }

    /**
     * helper method to determine if the animal being removed is present in the habitat's current
     * list of animals. Case Insensitive.
     *
     * @param acceptableIds list of acceptable enrichment ids in a habitat.
     * @param searchTerm id to be removed and searched in the current list.
     * @return boolean determining if id is present, ignoring casing.
     */
    private boolean containsIgnoreCase(List<String> acceptableIds, String searchTerm) {
        String lowerCaseSearchTerm = searchTerm.toLowerCase();
        for (String id : acceptableIds) {
            if (id.toLowerCase().equals(lowerCaseSearchTerm)) {
                return true;
            }
        }
        return false;
    }

}
