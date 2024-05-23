package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.AddAnimalToHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.AddAnimalToHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.DuplicateAnimalException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.InvalidCharacterException;
import com.nashss.se.animalenrichmenttrackerservice.utils.ServiceUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Implementation of the AddAnimalToHabitatActivity for the AnimalEnrichmentTrackerServices's
 * AddAnimalToHabitat API.
 * <p>
 * This API allows a keeper manager to view a habitat's list of animals saved in DDB.
 */
public class AddAnimalToHabitatActivity {
    private final Logger log = LogManager.getLogger();
    private final HabitatDao habitatDao;

    /**
     * Instantiates a new AddAnimalToHabitatActivity object.
     *
     * @param habitatDao the HabitatDao to access the Habitats DDB table.
     */
    @Inject
    public AddAnimalToHabitatActivity(HabitatDao habitatDao) {
        this.habitatDao = habitatDao;
    }

    /**
     * handles the incoming request by retrieving a habitat's list of animals, adding the new animal,
     * and saving the new list within the habitat.
     * <p>
     * returns the saved habitat's updated list of animals.
     * <p>
     * If the provided habitat name or keeper ID has invalid characters, throws an
     * InvalidCharacterException.
     * <p>
     * If the animal is already present in the habitat's list of animals, throws a
     * DuplicateAnimalException.
     *
     * @param addAnimalToHabitatRequest request object containing the habitatId and animal to be added.
     * @return addAnimalToHabitatResult result object containing the updated list of animals.
     */
    public AddAnimalToHabitatResult handleRequest(final AddAnimalToHabitatRequest addAnimalToHabitatRequest) {
        log.info("Recieved AddAnimalToHabitatActivity {}", addAnimalToHabitatRequest);

        Habitat habitat = habitatDao.getHabitat(addAnimalToHabitatRequest.getHabitatId());
        List<String> currAnimalsInHabitat = habitat.getAnimalsInHabitat();

        if (currAnimalsInHabitat == null) {
            currAnimalsInHabitat = new ArrayList<>();
        }

        List<String> updatedAnimalsList = new ArrayList<>(currAnimalsInHabitat);

        int totalAnimals = habitat.getTotalAnimals();
        String animalToAdd = addAnimalToHabitatRequest.getAnimalToAdd();

        if (!ServiceUtils.isValidString(animalToAdd)) {
            throw new InvalidCharacterException("Animal name [" + animalToAdd + "] contains illegal characters.");
        }

        if (containsIgnoreCase(currAnimalsInHabitat, animalToAdd)) {
            throw new DuplicateAnimalException("[" + animalToAdd + "] is already in the habitat.");
        }

        updatedAnimalsList.add(animalToAdd);
        totalAnimals += 1;
        habitat.setAnimalsInHabitat(updatedAnimalsList);
        habitat.setTotalAnimals(totalAnimals);
        habitat = habitatDao.saveHabitat(habitat);

        return AddAnimalToHabitatResult.builder()
                .withAnimalsInHabitat(updatedAnimalsList)
                .build();
    }

    /**
     * helper method to determine in the animal being added is already present in the habitat's current
     * list of animals. Case Insensitive.
     *
     * @param animals list of animals in a habitat.
     * @param searchTerm animal to be added and searched in the current list.
     * @return boolean determining if animal's name is already present, ignoring casing.
     */
    private boolean containsIgnoreCase(List<String> animals, String searchTerm) {
        String lowerCaseSearchTerm = searchTerm.toLowerCase();
        for (String animal : animals) {
            if (animal.toLowerCase().equals(lowerCaseSearchTerm)) {
                return true;
            }
        }
        return false;
    }
}
