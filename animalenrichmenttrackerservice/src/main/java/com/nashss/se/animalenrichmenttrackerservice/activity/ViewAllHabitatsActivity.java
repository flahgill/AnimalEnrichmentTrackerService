package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewAllHabitatsRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewAllHabitatsResult;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.models.HabitatModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import javax.inject.Inject;

public class ViewAllHabitatsActivity {
    private final Logger log = LogManager.getLogger();
    private final HabitatDao habitatDao;

    /**
     * Instantiates new ViewAllHabitatsActivity.
     *
     * @param habitatDao HabitatDao to access Habitat's table.
     */
    @Inject
    public ViewAllHabitatsActivity(HabitatDao habitatDao) {
        this.habitatDao = habitatDao;
    }

    /**
     * This method handles the incoming request by retrieving all habitat's based on the user's
     * input of active status.
     * <p>
     * It then returns a list of habitats.
     *
     * @param viewAllHabitatsRequest request object containing the active status.
     * @return viewAllHabitatsResult result object containgin a list of API defined {@link HabitatModel}
     */
    public ViewAllHabitatsResult handleRequest(final ViewAllHabitatsRequest viewAllHabitatsRequest) {
        log.info("Recieved ViewAllHabitatsRequest {}", viewAllHabitatsRequest);

        String activeStatusRequest = viewAllHabitatsRequest.getIsActive();
        if (activeStatusRequest == null) {
            activeStatusRequest = "active";
        }

        List<Habitat> habitats = habitatDao.getAllHabitats(activeStatusRequest);
        List<HabitatModel> habitatModels = new ModelConverter().toHabitatModelList(habitats);

        return ViewAllHabitatsResult.builder()
                .withHabitats(habitatModels)
                .build();

    }
}
