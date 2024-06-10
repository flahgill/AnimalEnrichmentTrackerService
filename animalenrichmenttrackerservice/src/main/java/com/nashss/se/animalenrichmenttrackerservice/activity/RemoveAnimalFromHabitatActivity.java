package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.RemoveAnimalFromHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.RemoveAnimalFromHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.AnimalNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.UserSecurityException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Implementation of the RemoveAnimalFromHabitatActivity for the AnimalEnrichmentTrackerServices's
 * RemoveAnimalFromHabitat API.
 * <p>
 * This API allows a keeper manager to remove an animal from a habitat's list of animals saved in DDB.
 */
public class RemoveAnimalFromHabitatActivity {
    private final Logger log = LogManager.getLogger();
    private final HabitatDao habitatDao;

    /**
     * Instantiates a new RemoveAnimalFromHabitatActivity object.
     *
     * @param habitatDao the HabitatDao to access the Habitats DDB table.
     */
    @Inject
    public RemoveAnimalFromHabitatActivity(HabitatDao habitatDao) {
        this.habitatDao = habitatDao;
    }

    /**
     * handles the incoming request by retrieving a habitat's list of animals, removing the animal,
     * and saving the new list within the habitat.
     * <p>
     * returns the saved habitat's updated list of animals.
     * <p>
     * If the animal is not present in the habitat's existing list of animals, throws
     * an AnimalNotFoundException.
     *<p>
     * If the keeper removing the animal is not the owner of the habitat, throws a UserSecurityException.
     *
     * @param removeAnimalFromHabitatRequest request object containing the habitatId and animal to be removed.
     * @return removeAnimalFromHabitatResult result object containing the updated list of animals.
     */
    public RemoveAnimalFromHabitatResult handleRequest(final RemoveAnimalFromHabitatRequest
                                                               removeAnimalFromHabitatRequest) {
        log.info("Recieved RemoveAnimalFromHabitatRequest {}", removeAnimalFromHabitatRequest);

        Habitat habitat = habitatDao.getHabitat(removeAnimalFromHabitatRequest.getHabitatId());

        if (!habitat.getKeeperManagerId().equals(removeAnimalFromHabitatRequest.getKeeperManagerId())) {
            throw new UserSecurityException("You must own this habitat to remove an animal from it.");
        }

        List<String> currAnimalsInHabitat = habitat.getAnimalsInHabitat();

        if (currAnimalsInHabitat == null) {
            currAnimalsInHabitat = new ArrayList<>();
        }

        List<String> updatedAnimalsList = new ArrayList<>(currAnimalsInHabitat);

        int totalAnimals = habitat.getTotalAnimals();
        String animalToRemove = removeAnimalFromHabitatRequest.getAnimalToRemove();

        if (!containsIgnoreCase(currAnimalsInHabitat, animalToRemove)) {
            throw new AnimalNotFoundException("[" + animalToRemove + "] is not in the habitat.");
        }

        updatedAnimalsList.remove(animalToRemove);
        totalAnimals -= 1;
        Collections.sort(updatedAnimalsList);
        habitat.setAnimalsInHabitat(updatedAnimalsList);
        habitat.setTotalAnimals(totalAnimals);
        habitat = habitatDao.saveHabitat(habitat);

        return RemoveAnimalFromHabitatResult.builder()
                .withAnimalsInHabitat(updatedAnimalsList)
                .build();
    }

    /**
     * helper method to determine if the animal being removed is present in the habitat's current
     * list of animals. Case Insensitive.
     *
     * @param animals list of animals in a habitat.
     * @param searchTerm animal to be removed and searched in the current list.
     * @return boolean determining if animal's name is present, ignoring casing.
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
