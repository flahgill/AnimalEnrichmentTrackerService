package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.AddHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.AddHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.InvalidCharacterException;
import com.nashss.se.animalenrichmenttrackerservice.models.HabitatModel;
import com.nashss.se.animalenrichmenttrackerservice.utils.ServiceUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Implementation of the AddHabitatActivity for the AnimalEnrichmentTrackerServices's AddHabitat API.
 * <p>
 * This API allows a keeper manager to create a new habitat.
 */
public class AddHabitatActivity {
    private final Logger log = LogManager.getLogger();
    private final HabitatDao habitatDao;

    /**
     * Instantiates a new AddHabitatActivity object.
     *
     * @param habitatDao the HabitatDao to access the Habitats DDB table.
     */
    @Inject
    public AddHabitatActivity(HabitatDao habitatDao) {
        this.habitatDao = habitatDao;
    }

    /**
     * handles the incoming request by persisting a new habitat
     * with the provided habitat name and customer ID from the request.
     * <p>
     * returns the newly created habitat
     * <p>
     * If the provided habitat name or keeper ID has invalid characters, throws an
     * InvalidCharacterException
     *
     * @param addHabitatRequest request object containing the habitat name and keeper ID
     *                          associated with it
     * @return addHabitatResult result object containing the API defined {@link HabitatModel}
     */
    public AddHabitatResult handleRequest(final AddHabitatRequest addHabitatRequest) {
        log.info("Recieved AddHabitatRequest {}", addHabitatRequest);

        if (!ServiceUtils.isValidString(addHabitatRequest.getHabitatName())) {
            throw new InvalidCharacterException("Habitat name [" + addHabitatRequest.getHabitatName() +
                    "] contains illegal characters.");
        }

        if (!ServiceUtils.isValidString(addHabitatRequest.getKeeperManagerId())) {
            throw new InvalidCharacterException("Keeper Id [" + addHabitatRequest.getKeeperManagerId() +
                    "] contains illegal characters.");
        }

        List<String> species = null;
        if (addHabitatRequest.getSpecies() != null) {
            species = new ArrayList<>(addHabitatRequest.getSpecies());
        }

        Habitat habitat = new Habitat();
        habitat.setHabitatId(ServiceUtils.generateId());
        habitat.setIsActive("active");
        habitat.setHabitatName(addHabitatRequest.getHabitatName());
        habitat.setSpecies(species);
        habitat.setKeeperManagerId(addHabitatRequest.getKeeperManagerId());
        habitat.setTotalAnimals(0);
        habitat.setAnimalsInHabitat(new ArrayList<>());
        habitat.setAcceptableEnrichmentIds(new ArrayList<>());
        habitat.setKeeperName(addHabitatRequest.getKeeperName());

        habitatDao.saveHabitat(habitat);

        HabitatModel habitatModel = new ModelConverter().toHabitatModel(habitat);
        return AddHabitatResult.builder()
                .withHabitat(habitatModel)
                .build();
    }

}
