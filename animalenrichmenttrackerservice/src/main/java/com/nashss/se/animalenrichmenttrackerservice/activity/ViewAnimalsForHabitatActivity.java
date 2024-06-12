package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewAnimalsForHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewAnimalsForHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.comparators.AnimalNameComparator;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.AnimalDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Animal;
import com.nashss.se.animalenrichmenttrackerservice.models.AnimalModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private final AnimalDao animalDao;

    /**
     * Instantiates a new ViewAnimalsForHabitatActivity object.
     *
     * @param animalDao the data access object to access the Animals DDB table.
     */
    @Inject
    public ViewAnimalsForHabitatActivity(AnimalDao animalDao) {
        this.animalDao = animalDao;
    }

    /**
     * handles the incoming request by retrieving a habitat's list of
     * animals with the provided habitatId from the request.
     * <p>
     * returns the saved habitat's list of animals
     *
     * @param viewAnimalsForHabitatRequest request object containing the habitatId
     * @return viewAnimalsForHabitatResult result object containing the list of animals, sorted.
     */
    public ViewAnimalsForHabitatResult handleRequest(final ViewAnimalsForHabitatRequest viewAnimalsForHabitatRequest) {
        log.info("Recieved ViewAnimalsInHabitatRequest {}", viewAnimalsForHabitatRequest);

        List<Animal> animalsInHabitat = animalDao.getHabitatAnimals(viewAnimalsForHabitatRequest.getHabitatId());

        List<AnimalModel> habitatAnimalModels = new ModelConverter().toAnimalModelList(animalsInHabitat);
        habitatAnimalModels.sort(new AnimalNameComparator());

        return ViewAnimalsForHabitatResult.builder()
                .withAnimalsInHabitat(habitatAnimalModels)
                .build();
    }
}
