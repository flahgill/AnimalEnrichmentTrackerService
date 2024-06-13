package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.RemoveAnimalRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.RemoveAnimalResult;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.AnimalDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Animal;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.AnimalCurrentlyOnHabitatException;
import com.nashss.se.animalenrichmenttrackerservice.models.AnimalModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Implementation of the RemoveAnimalActivity for the AnimalEnrichmentTrackerService API.
 *
 * This API allows the keeper to remove one of their saved animals.
 */
public class RemoveAnimalActivity {
    private final Logger log = LogManager.getLogger();
    private final AnimalDao animalDao;

    /**
     * Instantiates a new RemoveAnimalActivity object.
     *
     * @param animalDao data access object to access the Animals DDB table.
     */
    @Inject
    public RemoveAnimalActivity(AnimalDao animalDao) {
        this.animalDao = animalDao;
    }

    /**
     * handles the incoming request by removing the animal from the database.
     * <p>
     * If the animal is not found, will throw AnimalNotFoundException.
     * <p>
     * If the animal is currently on a habitat, will throw AnimalCurrentlyOnHabitatException.
     *
     * @param removeAnimalRequest request object containing the animalId.
     * @return removeAnimalResult result object containing the API defined {@link AnimalModel}.
     */
    public RemoveAnimalResult handleRequest(final RemoveAnimalRequest removeAnimalRequest) {
        log.info("Recieved RemoveAnimalRequest {}", removeAnimalRequest);

        String animalId = removeAnimalRequest.getAnimalId();
        Animal animalToRemove = animalDao.getAnimal(animalId);

        if (animalToRemove.getOnHabitat()) {
            throw new AnimalCurrentlyOnHabitatException("animal with id [" + animalId + "] is currently " +
                    "on a habitat[" + animalToRemove.getHabitatId() + "] and can't be deleted.");
        }

        animalToRemove = animalDao.removeAnimal(animalId);

        AnimalModel removedAnimalModel = new ModelConverter().toAnimalModel(animalToRemove);

        return RemoveAnimalResult.builder()
                .withRemovedAnimal(removedAnimalModel)
                .build();
    }
}
