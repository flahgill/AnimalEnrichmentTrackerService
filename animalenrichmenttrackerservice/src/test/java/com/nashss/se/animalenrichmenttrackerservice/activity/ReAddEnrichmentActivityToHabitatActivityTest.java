package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ReAddEnrichmentActivityToHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ReAddEnrichmentActivityToHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.EnrichmentActivityDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.EnrichmentActivityCurrentlyOnHabitatException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.IncompatibleHabitatIdException;
import com.nashss.se.animalenrichmenttrackerservice.helper.EnrichmentActivityTestHelper;
import com.nashss.se.animalenrichmenttrackerservice.helper.HabitatTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReAddEnrichmentActivityToHabitatActivityTest {
    @Mock
    private HabitatDao habitatDao;
    @Mock
    private EnrichmentActivityDao enrichmentActivityDao;
    private ReAddEnrichmentActivityToHabitatActivity reAddEnrichmentActivityToHabitatActivity;
    @BeforeEach
    public void setup() {
        initMocks(this);
        reAddEnrichmentActivityToHabitatActivity = new ReAddEnrichmentActivityToHabitatActivity(habitatDao, enrichmentActivityDao);
    }

    @Test
    public void handleRequest_validRequest_addsEAToHabitat() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitatWithNEnrichments(3);
        String habitatId = habitat.getHabitatId();
        String keeperId = habitat.getKeeperManagerId();

        EnrichmentActivity activity = EnrichmentActivityTestHelper.generateEnrichmentActivity(1);
        activity.setOnHabitat(false);
        activity.setHabitatId(habitatId);
        String activityId = activity.getActivityId();

        when(habitatDao.getHabitat(habitatId)).thenReturn(habitat);
        when(habitatDao.saveHabitat(habitat)).thenReturn(habitat);
        when(enrichmentActivityDao.getEnrichmentActivity(activityId)).thenReturn(activity);
        when(enrichmentActivityDao.saveEnrichmentActivity(activity)).thenReturn(activity);

        ReAddEnrichmentActivityToHabitatRequest request = ReAddEnrichmentActivityToHabitatRequest.builder()
                .withHabitatId(habitatId)
                .withActivityId(activityId)
                .withKeeperManagerId(keeperId)
                .build();

        // WHEN
        ReAddEnrichmentActivityToHabitatResult result = reAddEnrichmentActivityToHabitatActivity.handleRequest(request);

        // THEN
        verify(habitatDao).saveHabitat(habitat);
        verify(enrichmentActivityDao).saveEnrichmentActivity(activity);
        assertEquals(4, result.getCompletedEnrichments().size());
        assertEquals(activityId, result.getCompletedEnrichments().get(0).getActivityId());
        assertTrue(result.getCompletedEnrichments().get(0).getOnHabitat());
    }

    @Test
    public void handleRequest_activityCurrentlyOnHabitat_throwsEnrichmentActivityCurrentlyOnHabitatException() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitatWithNEnrichments(3);
        String habitatId = habitat.getHabitatId();
        String keeperId = habitat.getKeeperManagerId();

        EnrichmentActivity activity = EnrichmentActivityTestHelper.generateEnrichmentActivity(1);
        activity.setHabitatId(habitatId);
        String activityId = activity.getActivityId();

        when(habitatDao.getHabitat(habitatId)).thenReturn(habitat);
        when(enrichmentActivityDao.getEnrichmentActivity(activityId)).thenReturn(activity);

        ReAddEnrichmentActivityToHabitatRequest request = ReAddEnrichmentActivityToHabitatRequest.builder()
                .withHabitatId(habitatId)
                .withActivityId(activityId)
                .withKeeperManagerId(keeperId)
                .build();

        // WHEN
        assertThrows(EnrichmentActivityCurrentlyOnHabitatException.class, ()-> reAddEnrichmentActivityToHabitatActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_incompatibleHabitatId_throwsIncompatibleHabitatIdException() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitatWithNEnrichments(3);
        String habitatId = habitat.getHabitatId();
        String keeperId = habitat.getKeeperManagerId();

        EnrichmentActivity activity = EnrichmentActivityTestHelper.generateEnrichmentActivity(1);
        activity.setOnHabitat(false);
        String activityId = activity.getActivityId();

        when(habitatDao.getHabitat(habitatId)).thenReturn(habitat);
        when(enrichmentActivityDao.getEnrichmentActivity(activityId)).thenReturn(activity);

        ReAddEnrichmentActivityToHabitatRequest request = ReAddEnrichmentActivityToHabitatRequest.builder()
                .withHabitatId(habitatId)
                .withActivityId(activityId)
                .withKeeperManagerId(keeperId)
                .build();

        // WHEN
        assertThrows(IncompatibleHabitatIdException.class, ()-> reAddEnrichmentActivityToHabitatActivity.handleRequest(request));
    }
}
