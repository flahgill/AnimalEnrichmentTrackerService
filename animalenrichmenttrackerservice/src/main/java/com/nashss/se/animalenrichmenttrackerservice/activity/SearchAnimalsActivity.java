package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.SearchAnimalsRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.SearchAnimalsResult;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.AnimalDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Animal;
import com.nashss.se.animalenrichmenttrackerservice.models.AnimalModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import javax.inject.Inject;

import static com.nashss.se.animalenrichmenttrackerservice.utils.NullUtils.ifNull;

/**
 * Implementation of the SearchAnimalsActivity for the AnimalEnrichmentTrackerServices's
 * SearchAnimals API.
 * <p>
 * This API allows any user to search through the animals table saved in DDB.
 */
public class SearchAnimalsActivity {
    private final Logger log = LogManager.getLogger();
    private final AnimalDao animalDao;

    /**
     * Instantiates a new SearchAnimalsActivity object.
     *
     * @param animalDao the AnimalDao to access the animals DDB table.
     */
    @Inject
    public SearchAnimalsActivity(AnimalDao animalDao) {
        this.animalDao = animalDao;
    }

    /**
     * This method handles the incoming request by searching for animals from the DB.
     * <p>
     * It then returns the matching animals, or an empty list if none are found.
     *
     * @param searchAnimalsRequest request object containing the search criteria.
     * @return SearchAnimalsResult result object containing the animals that match the search criteria.
     */
    public SearchAnimalsResult handleRequest(final SearchAnimalsRequest searchAnimalsRequest) {
        log.info("Recieved SearchAnimalsRequest {}", searchAnimalsRequest);

        String criteria = ifNull(searchAnimalsRequest.getCriteria(), "");
        String[] criteriaArray = criteria.isBlank() ? new String[0] : criteria.split("\\s");

        List<Animal> animals = animalDao.searchAnimals(criteriaArray);
        List<AnimalModel> animalModels = new ModelConverter().toAnimalModelList(animals);

        return SearchAnimalsResult.builder()
                .withAnimals(animalModels)
                .build();
    }
}
