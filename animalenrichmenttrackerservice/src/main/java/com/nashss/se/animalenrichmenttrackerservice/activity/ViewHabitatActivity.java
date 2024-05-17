package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.models.HabitatModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Implementation of the ViewHabitatActivity for the AnimalEnrichmentTrackerServices's ViewHabitat API.
 * <p>
 * This API allows a keeper manager to view a habitat saved in DDB.
 */
public class ViewHabitatActivity {
    private final Logger log = LogManager.getLogger();
    private final HabitatDao habitatDao;

    /**
     * Instantiates a new ViewHabitatActivity object.
     *
     * @param habitatDao the HabitatDao to access the Habitats DDB table.
     */
    @Inject
    public ViewHabitatActivity(HabitatDao habitatDao) {
        this.habitatDao = habitatDao;
    }

    /**
     * handles the incoming request by retrieving a habitat
     * with the provided habitat ID from the request.
     * <p>
     * returns the saved habitat
     *
     * @param viewHabitatRequest request object containing the habitat id
     * @return viewHabitatResult result object containing the API defined {@link HabitatModel}
     */
    public ViewHabitatResult handleRequest(final ViewHabitatRequest viewHabitatRequest) {
        log.info("Recieved ViewHabitatRequest {}", viewHabitatRequest);

        Habitat habitat = habitatDao.getHabitat(viewHabitatRequest.getHabitatId(),
                viewHabitatRequest.getKeeperManagerId());
        HabitatModel habitatModel = new ModelConverter().toHabitatModel(habitat);

        return ViewHabitatResult.builder()
                .withHabitat(habitatModel)
                .build();
    }
}
