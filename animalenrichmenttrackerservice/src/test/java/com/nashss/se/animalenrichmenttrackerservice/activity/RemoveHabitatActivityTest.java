package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.RemoveHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.RemoveHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.helper.HabitatTestHelper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RemoveHabitatActivityTest {
    @Mock
    private HabitatDao habitatDao;
    private RemoveHabitatActivity removeHabitatActivity;

    @BeforeEach
    public void setup() {
        initMocks(this);
        removeHabitatActivity = new RemoveHabitatActivity(habitatDao);
    }

    @Test
    public void handleRequest_savedHabitatFound_returnsRemovedHabitatModelInResult() {
        // GIVEN
        Habitat expHabitat = HabitatTestHelper.generateHabitatWithNEnrichments(3);

        when(habitatDao.getHabitat(expHabitat.getHabitatId())).thenReturn(expHabitat);
        when(habitatDao.removeHabitat(expHabitat.getHabitatId())).thenReturn(expHabitat);

        RemoveHabitatRequest request = RemoveHabitatRequest.builder()
                .withHabitatId(expHabitat.getHabitatId())
                .withKeeperManagerId(expHabitat.getKeeperManagerId())
                .build();

        // WHEN
        RemoveHabitatResult result = removeHabitatActivity.handleRequest(request);

        // THEN
        assertEquals(expHabitat.getHabitatId(), result.getHabitat().getHabitatId());
        assertEquals(expHabitat.getHabitatName(), result.getHabitat().getHabitatName());
        assertEquals(expHabitat.getKeeperManagerId(), result.getHabitat().getKeeperManagerId());
        assertEquals(expHabitat.getTotalAnimals(), result.getHabitat().getTotalAnimals());
    }

    @Test
    public void handleRequest_removedHabitat_removedFromHabitats() {
        //GIVEN
        Habitat removedHabitat = HabitatTestHelper.generateHabitatWithNEnrichments(3);

        when(habitatDao.getHabitat(removedHabitat.getHabitatId())).thenReturn(removedHabitat);
        when(habitatDao.removeHabitat(removedHabitat.getHabitatId())).thenReturn(removedHabitat);

        RemoveHabitatRequest request = RemoveHabitatRequest.builder()
                .withHabitatId(removedHabitat.getHabitatId())
                .withKeeperManagerId(removedHabitat.getKeeperManagerId())
                .build();

        //WHEN
        RemoveHabitatResult result = removeHabitatActivity.handleRequest(request);

        List<Habitat> keepersHabitats = habitatDao.getAllHabitatsForKeeper(removedHabitat.getKeeperManagerId());

        //THEN
        assertEquals(0, keepersHabitats.size());
    }
}
