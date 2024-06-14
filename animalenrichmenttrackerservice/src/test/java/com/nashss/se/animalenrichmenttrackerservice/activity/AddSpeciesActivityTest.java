package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.AddSpeciesRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.AddSpeciesResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.DuplicateSpeciesException;
import com.nashss.se.animalenrichmenttrackerservice.helper.HabitatTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AddSpeciesActivityTest {
    @Mock
    private HabitatDao habitatDao;
    private AddSpeciesActivity addSpeciesActivity;
    @BeforeEach
    public void setup() {
        initMocks(this);
        addSpeciesActivity = new AddSpeciesActivity(habitatDao);
    }

    @Test
    public void handleRequest_NoOtherSpeciesInHabitat_addsSpeciesToHabitat() {
        // GIVEN
        Habitat habitat = HabitatTestHelper.generateHabitat();
        habitat.setSpecies(new ArrayList<>());
        String expectedSpecies = "test";

        AddSpeciesRequest request = AddSpeciesRequest.builder()
                .withSpeciesToAdd(expectedSpecies)
                .withHabitatId(habitat.getHabitatId())
                .withKeeperManagerId(habitat.getKeeperManagerId())
                .build();

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);
        when(habitatDao.saveHabitat(habitat)).thenReturn(habitat);

        // WHEN
        AddSpeciesResult result = addSpeciesActivity.handleRequest(request);

        // THEN
        assertEquals(1, result.getSpeciesList().size());
        assertEquals(expectedSpecies, result.getSpeciesList().get(0));
    }

    @Test
    public void handleRequest_OtherSpeciesInHabitat_addsSpeciesToHabitat() {
        // GIVEN
        // generated habitat with 3 species present
        Habitat habitat = HabitatTestHelper.generateHabitat();
        String expectedSpecies = "Z";

        AddSpeciesRequest request = AddSpeciesRequest.builder()
                .withSpeciesToAdd(expectedSpecies)
                .withHabitatId(habitat.getHabitatId())
                .withKeeperManagerId(habitat.getKeeperManagerId())
                .build();

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);
        when(habitatDao.saveHabitat(habitat)).thenReturn(habitat);

        // WHEN
        AddSpeciesResult result = addSpeciesActivity.handleRequest(request);

        // THEN
        assertEquals(4, result.getSpeciesList().size());
        assertEquals(expectedSpecies, result.getSpeciesList().get(3));
    }

    @Test
    public void handleRequest_duplicateSpecies_throwsDuplicateSpeciesException() {
        // GIVEN
        // generated habitat with 3 species present
        Habitat habitat = HabitatTestHelper.generateHabitat();
        String expectedSpecies = "Giraffe";

        AddSpeciesRequest request = AddSpeciesRequest.builder()
                .withSpeciesToAdd(expectedSpecies)
                .withHabitatId(habitat.getHabitatId())
                .withKeeperManagerId(habitat.getKeeperManagerId())
                .build();

        when(habitatDao.getHabitat(habitat.getHabitatId())).thenReturn(habitat);

        // WHEN + THEN
        assertThrows(DuplicateSpeciesException.class, () -> addSpeciesActivity.handleRequest(request));
    }
}
