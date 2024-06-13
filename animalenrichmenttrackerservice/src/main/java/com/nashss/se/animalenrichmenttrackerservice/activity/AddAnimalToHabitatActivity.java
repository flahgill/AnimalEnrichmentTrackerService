package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.AddAnimalToHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.AddAnimalToHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.AnimalDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Animal;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.DuplicateAnimalException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.InvalidCharacterException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.UserSecurityException;
import com.nashss.se.animalenrichmenttrackerservice.utils.ServiceUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Implementation of the AddAnimalToHabitatActivity for the AnimalEnrichmentTrackerServices's
 * AddAnimalToHabitat API.
 * <p>
 * This API allows a keeper manager to add an animal to a habitat's list of animals saved in DDB.
 */
public class AddAnimalToHabitatActivity {
    private final Logger log = LogManager.getLogger();
    private final HabitatDao habitatDao;
    private final AnimalDao animalDao;

    /**
     * Instantiates a new AddAnimalToHabitatActivity object.
     *
     * @param habitatDao the HabitatDao to access the Habitats DDB table.
     * @param animalDao the AnimalDao to acces the Animals DDB Table.
     */
    @Inject
    public AddAnimalToHabitatActivity(HabitatDao habitatDao, AnimalDao animalDao) {
        this.habitatDao = habitatDao;
        this.animalDao = animalDao;
    }

    /**
     * handles the incoming request by retrieving a habitat's list of animals, adding the new animal's name,
     * saving the new list within the habitat, and saving the new animal to the animals table.
     * <p>
     * returns the newly saved animal.
     * <p>
     * If the provided habitat name or keeper ID has invalid characters, throws an
     * InvalidCharacterException.
     * <p>
     * If the animal is already present in the habitat's list of animals, throws a
     * DuplicateAnimalException.
     * <p>
     * If the keeper adding the animal is not the owner of the habitat, throws a UserSecurityException.
     *
     * @param addAnimalToHabitatRequest request object containing the habitatId and animal to be added.
     * @return addAnimalToHabitatResult result object containing newly saved animal.
     */
    public AddAnimalToHabitatResult handleRequest(final AddAnimalToHabitatRequest addAnimalToHabitatRequest) {
        log.info("Recieved AddAnimalToHabitatActivity {}", addAnimalToHabitatRequest);

        Habitat habitat = habitatDao.getHabitat(addAnimalToHabitatRequest.getHabitatId());

        if (!habitat.getKeeperManagerId().equals(addAnimalToHabitatRequest.getKeeperManagerId())) {
            throw new UserSecurityException("You must own this habitat to add a new animal to it.");
        }

        List<String> currAnimalsInHabitat = habitat.getAnimalsInHabitat();

        if (currAnimalsInHabitat == null) {
            currAnimalsInHabitat = new ArrayList<>();
        }

        List<String> updatedAnimalsList = new ArrayList<>(currAnimalsInHabitat);

        int totalAnimals = habitat.getTotalAnimals();
        String animalNameToAdd = addAnimalToHabitatRequest.getAnimalName();

        if (!ServiceUtils.isValidString(animalNameToAdd)) {
            throw new InvalidCharacterException("Animal name [" + animalNameToAdd + "] contains illegal characters.");
        }

        if (containsIgnoreCase(currAnimalsInHabitat, animalNameToAdd)) {
            throw new DuplicateAnimalException("[" + animalNameToAdd + "] is already in the habitat.");
        }

        updatedAnimalsList.add(animalNameToAdd);
        totalAnimals += 1;
        Collections.sort(updatedAnimalsList);
        habitat.setAnimalsInHabitat(updatedAnimalsList);
        habitat.setTotalAnimals(totalAnimals);
        habitat = habitatDao.saveHabitat(habitat);

        Animal animal = new Animal();
        animal.setAnimalId(ServiceUtils.generateId());
        animal.setAnimalName(animalNameToAdd);
        animal.setHabitatId(addAnimalToHabitatRequest.getHabitatId());
        animal.setAge(addAnimalToHabitatRequest.getAge());
        animal.setSex(addAnimalToHabitatRequest.getSex());
        animal.setSpecies(addAnimalToHabitatRequest.getSpecies());
        animal.setIsActive("active");
        animal.setOnHabitat(true);

        animalDao.saveAnimal(animal);

        return AddAnimalToHabitatResult.builder()
                .withAddedAnimal(new ModelConverter().toAnimalModel(animal))
                .build();
    }

    /**
     * helper method to determine if the animal being added is already present in the habitat's current
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
