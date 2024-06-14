package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.RemoveSpeciesRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.RemoveSpeciesResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.SpeciesNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.helper.HabitatTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RemoveSpeciesActivityTest {
    @Mock
    private HabitatDao habitatDao;
    private RemoveSpeciesActivity removeSpeciesActivity;
    @BeforeEach
    public void setup() {
        initMocks(this);
        removeSpeciesActivity = new RemoveSpeciesActivity(habitatDao);
    }

    @Test
    public void handleRequest_speciesInHabitat_removesSpecies() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();
        habitat.setSpecies(List.of("01", "02"));
        String speciesToRemove = "01";

        RemoveSpeciesRequest request = RemoveSpeciesRequest.builder()
                .withSpeciesToRemove(speciesToRemove)
                .withHabitatId(habitat.getHabitatId())
                .withKeeperManagerId(habitat.getKeeperManagerId())
                .build();

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);
        when(habitatDao.saveHabitat(habitat)).thenReturn(habitat);

        // WHEN
        RemoveSpeciesResult result = removeSpeciesActivity.handleRequest(request);

        // THEN
        assertEquals(1, result.getSpeciesList().size());
        assertFalse(result.getSpeciesList().contains(speciesToRemove));
    }
    @Test
    public void handleRequest_speciesNotInHabitat_throwsSpeciesNotInHabitatException() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();
        habitat.setSpecies(List.of("01", "02"));
        String speciesToRemove = "03";

        RemoveSpeciesRequest request = RemoveSpeciesRequest.builder()
                .withSpeciesToRemove(speciesToRemove)
                .withHabitatId(habitat.getHabitatId())
                .withKeeperManagerId(habitat.getKeeperManagerId())
                .build();

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);

        // WHEN + THEN
        assertThrows(SpeciesNotFoundException.class, () -> removeSpeciesActivity.handleRequest(request));
    }
}
