package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ReactivateAnimalRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ReactivateAnimalResult;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.AnimalDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Animal;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.AnimalCurrentlyOnHabitatException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.DuplicateAnimalException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.IncompatibleSpeciesException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.UserSecurityException;

import com.nashss.se.animalenrichmenttrackerservice.models.AnimalModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Implementation of the ReactivateAnimalActivity for the AnimalEnrichmentTrackerServices's ReactivateAnimal API.
 * <p>
 * This API allows a keeper manager to re-add an animal to a habitat.
 */
public class ReactivateAnimalActivity {
    private final Logger log = LogManager.getLogger();
    private final HabitatDao habitatDao;
    private final AnimalDao animalDao;

    /**
     * Instantiates new ReactivateAnimalActivity.
     *
     * @param habitatDao the HabitatDao to access the habitat's DDB table.
     * @param animalDao the AnimalDao to access the animal's DDB table.
     */
    @Inject
    public ReactivateAnimalActivity(HabitatDao habitatDao, AnimalDao animalDao) {
        this.habitatDao = habitatDao;
        this.animalDao = animalDao;
    }

    /**
     * handles incoming request by retrieving the animal to be reactivated, adding to a habitat, and saving both the
     * animal and habitat.
     * <p>
     * returns the updated animal.
     * <p>
     * If the keeper does not own the habitat they are trying to add the animal to, will throw UserSecurityException.
     * <p>
     * If the species on the requested habitat do not match the animal's species type, will throw
     * IncompatibleSpeciesException.
     * <p>
     * If the animal is already on a habitat, will throw AnimalCurrentlyOnHabitatException.
     *
     * @param reactivateAnimalRequest request object containing the animalId, habitatId, and keeperManagerId.
     * @return ReactivateAnimalResult result object containing the API defined {@link AnimalModel}
     */
    public ReactivateAnimalResult handleRequest(final ReactivateAnimalRequest reactivateAnimalRequest) {
        log.info("Recieved ReactivateAnimalRequest {}", reactivateAnimalRequest);

        String keeperManagerId = reactivateAnimalRequest.getKeeperManagerId();
        String habitatId = reactivateAnimalRequest.getHabitatId();
        String animalId = reactivateAnimalRequest.getAnimalId();

        Animal animalToReactivate = animalDao.getAnimal(animalId);
        Habitat habitatToAddTo = habitatDao.getHabitat(habitatId);

        if (animalToReactivate.getOnHabitat()) {
            throw new AnimalCurrentlyOnHabitatException("Animal[" + animalId + "] is already on a habitat[" +
                    animalToReactivate.getHabitatId() + "].");
        }

        if (!keeperIsOwnerOfRequestedHabitat(keeperManagerId, habitatId)) {
            throw new UserSecurityException("You must own the habitat to add the animal [" + animalId + "] to.");
        }

        if (!animalSpeciesMatchesHabitatSpecies(animalToReactivate, habitatToAddTo)) {
            throw new IncompatibleSpeciesException("Species listed on habitat [" + habitatId + "] do not match " +
                    "animal's species [" + animalToReactivate.getSpecies() + "].");
        }

        List<String> copiedCurrAnimals = getHabitatAnimalsAndCopy(habitatToAddTo);
        String animalName = animalToReactivate.getAnimalName();

        if (containsIgnoreCase(copiedCurrAnimals, animalName)) {
            throw new DuplicateAnimalException("Habitat already contains animal with name [" + animalName + "].");
        }

        animalToReactivate.setIsActive("active");
        animalToReactivate.setOnHabitat(true);
        animalToReactivate.setHabitatId(habitatId);
        animalToReactivate = animalDao.saveAnimal(animalToReactivate);

        copiedCurrAnimals.add(animalName);
        Collections.sort(copiedCurrAnimals);
        habitatToAddTo.setAnimalsInHabitat(copiedCurrAnimals);
        int totalAnimals = habitatToAddTo.getTotalAnimals();
        totalAnimals += 1;
        habitatToAddTo.setTotalAnimals(totalAnimals);
        habitatToAddTo = habitatDao.saveHabitat(habitatToAddTo);

        return ReactivateAnimalResult.builder()
                .withAnimal(new ModelConverter().toAnimalModel(animalToReactivate))
                .build();
    }

    /**
     * private helper method to determine if the requested habitatId is included in the habitats that the keeper owns.
     *
     * @param keeperManagerId the keeperManagerId to fetch the keeper's list of habitats.
     * @param requestHabitatId the habitatId being requested to add the animal to.
     * @return boolean determining if the requested habitatId is included in the keeper's list of habitats.
     */
    private Boolean keeperIsOwnerOfRequestedHabitat(String keeperManagerId, String requestHabitatId) {
        List<Habitat> keeperHabitats = habitatDao.getAllHabitatsForKeeper(keeperManagerId);

        List<String> keeperHabitatIds = new ArrayList<>();
        for (Habitat habitat : keeperHabitats) {
            keeperHabitatIds.add(habitat.getHabitatId());
        }

        return keeperHabitatIds.contains(requestHabitatId);
    }

    /**
     * private helper method to determine if the requested animal species matches the requested habitat's list of
     * species.
     *
     * @param animal the requested animal.
     * @param habitat the requested habitat.
     * @return boolean determining if the requested species match and animal can be added.
     */
    private Boolean animalSpeciesMatchesHabitatSpecies(Animal animal, Habitat habitat) {
        List<String> habitatSpecies = habitat.getSpecies();

        if (habitatSpecies == null) {
            habitatSpecies = new ArrayList<>();
        }

        return habitatSpecies.contains(animal.getSpecies());
    }

    /**
     * private helper method to pull and copy the list of current animals from the requested habitat.
     *
     * @param habitat the requested habitat to pull the current list of animals from.
     * @return the copied current list of animals on a given habitat.
     */
    private List<String> getHabitatAnimalsAndCopy(Habitat habitat) {
        List<String> currAnimals = habitat.getAnimalsInHabitat();

        return new ArrayList<>(currAnimals);
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
