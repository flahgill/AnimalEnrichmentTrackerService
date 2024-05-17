package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Enrichment;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.helper.EnrichmentTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ViewHabitatActivityTest {
    @Mock
    private HabitatDao habitatDao;
    private ViewHabitatActivity viewHabitatActivity;

    @BeforeEach
    public void setup() {
        initMocks(this);
        viewHabitatActivity = new ViewHabitatActivity(habitatDao);
    }

    @Test
    public void handleRequest_savedHabitatFound_returnsHabitatModelInResult() {
        // GIVEN
        String expectedId = "expectedId";
        String expectedName = "expectedName";
        String expectedKeeperId = "expectedCustomerId";
        int expectedTotalAnimals = 0;
        List<String> expectedSpecies = List.of("Giraffe");
        List<String> expectedAnimals = List.of("G1", "G2");
        List<String> expAccEnrichments = List.of("01", "02");
        List<Enrichment> expCompletedEnrichments = List.of(EnrichmentTestHelper.generateEnrichment(1));

        Habitat habitat = new Habitat();
        habitat.setHabitatId(expectedId);
        habitat.setHabitatName(expectedName);
        habitat.setKeeperManagerId(expectedKeeperId);
        habitat.setTotalAnimals(expectedTotalAnimals);
        habitat.setSpecies(expectedSpecies);
        habitat.setAnimalsInHabitat(expectedAnimals);
        habitat.setAcceptableEnrichmentIds(expAccEnrichments);
        habitat.setCompletedEnrichments(expCompletedEnrichments);

        when(habitatDao.getHabitat(expectedId)).thenReturn(habitat);

        ViewHabitatRequest request = ViewHabitatRequest.builder()
                .withHabitatId(expectedId)
                .build();

        // WHEN
        ViewHabitatResult result = viewHabitatActivity.handleRequest(request);

        // THEN
        assertEquals(expectedId, result.getHabitat().getHabitatId());
        assertEquals(expectedName, result.getHabitat().getHabitatName());
        assertEquals(expectedKeeperId, result.getHabitat().getKeeperManagerId());
        assertEquals(expectedTotalAnimals, result.getHabitat().getTotalAnimals());
        assertEquals(expectedSpecies, result.getHabitat().getSpecies());
        assertEquals(expectedAnimals, result.getHabitat().getAnimalsInHabitat());
        assertEquals(expAccEnrichments, result.getHabitat().getAcceptableEnrichmentIds());
        assertEquals(expCompletedEnrichments, result.getHabitat().getCompletedEnrichments());
    }
}
