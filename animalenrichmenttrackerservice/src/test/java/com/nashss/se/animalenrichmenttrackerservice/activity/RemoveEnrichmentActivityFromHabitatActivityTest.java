package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.AddEnrichmentActivityToHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.requests.RemoveEnrichmentActivityFromHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.RemoveEnrichmentActivityFromHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.EnrichmentActivityDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.EnrichmentActivityNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.EnrichmentNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.HabitatNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.helper.EnrichmentActivityTestHelper;
import com.nashss.se.animalenrichmenttrackerservice.helper.HabitatTestHelper;
import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentActivityModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RemoveEnrichmentActivityFromHabitatActivityTest {
    @Mock
    private HabitatDao habitatDao;
    @Mock
    private EnrichmentActivityDao enrichmentActivityDao;
    private RemoveEnrichmentActivityFromHabitatActivity removeEnrichmentActivityFromHabitatActivity;
    @BeforeEach
    public void setup() {
        initMocks(this);
        removeEnrichmentActivityFromHabitatActivity = new RemoveEnrichmentActivityFromHabitatActivity(habitatDao, enrichmentActivityDao);
    }

    @Test
    public void handleRequest_validRequest_removesEnrichmentActivityFromHabtiat() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitatWithNEnrichments(3);
        String habitatId = habitat.getHabitatId();
        String keeperId = habitat.getKeeperManagerId();
        String activityId = "456";

        EnrichmentActivity activityToRemove = EnrichmentActivityTestHelper.generateEnrichmentActivity(1);
        activityToRemove.setActivityId(activityId);
        EnrichmentActivityModel removedActivityModel = new ModelConverter().toEnrichmentActivityModel(activityToRemove);

        List<EnrichmentActivity> currActivityList = habitat.getCompletedEnrichments();
        currActivityList.add(activityToRemove);

        habitat.setCompletedEnrichments(currActivityList);

        when(habitatDao.getHabitat(habitatId)).thenReturn(habitat);
        when(habitatDao.saveHabitat(habitat)).thenReturn(habitat);

        RemoveEnrichmentActivityFromHabitatRequest request = RemoveEnrichmentActivityFromHabitatRequest.builder()
                .withActivityId(activityId)
                .withKeeperManagerId(keeperId)
                .withHabitatId(habitatId)
                .build();

        // WHEN
        RemoveEnrichmentActivityFromHabitatResult result = removeEnrichmentActivityFromHabitatActivity.handleRequest(request);

        // THEN
        verify(habitatDao).saveHabitat(habitat);
        assertEquals(3, result.getCompletedEnrichments().size());
        assertFalse(result.getCompletedEnrichments().contains(removedActivityModel));
    }

    @Test
    public void handleRequest_noActivityFound_throwsEnrichmentActivityNotFoundException() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitatWithNEnrichments(3);
        String habitatId = habitat.getHabitatId();
        String keeperId = habitat.getKeeperManagerId();
        String activityId = "456";

        RemoveEnrichmentActivityFromHabitatRequest request = RemoveEnrichmentActivityFromHabitatRequest.builder()
                .withActivityId(activityId)
                .withKeeperManagerId(keeperId)
                .withHabitatId(habitatId)
                .build();

        // WHEN
        when(habitatDao.getHabitat(habitatId)).thenReturn(habitat);

        // THEN
        assertThrows(EnrichmentActivityNotFoundException.class, ()-> removeEnrichmentActivityFromHabitatActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_noHabitatFound_throwsHabitatNotFoundException() {
        // GIVEN
        String habitatId = "fake id";
        RemoveEnrichmentActivityFromHabitatRequest request = RemoveEnrichmentActivityFromHabitatRequest.builder()
                .withHabitatId(habitatId)
                .withKeeperManagerId("12345")
                .withActivityId("789")
                .build();
        when(habitatDao.getHabitat(habitatId)).thenThrow(new HabitatNotFoundException());

        // WHEN + THEN
        assertThrows(HabitatNotFoundException.class, () -> removeEnrichmentActivityFromHabitatActivity.handleRequest(request));
    }
}
