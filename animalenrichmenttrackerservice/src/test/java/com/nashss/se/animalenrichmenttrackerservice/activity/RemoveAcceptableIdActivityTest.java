package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.RemoveAcceptableIdRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.RemoveAcceptableIdResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.AcceptableIdNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.helper.HabitatTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RemoveAcceptableIdActivityTest {
    @Mock
    private HabitatDao habitatDao;
    private RemoveAcceptableIdActivity removeAcceptableIdActivity;
    @BeforeEach
    public void setup() {
        initMocks(this);
        removeAcceptableIdActivity = new RemoveAcceptableIdActivity(habitatDao);
    }

    @Test
    public void handleRequest_idInHabitat_removesId() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();
        habitat.setAcceptableEnrichmentIds(List.of("01", "02"));
        String idToRemove = "01";

        RemoveAcceptableIdRequest request = RemoveAcceptableIdRequest.builder()
                .withIdToRemove(idToRemove)
                .withHabitatId(habitat.getHabitatId())
                .withKeeperManagerId(habitat.getKeeperManagerId())
                .build();

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);
        when(habitatDao.saveHabitat(habitat)).thenReturn(habitat);

        // WHEN
        RemoveAcceptableIdResult result = removeAcceptableIdActivity.handleRequest(request);

        // THEN
        assertEquals(1, result.getAcceptableEnrichmentIds().size());
        assertFalse(result.getAcceptableEnrichmentIds().contains(idToRemove));
    }

    @Test
    public void handleRequest_idNotInHabitat_throwsAcceptableIdNotInHabitatException() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();
        habitat.setAcceptableEnrichmentIds(List.of("01", "02"));
        String idToRemove = "03";

        RemoveAcceptableIdRequest request = RemoveAcceptableIdRequest.builder()
                .withIdToRemove(idToRemove)
                .withHabitatId(habitat.getHabitatId())
                .withKeeperManagerId(habitat.getKeeperManagerId())
                .build();

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);

        // WHEN && THEN
        assertThrows(AcceptableIdNotFoundException.class, () -> removeAcceptableIdActivity.handleRequest(request));
    }
}
