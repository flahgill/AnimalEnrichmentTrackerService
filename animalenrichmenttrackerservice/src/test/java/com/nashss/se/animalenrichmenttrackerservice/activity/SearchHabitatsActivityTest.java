package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.SearchHabitatsRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.SearchHabitatsResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.models.HabitatModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SearchHabitatsActivityTest {
    @Mock
    private HabitatDao habitatDao;
    private SearchHabitatsActivity searchHabitatsActivity;
    @BeforeEach
    public void setup() {
        initMocks(this);
        searchHabitatsActivity = new SearchHabitatsActivity(habitatDao);
    }

    @Test
    public void handleRequest_whenHabitatsMatchSearch_returnsHabitatModelListInResult() {
        // GIVEN
        String criteria = "Benny";
        String[] criteriaArray = {criteria};

        List<Habitat> expected = List.of(
                newHabitat("id1", "a good habitat", List.of("species1", "species2"), List.of("Betty", "Benny")),
                newHabitat("id2", "another good habitat", List.of("species1", "species2"), List.of("Betty", "Benny")));

        when(habitatDao.searchHabitats(criteriaArray)).thenReturn(expected);

        SearchHabitatsRequest request = SearchHabitatsRequest.builder()
                .withCriteria(criteria)
                .build();

        // WHEN
        SearchHabitatsResult result = searchHabitatsActivity.handleRequest(request);

        // THEN
        List<HabitatModel> resultHabitats = result.getHabitats();
        assertEquals(expected.size(), resultHabitats.size());

        for (int i=0; i<expected.size(); i++) {
            assertEquals(expected.get(i).getHabitatId(), resultHabitats.get(i).getHabitatId());
            assertEquals(expected.get(i).getHabitatName(), resultHabitats.get(i).getHabitatName());
        }
    }

    @Test
    public void handleRequest_withNullCriteria_isIdenticalToEmptyCriteria() {
        // GIVEN
        String criteria = null;
        ArgumentCaptor<String[]> criteriaArray = ArgumentCaptor.forClass(String[].class);

        when(habitatDao.searchHabitats(criteriaArray.capture())).thenReturn(List.of());

        SearchHabitatsRequest request = SearchHabitatsRequest.builder()
                .withCriteria(criteria)
                .build();

        // WHEN
        SearchHabitatsResult result = searchHabitatsActivity.handleRequest(request);

        // THEN
        assertEquals(0, criteriaArray.getValue().length, "Criteria Array should be empty");
    }

    private static Habitat newHabitat(String habitatId, String habitatName, List<String> species, List<String> animals) {
        Habitat habitat = new Habitat();

        habitat.setHabitatId(habitatId);
        habitat.setHabitatName(habitatName);
        habitat.setSpecies(species);
        habitat.setAnimalsInHabitat(animals);

        // the test code doesn't need these properties to be distinct.
        habitat.setKeeperManagerId("a customer id");
        habitat.setTotalAnimals(0);

        return habitat;
    }
}
