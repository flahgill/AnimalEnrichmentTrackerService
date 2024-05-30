package com.nashss.se.animalenrichmenttrackerservice.comparators;

import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentModel;

import java.time.LocalDate;
import java.util.Comparator;

public class EnrichmentDateComparator implements Comparator<EnrichmentModel> {

    @Override
    public int compare(EnrichmentModel e1, EnrichmentModel e2) {
        LocalDate e1Date = e1.getDateCompleted();
        LocalDate e2Date = e2.getDateCompleted();

        return e1Date.compareTo(e2Date);
    }
}
