package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewUserHabitatsRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewUserHabitatsResult;
import com.nashss.se.animalenrichmenttrackerservice.comparators.HabitatNameComparator;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.models.HabitatModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import javax.inject.Inject;

public class ViewUserHabitatsActivity {
    private final Logger log = LogManager.getLogger();
    private final HabitatDao habitatDao;

    /**
     * Instantiates new ViewUserHabitatsActivity.
     *
     * @param habitatDao HabitatDao to access Habitat's table
     */
    @Inject
    public ViewUserHabitatsActivity(HabitatDao habitatDao) {
        this.habitatDao = habitatDao;
    }

    /**
     * This method handles the incoming request by retrieving the keeper's habitats from the DB.
     * <p>
     * It then returns a list of habitats.
     *
     * @param viewUserHabitatsRequest request object containing the keeperManagerId
     * @return viewUserHabitatsResult result object containing the API defined {@link HabitatModel}
     */
    public ViewUserHabitatsResult handleRequest(final ViewUserHabitatsRequest viewUserHabitatsRequest) {
        log.info("Recieved ViewUserHabitatsRequest {}", viewUserHabitatsRequest);

        List<Habitat> userHabitats = habitatDao.getAllHabitatsForKeeper(viewUserHabitatsRequest.getKeeperManagerId());
        List<HabitatModel> userHabitatsModels = new ModelConverter().toHabitatModelList(userHabitats);
        userHabitatsModels.sort(new HabitatNameComparator());

        return ViewUserHabitatsResult.builder()
                .withHabitats(userHabitatsModels)
                .build();
    }

}
