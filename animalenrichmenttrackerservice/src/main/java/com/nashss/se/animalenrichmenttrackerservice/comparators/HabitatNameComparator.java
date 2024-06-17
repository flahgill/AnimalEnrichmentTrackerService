package com.nashss.se.animalenrichmenttrackerservice.comparators;

import com.nashss.se.animalenrichmenttrackerservice.models.HabitatModel;

import java.util.Comparator;

public class HabitatNameComparator implements Comparator<HabitatModel> {
    @Override
    public int compare(HabitatModel h1, HabitatModel h2) {
        String h1Name = h1.getHabitatName();
        String h2Name = h2.getHabitatName();

        return h1Name.compareTo(h2Name);
    }
}
