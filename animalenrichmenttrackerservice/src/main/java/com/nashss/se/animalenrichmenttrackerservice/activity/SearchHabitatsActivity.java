package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.SearchHabitatsRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.SearchHabitatsResult;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.models.HabitatModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import javax.inject.Inject;

import static com.nashss.se.animalenrichmenttrackerservice.utils.NullUtils.ifNull;

/**
 * Implementation of the SearchHabitatsActivity for the AnimalEnrichmentTrackerServices's
 * SearchHabitats API.
 * <p>
 * This API allows any user to search through the habitats table saved in DDB.
 */
public class SearchHabitatsActivity {
    private final Logger log = LogManager.getLogger();
    private final HabitatDao habitatDao;

    /**
     * Instantiates a new SearchHabitatsActivity object.
     *
     * @param habitatDao the HabitatDao to access the Habitats DDB table.
     */
    @Inject
    public SearchHabitatsActivity(HabitatDao habitatDao) {
        this.habitatDao = habitatDao;
    }

    /**
     * This method handles the incoming request by searching for habitats from the database.
     * <p>
     * It then returns the matching habitats, or an empty result list if none are found.
     *
     * @param searchHabitatsRequest request object containing the search criteria
     * @return searchHabitatsResult result object containing the habitats that match the
     * search criteria.
     */
    public SearchHabitatsResult handleRequest(final SearchHabitatsRequest searchHabitatsRequest) {
        log.info("Received SearchHabitatsRequest {}", searchHabitatsRequest);

        String criteria = ifNull(searchHabitatsRequest.getCriteria(), "");
        String[] criteriaArray = criteria.isBlank() ? new String[0] : criteria.split("\\s");

        List<Habitat> results = habitatDao.searchHabitats(criteriaArray);
        List<HabitatModel> habitatModels = new ModelConverter().toHabitatModelList(results);

        return SearchHabitatsResult.builder()
                .withHabitats(habitatModels)
                .build();
    }
}
