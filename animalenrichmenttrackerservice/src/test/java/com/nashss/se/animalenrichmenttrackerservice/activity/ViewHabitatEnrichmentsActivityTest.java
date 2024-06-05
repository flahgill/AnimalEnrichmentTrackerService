package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewHabitatEnrichmentActivitesRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewHabitatEnrichmentActivitesResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.HabitatNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.helper.EnrichmentActivityTestHelper;
import com.nashss.se.animalenrichmenttrackerservice.helper.HabitatTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ViewHabitatEnrichmentsActivityTest {
    @Mock
    private HabitatDao habitatDao;
    private ViewHabitatEnrichmentActivitiesActivity viewHabitatEnrichmentsActivity;
    @BeforeEach
    public void setup() {
        initMocks(this);
        viewHabitatEnrichmentsActivity = new ViewHabitatEnrichmentActivitiesActivity(habitatDao);
    }

    @Test
    public void handleRequest_HabitatExistsWithEnrichments_returnsSortedEnrichmentsList() {
        // GIVEN
        EnrichmentActivity e1 = EnrichmentActivityTestHelper.generateEnrichmentActivity(1);
        e1.setDateCompleted(LocalDate.of(2024, 5, 27));
        EnrichmentActivity e2 = EnrichmentActivityTestHelper.generateEnrichmentActivity(2);
        e2.setDateCompleted(LocalDate.of(2024, 5, 28));
        EnrichmentActivity e3 = EnrichmentActivityTestHelper.generateEnrichmentActivity(3);
        e3.setDateCompleted(LocalDate.of(2024, 5, 29));

        Habitat habitat = HabitatTestHelper.generateHabitat();
        habitat.setCompletedEnrichments(List.of(e1, e2, e3));

        ViewHabitatEnrichmentActivitesRequest request = ViewHabitatEnrichmentActivitesRequest.builder()
                .withHabitatId(habitat.getHabitatId())
                .build();

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);

        // WHEN
        ViewHabitatEnrichmentActivitesResult result = viewHabitatEnrichmentsActivity.handleRequest(request);

        // THEN
        assertEquals(3, result.getCompletedEnrichments().size());
        assertEquals(e3.getEnrichmentId(), result.getCompletedEnrichments().get(0).getEnrichmentId());
        assertEquals(e2.getEnrichmentId(), result.getCompletedEnrichments().get(1).getEnrichmentId());
        assertEquals(e1.getEnrichmentId(), result.getCompletedEnrichments().get(2).getEnrichmentId());
    }

    @Test
    public void handleRequest_habitatExistsWithoutEnrichments_returnsEmptyList() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitatWithNEnrichments(0);

        ViewHabitatEnrichmentActivitesRequest request = ViewHabitatEnrichmentActivitesRequest.builder()
                .withHabitatId(habitat.getHabitatId())
                .build();

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);

        // WHEN
        ViewHabitatEnrichmentActivitesResult result = viewHabitatEnrichmentsActivity.handleRequest(request);

        // THEN
        assertNotNull(result);
        assertTrue(result.getCompletedEnrichments().isEmpty());
    }

    @Test
    public void handleRequest_noHabitatFound_throwsHabitatNotFoundException() {
        // WHEN
        String fakeId = "notRealId";

        ViewHabitatEnrichmentActivitesRequest request = ViewHabitatEnrichmentActivitesRequest.builder()
                .withHabitatId(fakeId)
                .build();

        // WHEN
        when(habitatDao.getHabitat(fakeId)).thenThrow(new HabitatNotFoundException());

        // WHEN + THEN
        assertThrows(HabitatNotFoundException.class, () -> viewHabitatEnrichmentsActivity.handleRequest(request));
    }
}
