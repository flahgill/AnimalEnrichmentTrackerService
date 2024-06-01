package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.AddEnrichmentActivityToHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.AddEnrichmentActivityToHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.EnrichmentActivityDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.EnrichmentDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Enrichment;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.EnrichmentNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.HabitatNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.UnsuitableEnrichmentForHabitatException;
import com.nashss.se.animalenrichmenttrackerservice.helper.EnrichmentActivityTestHelper;
import com.nashss.se.animalenrichmenttrackerservice.helper.EnrichmentTestHelper;
import com.nashss.se.animalenrichmenttrackerservice.helper.HabitatTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AddEnrichmentActivityToHabitatActivityTest {
    @Mock
    private HabitatDao habitatDao;
    @Mock
    private EnrichmentDao enrichmentDao;
    @Mock
    private EnrichmentActivityDao enrichmentActivityDao;

    private AddEnrichmentActivityToHabitatActivity addEnrichmentActivityToHabitatActivity;

    @BeforeEach
    public void setup() {
        initMocks(this);
        addEnrichmentActivityToHabitatActivity = new AddEnrichmentActivityToHabitatActivity(habitatDao, enrichmentDao, enrichmentActivityDao);
    }

    @Test
    void handleRequest_validRequest_addsEnrichmentActivityToHabitat() {
        // GIVEN
        // hbaitat with 3 enrichments to start
        Habitat habitat = HabitatTestHelper.generateHabitatWithNEnrichments(3);
        String habitatId = habitat.getHabitatId();
        String keeperId = habitat.getKeeperManagerId();

        Enrichment enrichment = EnrichmentTestHelper.generateEnrichment(1);

        EnrichmentActivity activity = EnrichmentActivityTestHelper.generateEnrichmentActivity(1);
        String enrichId = activity.getEnrichmentId();

        when(habitatDao.getHabitat(habitatId)).thenReturn(habitat);
        when(habitatDao.saveHabitat(habitat)).thenReturn(habitat);
        when(enrichmentDao.getEnrichment(enrichId)).thenReturn(enrichment);

        AddEnrichmentActivityToHabitatRequest request = AddEnrichmentActivityToHabitatRequest.builder()
                .withHabitatId(habitatId)
                .withEnrichmentId(enrichId)
                .withKeeperManagerId(keeperId)
                .withDateCompleted(LocalDate.of(2024, 05, 30))
                .withKeeperRating(8)
                .build();

        // WHEN
        AddEnrichmentActivityToHabitatResult result = addEnrichmentActivityToHabitatActivity.handleRequest(request);

        // THEN
        verify(habitatDao).saveHabitat(habitat);
        assertEquals(4, result.getCompletedEnrichments().size());
    }

    @Test
    public void handleRequest_noHabitatFound_throwsHabitatNotFoundException() {
        // GIVEN
        String habitatId = "fake id";
        AddEnrichmentActivityToHabitatRequest request = AddEnrichmentActivityToHabitatRequest.builder()
                .withHabitatId(habitatId)
                .withEnrichmentId("123")
                .withKeeperManagerId("12345")
                .withDateCompleted(LocalDate.of(2024, 05, 30))
                .withKeeperRating(5)
                .build();
        when(habitatDao.getHabitat(habitatId)).thenThrow(new HabitatNotFoundException());

        // WHEN + THEN
        assertThrows(HabitatNotFoundException.class, () -> addEnrichmentActivityToHabitatActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_noEnrichmentFound_throwsEnrichmentNotFoundException() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();

        String habitatId = habitat.getHabitatId();
        String keeperId = habitat.getKeeperManagerId();
        String enrichmentId = "fake id";

        AddEnrichmentActivityToHabitatRequest request = AddEnrichmentActivityToHabitatRequest.builder()
                .withHabitatId(habitatId)
                .withEnrichmentId(enrichmentId)
                .withKeeperManagerId(keeperId)
                .withDateCompleted(LocalDate.of(2024, 05, 30))
                .withKeeperRating(5)
                .build();

        // WHEN
        when(habitatDao.getHabitat(habitatId)).thenReturn(habitat);
        when(enrichmentDao.getEnrichment(enrichmentId)).thenThrow(new EnrichmentNotFoundException());

        // THEN
        assertThrows(EnrichmentNotFoundException.class, () -> addEnrichmentActivityToHabitatActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_unsuitableEnrichment_throwsUnsuitableEnrichmentForHabitatException() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();
        String habitatId = habitat.getHabitatId();
        String keeperId = habitat.getKeeperManagerId();

        EnrichmentActivity activity = EnrichmentActivityTestHelper.generateEnrichmentActivity(9);
        // 09 is not in HabitatTestHelper::generateHabitat list of acceptableEnrichmentIds.
        String enrichmentId = activity.getEnrichmentId();

        AddEnrichmentActivityToHabitatRequest request = AddEnrichmentActivityToHabitatRequest.builder()
                .withHabitatId(habitatId)
                .withEnrichmentId(enrichmentId)
                .withKeeperManagerId(keeperId)
                .withDateCompleted(LocalDate.of(2024, 05, 30))
                .withKeeperRating(5)
                .build();

        // WHEN
        when(habitatDao.getHabitat(habitatId)).thenReturn(habitat);
        when(enrichmentDao.getEnrichment(enrichmentId)).thenThrow(new UnsuitableEnrichmentForHabitatException());

        // THEN
        assertThrows(UnsuitableEnrichmentForHabitatException.class, () -> addEnrichmentActivityToHabitatActivity.handleRequest(request));
    }
}
