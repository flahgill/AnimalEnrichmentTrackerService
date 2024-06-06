package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ReAddEnrichmentActivityToHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ReAddEnrichmentActivityToHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.EnrichmentActivityDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.EnrichmentActivityCurrentlyOnHabitatException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.IncompatibleHabitatIdException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.UserSecurityException;
import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentActivityModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Implementation of the ReAddEnrichmentActivityToHabitatActivity for the AnimalEnrichmentTrackerServices's
 * ReAddEnrichmentActivityToHabitat API.
 * <p>
 * This API allows a keeper manager to re-add a completed EnrichmentActivity to a habitat's list of
 * completedEnrichments saved in DDB.
 */
public class ReAddEnrichmentActivityToHabitatActivity {
    private final Logger log = LogManager.getLogger();
    private final HabitatDao habitatDao;
    private final EnrichmentActivityDao enrichmentActivityDao;

    /**
     * Instantiates new ReAddEnrichmentActivityToHabitatActivity.
     *
     * @param habitatDao data access object to access habitats table.
     * @param enrichmentActivityDao data access object to access enrichmentActivities table.
     */
    @Inject
    public ReAddEnrichmentActivityToHabitatActivity(HabitatDao habitatDao,
                                                    EnrichmentActivityDao enrichmentActivityDao) {
        this.habitatDao = habitatDao;
        this.enrichmentActivityDao = enrichmentActivityDao;
    }

    /**
     * handles the incoming request by retrieving a habitat's list of completedEnrichments,
     * adding the removed enrichment activity, and saving the new list within the habitat.
     * Also saves the enrichment activity to the master EnrichmentActivities table.
     * <p>
     * returns the saved habitat's updated list of enrichments
     * <p>
     * If the activity is already on a habitat, throws EnrichmentActivityCurrentlyOnHabitatException.
     * <p>
     * If the habitatId does not match the habitatId that the activity was originally removed from,
     * throws IncompatibleHabitatIdException.
     * <p>
     * If the keeper adding the enrichment is not the owner of the habitat, throws a UserSecurityException.
     *
     * @param reAddEnrichmentActivityToHabitatRequest request object containing the habitatId,
     *                                              activityId, and keeperManagerId.
     * @return reAddEnrichmentActivityToHabitatResult result object containing the update list of completedEnrichments.
     */
    public ReAddEnrichmentActivityToHabitatResult handleRequest(final ReAddEnrichmentActivityToHabitatRequest
                                                                        reAddEnrichmentActivityToHabitatRequest) {
        log.info("Recieved ReAddEnrichmentActivityToHabitatRequest {}",
                reAddEnrichmentActivityToHabitatRequest);

        String habitatId = reAddEnrichmentActivityToHabitatRequest.getHabitatId();
        Habitat habitat = habitatDao.getHabitat(habitatId);

        if (!habitat.getKeeperManagerId().equals(reAddEnrichmentActivityToHabitatRequest.getKeeperManagerId())) {
            throw new UserSecurityException("You must own this habitat to add an enrichment activity.");
        }

        List<EnrichmentActivity> completedEnrichList = getCurrEnrichmentsAndCopy(habitat);

        String activityId = reAddEnrichmentActivityToHabitatRequest.getActivityId();
        EnrichmentActivity eaToReAdd = enrichmentActivityDao.getEnrichmentActivity(activityId);

        if (eaToReAdd.getOnHabitat()) {
            throw new EnrichmentActivityCurrentlyOnHabitatException("Activity with Id [" + activityId + "] is " +
                    "already on a habitat[" + eaToReAdd.getHabitatId() + "].");
        }

        if (!habitat.getHabitatId().equals(eaToReAdd.getHabitatId())) {
            throw new IncompatibleHabitatIdException("Incompatible Habitat Id [" + habitatId + "]." +
                    "Activity with Id [" + activityId + "] can only be re-added to " +
                    "the original habitat with Id [" + eaToReAdd.getHabitatId() + "].");
        }

        eaToReAdd.setOnHabitat(true);

        completedEnrichList.add(0, eaToReAdd);
        eaToReAdd = enrichmentActivityDao.saveEnrichmentActivity(eaToReAdd);

        List<EnrichmentActivityModel> updatedEAModels =
                new ModelConverter().toEnrichmentActivityModelList(completedEnrichList);

        habitat.setCompletedEnrichments(completedEnrichList);
        habitat = habitatDao.saveHabitat(habitat);

        return ReAddEnrichmentActivityToHabitatResult.builder()
                .withCompletedEnrichments(updatedEAModels)
                .build();

    }

    /**
     * private helper method to extract the habitat's list of current enrichments, and copies to a new list.
     *
     * @param habitat habitat to pull current list of enrichments from.
     * @return copied list of current enrichment activities.
     */
    private static List<EnrichmentActivity> getCurrEnrichmentsAndCopy(Habitat habitat) {
        List<String> acceptableEnrichments = habitat.getAcceptableEnrichmentIds();

        if (acceptableEnrichments == null || acceptableEnrichments.isEmpty()) {
            acceptableEnrichments = new ArrayList<>();
        }

        List<EnrichmentActivity> currEnrichments = habitat.getCompletedEnrichments();

        if (currEnrichments == null) {
            currEnrichments = new ArrayList<>();
        }

        return new ArrayList<>(currEnrichments);
    }
}
