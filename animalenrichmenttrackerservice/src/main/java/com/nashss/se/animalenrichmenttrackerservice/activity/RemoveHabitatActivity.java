package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.RemoveHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.RemoveHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.models.HabitatModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Implementation of the RemoveHabitatActivity for the AnimalEnrichmentTrackerService API.
 *
 * This API allows the customer to remove one of their saved habitats.
 */
public class RemoveHabitatActivity {
    private final Logger log = LogManager.getLogger();
    private final HabitatDao habitatDao;

    /**
     * Instantiates a new RemoveHabitatActivity object.
     *
     * @param habitatDao habitatDao to access the habitat table.
     */
    @Inject
    public RemoveHabitatActivity(HabitatDao habitatDao) {
        this.habitatDao = habitatDao;
    }

    /**
     * This method handles the incoming request by removing the habitat from the database.
     * <p>
     * If the habitat does not exist, this should throw a HabitatNotFoundException.
     *
     * @param removeHabitatRequest request object containing the habitatId and keeperManagerId
     * @return removeHabitatResult result object containing the API defined {@link HabitatModel}
     */
    public RemoveHabitatResult handleRequest(final RemoveHabitatRequest removeHabitatRequest) {
        log.info("Recieved RemoveHabitatRequest {}", removeHabitatRequest);

        Habitat habitat = habitatDao.removeHabitat(removeHabitatRequest.getHabitatId());
        HabitatModel habitatModel = null;

        if (habitat == null) {
            log.info("Habitat with id {} not found", removeHabitatRequest.getHabitatId());
        } else {
            habitatModel = new ModelConverter().toHabitatModel(habitat);
        }

        return RemoveHabitatResult.builder()
                .withHabitat(habitatModel)
                .build();
    }
}
