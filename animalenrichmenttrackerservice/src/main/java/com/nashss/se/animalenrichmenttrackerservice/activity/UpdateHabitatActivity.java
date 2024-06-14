package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.UpdateHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.UpdateHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.InvalidCharacterException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.UserSecurityException;
import com.nashss.se.animalenrichmenttrackerservice.metrics.MetricsConstants;
import com.nashss.se.animalenrichmenttrackerservice.metrics.MetricsPublisher;
import com.nashss.se.animalenrichmenttrackerservice.models.HabitatModel;
import com.nashss.se.animalenrichmenttrackerservice.utils.ServiceUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Implementation of the UpdateHabitatActivity for the AnimalEnrichmentTrackerService API.
 *
 * This API allows the customer to update one of their saved habitats.
 */
public class UpdateHabitatActivity {
    private final Logger log = LogManager.getLogger();
    private final HabitatDao habitatDao;
    private final MetricsPublisher metricsPublisher;

    /**
     * Instantiates a new UpdateHabitatActivity object.
     *
     * @param habitatDao habitatDao to access the habitat table.
     * @param metricsPublisher metricsPublisher to publish metrics to cloudwatch.
     */
    @Inject
    public UpdateHabitatActivity(HabitatDao habitatDao, MetricsPublisher metricsPublisher) {
        this.habitatDao = habitatDao;
        this.metricsPublisher = metricsPublisher;
    }

    /**
     * This method handles the incoming request by retrieving the habitat, updating it,
     * and persisting the habitat.
     * <p>
     * It then returns the updated habitat.
     * <p>
     * If the habitat does not exist, this should throw a HabitatNotFoundException.
     * <p>
     * If the provided habitat name has invalid characters, throws an
     * InvalidCharacterException
     * <p>
     * If the request tries to update the customer ID,
     * this should throw a UserSecurityException
     *
     * @param updateHabitatRequest request object containing the habitatId, habitatName,
     *                             and keeperManagerId associated with it.
     * @return result object containing the API defined {@link HabitatModel}
     */
    public UpdateHabitatResult handleRequest(final UpdateHabitatRequest updateHabitatRequest) {
        log.info("Recieved UpdateHabitatRequest {}", updateHabitatRequest);

        Habitat habitat = habitatDao.getHabitat(updateHabitatRequest.getHabitatId());

        if (!habitat.getKeeperManagerId().equals(updateHabitatRequest.getKeeperManagerId())) {
            metricsPublisher.addCount(MetricsConstants.UPDATEHABITAT_USERSECURITYEXCEPTION, 1);
            throw new UserSecurityException("You must own this habitat to update it.");
        }

        String updateName = updateHabitatRequest.getHabitatName();

        if (updateName != null) {
            if (updateName.isEmpty()) {
                updateName = habitat.getHabitatName();
            }
            if (!ServiceUtils.isValidString(updateName)) {
                metricsPublisher.addCount(MetricsConstants.UPDATEHABITAT_INVALIDCHARACTEREXCEPTION, 1);
                throw new InvalidCharacterException("HabitatName [" + updateHabitatRequest.getHabitatName() +
                        "] contains invalid characters.");
            }

            habitat.setHabitatName(updateName);
        }

        String updateActivity = updateHabitatRequest.getIsActive();


        if (updateActivity == null) {
            updateActivity = "active";
        } else if (updateActivity.isEmpty()) {
            updateActivity = "active";
        }
        habitat.setIsActive(updateActivity);


        habitat = habitatDao.saveHabitat(habitat);

        metricsPublisher.addCount(MetricsConstants.UPDATEHABITAT_INVALIDCHARACTEREXCEPTION, 0);
        metricsPublisher.addCount(MetricsConstants.UPDATEHABITAT_USERSECURITYEXCEPTION, 0);

        return UpdateHabitatResult.builder()
                .withHabitat(new ModelConverter().toHabitatModel(habitat))
                .build();
    }
}
