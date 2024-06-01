package com.nashss.se.animalenrichmenttrackerservice.comparators;

import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentActivityModel;

import java.time.LocalDate;
import java.util.Comparator;

public class EnrichmentActivityDateComparator implements Comparator<EnrichmentActivityModel> {

    @Override
    public int compare(EnrichmentActivityModel e1, EnrichmentActivityModel e2) {
        LocalDate e1Date = e1.getDateCompleted();
        LocalDate e2Date = e2.getDateCompleted();

        return e1Date.compareTo(e2Date);
    }
}
