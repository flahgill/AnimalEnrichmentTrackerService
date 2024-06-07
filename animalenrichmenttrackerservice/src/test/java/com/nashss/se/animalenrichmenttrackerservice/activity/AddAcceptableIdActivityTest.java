package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.AddAcceptableIdRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.AddAcceptableIdResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.DuplicateAcceptableIdException;
import com.nashss.se.animalenrichmenttrackerservice.helper.HabitatTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AddAcceptableIdActivityTest {
    @Mock
    private HabitatDao habitatDao;
    private AddAcceptableIdActivity addAcceptableIdActivity;
    @BeforeEach
    public void setup() {
        initMocks(this);
        addAcceptableIdActivity = new AddAcceptableIdActivity(habitatDao);
    }

    @Test
    public void handleRequest_acceptableIdNoOtherIdsInHabitat_addsIdToHabitat() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();
        habitat.setAcceptableEnrichmentIds(new ArrayList<>());
        String expectedId = "01";

        AddAcceptableIdRequest request = AddAcceptableIdRequest.builder()
                .withIdToAdd(expectedId)
                .withHabitatId(habitat.getHabitatId())
                .withKeeperManagerId(habitat.getKeeperManagerId())
                .build();

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);
        when(habitatDao.saveHabitat(habitat)).thenReturn(habitat);

        // WHEN
        AddAcceptableIdResult result = addAcceptableIdActivity.handleRequest(request);

        // THEN
        assertEquals(1, result.getAcceptableEnrichmentIds().size());
        assertEquals(expectedId, result.getAcceptableEnrichmentIds().get(0));
    }

    @Test
    public void handleRequest_acceptableIdOtherIdsInHabitat_addsIdToHabitat() {
        // GIVEN
        // generated habitat with 4 acceptable ids present
        Habitat habitat = HabitatTestHelper.generateHabitat();
        String expectedId = "new id";

        AddAcceptableIdRequest request = AddAcceptableIdRequest.builder()
                .withIdToAdd(expectedId)
                .withHabitatId(habitat.getHabitatId())
                .withKeeperManagerId(habitat.getKeeperManagerId())
                .build();

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);
        when(habitatDao.saveHabitat(habitat)).thenReturn(habitat);

        // WHEN
        AddAcceptableIdResult result = addAcceptableIdActivity.handleRequest(request);

        // THEN
        assertEquals(5, result.getAcceptableEnrichmentIds().size());
        assertEquals(expectedId, result.getAcceptableEnrichmentIds().get(4));
    }

    @Test
    public void handleRequest_duplicateId_throwsDuplicateAcceptableIdException() {
        // GIVEN
        // generated habitat with 4 acceptable ids present
        Habitat habitat = HabitatTestHelper.generateHabitat();
        String expectedId = "01";

        AddAcceptableIdRequest request = AddAcceptableIdRequest.builder()
                .withIdToAdd(expectedId)
                .withHabitatId(habitat.getHabitatId())
                .withKeeperManagerId(habitat.getKeeperManagerId())
                .build();

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);

        // WHEN && THEN
        assertThrows(DuplicateAcceptableIdException.class, () -> addAcceptableIdActivity.handleRequest(request));
    }
}
