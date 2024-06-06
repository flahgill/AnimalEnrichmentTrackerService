package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewAnimalsForHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewAnimalsForHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Implementation of the ViewAnimalsForHabitatActivity for the AnimalEnrichmentTrackerServices's
 * ViewAnimalsForHabitat API.
 * <p>
 * This API allows a keeper manager to view a habitat's list of animals saved in DDB.
 */
public class ViewAnimalsForHabitatActivity {
    private final Logger log = LogManager.getLogger();
    private final HabitatDao habitatDao;

    /**
     * Instantiates a new ViewAnimalsForHabitatActivity object.
     *
     * @param habitatDao the HabitatDao to access the Habitats DDB table.
     */
    @Inject
    public ViewAnimalsForHabitatActivity(HabitatDao habitatDao) {
        this.habitatDao = habitatDao;
    }

    /**
     * handles the incoming request by retrieving a habitat's list of
     * animals with the provided habitatId from the request.
     * <p>
     * returns the saved habitat's list of animals
     *
     * @param viewAnimalsForHabitatRequest request object containing the habitatId
     * @return viewAnimalsForHabitatResult result object containing the list of animals
     */
    public ViewAnimalsForHabitatResult handleRequest(final ViewAnimalsForHabitatRequest viewAnimalsForHabitatRequest) {
        log.info("Recieved ViewAnimalsInHabitatRequest {}", viewAnimalsForHabitatRequest);

        Habitat habitat = habitatDao.getHabitat(viewAnimalsForHabitatRequest.getHabitatId());
        List<String> animalsInHabitat = habitat.getAnimalsInHabitat();

        List<String> sortedAnimals = new ArrayList<>(animalsInHabitat);

        Collections.sort(sortedAnimals);

        return ViewAnimalsForHabitatResult.builder()
                .withAnimalsInHabitat(sortedAnimals)
                .build();
    }
}
