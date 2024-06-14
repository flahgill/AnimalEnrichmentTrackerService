package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.UpdateAnimalRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.UpdateAnimalResult;
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
import java.util.List;

import javax.inject.Inject;


/**
 * Implementation of the UpdateAnimalActivity for the AnimalEnrichmentTrackerService API.
 *
 * This API allows the keeper to update one of their saved animals.
 */
public class UpdateAnimalActivity {
    private final Logger log = LogManager.getLogger();
    private final HabitatDao habitatDao;
    private final AnimalDao animalDao;

    /**
     * Instantiates UpdateAnimalActivity object.
     * @param habitatDao the HabitatDao to access the habitat's DDB table.
     * @param animalDao the AnimalDao to access the animal's DDB table.
     */
    @Inject
    public UpdateAnimalActivity(HabitatDao habitatDao, AnimalDao animalDao) {
        this.habitatDao = habitatDao;
        this.animalDao = animalDao;
    }

    /**
     * handles the incoming request by retrieving the animal, updating it, and persisting the animal.
     * <p>
     * If animal is currenly on a habitat, will also pull the habitat's list of animals to update and check that name
     * is not duplicated.
     * <p>
     * If the provided animalName has invalid charachters, throws and InvalidCharacterException.
     * <p>
     * If the animal is on a habitat, and the owner trying to update that animal is not the owner of that habitat,
     * throws UserSecurityException.
     *
     * @param updateAnimalRequest request object containing the animalId, animalName, age, sex, and species.
     * @return UpdateAnimalResult result object containing the animal that was updated.
     */
    public UpdateAnimalResult handleRequest(final UpdateAnimalRequest updateAnimalRequest) {
        log.info("Recieved UpdateAnimalRequest {}", updateAnimalRequest);

        Animal animal = animalDao.getAnimal(updateAnimalRequest.getAnimalId());

        String oldName = animal.getAnimalName();
        String updateName = updateAnimalRequest.getAnimalName();
        if (updateName != null) {
            if (updateName.isEmpty()) {
                updateName = animal.getAnimalName();
            }

            if (!ServiceUtils.isValidString(updateName)) {
                throw new InvalidCharacterException("Animal name [" + updateName +
                        "] contains invalid characters.");
            }
            animal.setAnimalName(updateName);
        }

        int updateAge = updateAnimalRequest.getAge();
        animal.setAge(updateAge);

        String updatedSex = updateAnimalRequest.getSex();
        if (updatedSex != null) {
            if (updatedSex.isEmpty()) {
                updatedSex = animal.getSex();
            }
            animal.setSex(updatedSex);
        }


        String updateSpecies = updateAnimalRequest.getSpecies();
        if (updateSpecies != null) {
            if (updateSpecies.isEmpty()) {
                updateSpecies = animal.getSpecies();
            }
            animal.setSpecies(updateSpecies);
        }


        if (animal.getOnHabitat()) {
            if (updateName != null) {
                Habitat habitat = habitatDao.getHabitat(animal.getHabitatId());

                if (!updateAnimalRequest.getKeeperManagerId().equals(habitat.getKeeperManagerId())) {
                    throw new UserSecurityException("Must own habitat that animal is currently on to update the " +
                            "animal.");
                }

                List<String> updateAnimals = new ArrayList<>(habitat.getAnimalsInHabitat());

                if (containsIgnoreCase(updateAnimals, updateName)) {
                    throw new DuplicateAnimalException("Name [" + updateName + "] is already in use for the habitat.");
                }

                updateAnimals.remove(oldName);
                updateAnimals.add(updateName);
                habitat.setAnimalsInHabitat(updateAnimals);
                habitat = habitatDao.saveHabitat(habitat);
                animal.setAnimalName(updateName);
            }
        }



        animal = animalDao.saveAnimal(animal);

        return UpdateAnimalResult.builder()
                .withAnimal(new ModelConverter().toAnimalModel(animal))
                .build();
    }

    /**
     * helper method to determine if the animal being added is already present in the habitat's current
     * list of animals. Case Insensitive.
     *
     * @param animals list of animals in a habitat.
     * @param searchTerm animal to be updated and searched in the current list.
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
