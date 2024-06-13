package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewAllAnimalsRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewAllAnimalsResult;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.AnimalDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Animal;
import com.nashss.se.animalenrichmenttrackerservice.models.AnimalModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import javax.inject.Inject;

/**
 * Implementation of the ViewAllAnimalsActivity for the AnimalEnrichmentTrackerService API.
 *
 * This API allows the customer to view all animals based on activity status.
 */
public class ViewAllAnimalsActivity {
    private final Logger log = LogManager.getLogger();
    private final AnimalDao animalDao;

    /**
     * Instantiates new ViewAllAnimalsActivity.
     *
     * @param animalDao AnimalDao to access Animals table.
     */
    @Inject
    public ViewAllAnimalsActivity(AnimalDao animalDao) {
        this.animalDao = animalDao;
    }

    /**
     * This method handles the incoming request by retrieving all animals based on the user's input of active status.
     * <p>
     * It then returns a list of animals.
     *
     * @param viewAllAnimalsRequest request object containing the active status.
     * @return viewAllAnimalsResult result object containing a list of API defined {@link AnimalModel}.
     */
    public ViewAllAnimalsResult handleRequest(final ViewAllAnimalsRequest viewAllAnimalsRequest) {
        log.info("Recieved ViewAllAnimalsRequest {}", viewAllAnimalsRequest);

        String activeStatusRequest = viewAllAnimalsRequest.getIsActive();
        if (activeStatusRequest == null) {
            activeStatusRequest = "active";
        }

        List<Animal> animals = animalDao.getAllAnimals(activeStatusRequest);
        List<AnimalModel> animalModels = new ModelConverter().toAnimalModelList(animals);

        return ViewAllAnimalsResult.builder()
                .withAnimals(animalModels)
                .build();
    }
}
