package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewAnimalRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewAnimalResult;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.AnimalDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Animal;
import com.nashss.se.animalenrichmenttrackerservice.models.AnimalModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Implementation of the ViewAnimalActivity for the AnimalEnrichmentTrackerServices's ViewAnimal API.
 * <p>
 * This API allows a keeper manager to view an animal saved in DDB.
 */
public class ViewAnimalActivity {
    private final Logger log = LogManager.getLogger();
    private final AnimalDao animalDao;

    /**
     * Instantiates a new ViewAnimalActivity object.
     *
     * @param animalDao the AnimalDao to access the Animals DDB table.
     */
    @Inject
    public ViewAnimalActivity(AnimalDao animalDao) {
        this.animalDao = animalDao;
    }

    /**
     * handles the incoming request by retrieving an animal with the provided animalId.
     * <p>
     * returns the saved animal.
     *
     * @param viewAnimalRequest request object containing the animalId.
     * @return viewAnimalResult result object containing the API defined {@link AnimalModel}
     */
    public ViewAnimalResult handleRequest(final ViewAnimalRequest viewAnimalRequest) {
        log.info("Recieved ViewAnimalRequest {}", viewAnimalRequest);

        Animal animal = animalDao.getAnimal(viewAnimalRequest.getAnimalId());
        AnimalModel animalModel = new ModelConverter().toAnimalModel(animal);

        return ViewAnimalResult.builder()
                .withAnimal(animalModel)
                .build();
    }
}
