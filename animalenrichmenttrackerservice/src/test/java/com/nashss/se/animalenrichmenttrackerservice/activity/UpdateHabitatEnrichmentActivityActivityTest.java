package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.UpdateHabitatEnrichmentActivityRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.UpdateHabitatEnrichmentActivityResult;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.EnrichmentActivityDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.EnrichmentActivityNotOnHabitatException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.HabitatNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.helper.EnrichmentActivityTestHelper;
import com.nashss.se.animalenrichmenttrackerservice.helper.HabitatTestHelper;
import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentActivityModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UpdateHabitatEnrichmentActivityActivityTest {
    @Mock
    private HabitatDao habitatDao;
    @Mock
    private EnrichmentActivityDao enrichmentActivityDao;
    private UpdateHabitatEnrichmentActivityActivity updateHabitatEnrichmentActivityActivity;
    @BeforeEach
    public void setup() {
        initMocks(this);
        updateHabitatEnrichmentActivityActivity = new UpdateHabitatEnrichmentActivityActivity(habitatDao, enrichmentActivityDao);
    }

    @Test
    public void handleRequest_validRequest_updatesBothEAandCompletedEnrichments() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitatWithNEnrichments(3);
        String habitatId = habitat.getHabitatId();
        String keeperId = habitat.getKeeperManagerId();
        String activityId = "456";
        String expIsComplete = "isComplete";
        int expKeeperRating = 10;
        LocalDate expDate = LocalDate.of(2024, 06, 03);

        EnrichmentActivity activityToUpdate = EnrichmentActivityTestHelper.generateEnrichmentActivity(1);
        activityToUpdate.setActivityId(activityId);
        activityToUpdate.setOnHabitat(true);
        EnrichmentActivityModel updatedActivityModel = new ModelConverter().toEnrichmentActivityModel(activityToUpdate);

        List<EnrichmentActivity> currActivityList = habitat.getCompletedEnrichments();
        currActivityList.add(activityToUpdate);

        habitat.setCompletedEnrichments(currActivityList);

        when(habitatDao.getHabitat(habitatId)).thenReturn(habitat);
        when(habitatDao.saveHabitat(habitat)).thenReturn(habitat);
        when(enrichmentActivityDao.getEnrichmentActivity(activityId)).thenReturn(activityToUpdate);
        when(enrichmentActivityDao.saveEnrichmentActivity(activityToUpdate)).thenReturn(activityToUpdate);

        UpdateHabitatEnrichmentActivityRequest request = UpdateHabitatEnrichmentActivityRequest.builder()
                .withHabitatId(habitatId)
                .withKeeperManagerId(keeperId)
                .withActivityId(activityId)
                .withIsComplete(expIsComplete)
                .withKeeperRating(expKeeperRating)
                .withDateCompleted(expDate)
                .build();

        // WHEN
        UpdateHabitatEnrichmentActivityResult result = updateHabitatEnrichmentActivityActivity.handleRequest(request);

        // THEN
        verify(habitatDao).saveHabitat(habitat);
        verify(enrichmentActivityDao).saveEnrichmentActivity(activityToUpdate);
        assertEquals(4, result.getCompletedEnrichments().size());
        assertEquals(activityId, result.getCompletedEnrichments().get(0).getActivityId());
        assertEquals(expIsComplete, result.getCompletedEnrichments().get(0).getIsComplete());
        assertEquals(expKeeperRating, result.getCompletedEnrichments().get(0).getKeeperRating());
        assertEquals(expDate, result.getCompletedEnrichments().get(0).getDateCompleted());
    }

    @Test
    public void handleRequest_activityNotOnHabitat_throwsEnrichmentActivityNotOnHabitatException() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitatWithNEnrichments(3);
        String habitatId = habitat.getHabitatId();
        String keeperId = habitat.getKeeperManagerId();
        String activityId = "456";
        String expIsComplete = "isComplete";
        int expKeeperRating = 10;
        LocalDate expDate = LocalDate.of(2024, 06, 03);

        EnrichmentActivity activityToUpdate = EnrichmentActivityTestHelper.generateEnrichmentActivity(1);
        activityToUpdate.setActivityId(activityId);
        activityToUpdate.setOnHabitat(false);
        EnrichmentActivityModel updatedActivityModel = new ModelConverter().toEnrichmentActivityModel(activityToUpdate);

        List<EnrichmentActivity> currActivityList = habitat.getCompletedEnrichments();
        currActivityList.add(activityToUpdate);

        habitat.setCompletedEnrichments(currActivityList);

        when(habitatDao.getHabitat(habitatId)).thenReturn(habitat);
        when(habitatDao.saveHabitat(habitat)).thenReturn(habitat);
        when(enrichmentActivityDao.getEnrichmentActivity(activityId)).thenReturn(activityToUpdate);
        when(enrichmentActivityDao.saveEnrichmentActivity(activityToUpdate)).thenReturn(activityToUpdate);

        UpdateHabitatEnrichmentActivityRequest request = UpdateHabitatEnrichmentActivityRequest.builder()
                .withHabitatId(habitatId)
                .withKeeperManagerId(keeperId)
                .withActivityId(activityId)
                .withIsComplete(expIsComplete)
                .withKeeperRating(expKeeperRating)
                .withDateCompleted(expDate)
                .build();

        // WHEN && THEN
        assertThrows(EnrichmentActivityNotOnHabitatException.class, () -> updateHabitatEnrichmentActivityActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_noHabitatFound_throwsHabitatNotFoundException() {
        // GIVEN
        String habitatId = "fake id";
        UpdateHabitatEnrichmentActivityRequest request = UpdateHabitatEnrichmentActivityRequest.builder()
                .withHabitatId(habitatId)
                .withKeeperManagerId("12345")
                .withActivityId("789")
                .build();
        when(habitatDao.getHabitat(habitatId)).thenThrow(new HabitatNotFoundException());

        // WHEN + THEN
        assertThrows(HabitatNotFoundException.class, () -> updateHabitatEnrichmentActivityActivity.handleRequest(request));
    }
}
