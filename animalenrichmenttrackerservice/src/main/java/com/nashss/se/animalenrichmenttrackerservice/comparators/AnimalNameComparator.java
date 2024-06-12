package com.nashss.se.animalenrichmenttrackerservice.comparators;

import com.nashss.se.animalenrichmenttrackerservice.models.AnimalModel;

import java.util.Comparator;

public class AnimalNameComparator implements Comparator<AnimalModel> {

    @Override
    public int compare(AnimalModel a1, AnimalModel a2) {
        String a1Name = a1.getAnimalName();
        String a2Name = a2.getAnimalName();

        return a1Name.compareTo(a2Name);
    }
}
