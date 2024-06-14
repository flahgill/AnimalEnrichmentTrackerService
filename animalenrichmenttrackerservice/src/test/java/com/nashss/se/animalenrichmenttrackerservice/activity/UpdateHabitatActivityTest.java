package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.UpdateHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.UpdateHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.HabitatNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.UserSecurityException;
import com.nashss.se.animalenrichmenttrackerservice.metrics.MetricsConstants;
import com.nashss.se.animalenrichmenttrackerservice.metrics.MetricsPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UpdateHabitatActivityTest {

    @Mock
    private HabitatDao habitatDao;

    @Mock
    private MetricsPublisher metricsPublisher;

    private UpdateHabitatActivity updateHabitatActivity;

    @BeforeEach
    public void setup() {
        initMocks(this);
        updateHabitatActivity = new UpdateHabitatActivity(habitatDao, metricsPublisher);
    }

    @Test
    public void handleRequest_goodNameRequest_updatesHabitat() {
        // GIVEN
        String id = "id";
        String expectedKeeperId = "expectedKeeperId";
        String expectedName = "new name";
        int expectedAnimalCount = 5;

        UpdateHabitatRequest request = UpdateHabitatRequest.builder()
                .withHabitatId(id)
                .withKeeperManagerId(expectedKeeperId)
                .withHabitatName(expectedName)
                .build();

        Habitat startingHabitat = new Habitat();
        startingHabitat.setKeeperManagerId(expectedKeeperId);
        startingHabitat.setHabitatName("old name");
        startingHabitat.setTotalAnimals(expectedAnimalCount);

        when(habitatDao.getHabitat(id)).thenReturn(startingHabitat);
        when(habitatDao.saveHabitat(startingHabitat)).thenReturn(startingHabitat);

        // WHEN
        UpdateHabitatResult result = updateHabitatActivity.handleRequest(request);

        // THEN
        assertEquals(expectedName, result.getHabitat().getHabitatName());
        assertEquals(expectedKeeperId, result.getHabitat().getKeeperManagerId());
        assertEquals(expectedAnimalCount, result.getHabitat().getTotalAnimals());
    }

    @Test
    public void handleRequest_habitatDoesNotExist_throwsHabitatNotFoundException() {
        // GIVEN
        String id = "id";
        UpdateHabitatRequest request = UpdateHabitatRequest.builder()
                .withHabitatId(id)
                .withHabitatName("name")
                .withKeeperManagerId("customerId")
                .build();

        when(habitatDao.getHabitat(id)).thenThrow(new HabitatNotFoundException());

        // THEN
        assertThrows(HabitatNotFoundException.class, () -> updateHabitatActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_keeperIdNotMatch_throwsUserSecurityException() {
        // GIVEN
        String id = "id";
        UpdateHabitatRequest request = UpdateHabitatRequest.builder()
                .withHabitatId(id)
                .withHabitatName("name")
                .withKeeperManagerId("customerId")
                .build();

        Habitat differentCustomerIdHabitat = new Habitat();
        differentCustomerIdHabitat.setKeeperManagerId("different");

        when(habitatDao.getHabitat(id)).thenReturn(differentCustomerIdHabitat);

        // WHEN + THEN
        assertThrows(UserSecurityException.class, () -> updateHabitatActivity.handleRequest(request));
        verify(metricsPublisher).addCount(MetricsConstants.UPDATEHABITAT_USERSECURITYEXCEPTION, 1);
    }
}
