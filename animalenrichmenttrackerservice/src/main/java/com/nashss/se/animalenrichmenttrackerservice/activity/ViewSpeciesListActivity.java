package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewSpeciesListRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewSpeciesListResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Implementation of the ViewSpeciesListActivity for the AnimalEnrichmentTrackerServices's
 * ViewSpeciesList API.
 * <p>
 * This API allows a keeper manager to view a habitat's list of species saved in DDB.
 */
public class ViewSpeciesListActivity {
    private final Logger log = LogManager.getLogger();
    private final HabitatDao habitatDao;

    /**
     * Instantiates a new ViewSpeciesListActivity.
     *
     * @param habitatDao the HabitatDao to access the habitats DDB table.
     */
    @Inject
    public ViewSpeciesListActivity(HabitatDao habitatDao) {
        this.habitatDao = habitatDao;
    }

    /**
     * handles the incoming request by retrieving a habitat, getting it's saved list of species, and returning that
     * list.
     *
     * @param viewSpeciesListRequest request object containing the habitatId.
     * @return ViewSpeciesListResult result object containing the habitat's saved list of species.
     */
    public ViewSpeciesListResult handleRequest(final ViewSpeciesListRequest viewSpeciesListRequest) {
        log.info("Recieved ViewSpeciesListRequest {}", viewSpeciesListRequest);

        Habitat habitat = habitatDao.getHabitat(viewSpeciesListRequest.getHabitatId());
        List<String> speciesList = habitat.getSpecies();

        List<String> sortedSpecies = new ArrayList<>(speciesList);
        Collections.sort(sortedSpecies);

        return ViewSpeciesListResult.builder()
                .withSpeciesList(sortedSpecies)
                .build();
    }

}
