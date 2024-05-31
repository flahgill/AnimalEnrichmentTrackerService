package com.nashss.se.animalenrichmenttrackerservice.dependency;

import com.nashss.se.animalenrichmenttrackerservice.activity.AddAnimalToHabitatActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.AddEnrichmentToHabitatActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.AddHabitatActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.RemoveAnimalFromHabitatActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.RemoveHabitatActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.SearchHabitatsActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.UpdateHabitatActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.ViewAllHabitatsActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.ViewAnimalsForHabitatActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.ViewHabitatActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.ViewHabitatEnrichmentsActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.ViewUserHabitatsActivity;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {DaoModule.class, MetricsModule.class})
public interface ServiceComponent {

    /**
     * Provides the relevant activity.
     * @return AddHabitatActivity
     */
    AddHabitatActivity provideAddHabitatActivity();

    /**
     * Provides the relevant activity.
     * @return ViewHabitatActivity
     */
    ViewHabitatActivity provideViewHabitatActivity();

    /**
     * Provides the relevant activity.
     * @return ViewUserHabitatsActivity
     */
    ViewUserHabitatsActivity provideViewUserHabitatsActivity();

    /**
     * Provides the relevant activity.
     * @return RemoveHabitatActivity
     */
    RemoveHabitatActivity provideRemoveHabitatActivity();

    /**
     * Provides the relevant activity.
     * @return UpdateHabitatActivity
     */
    UpdateHabitatActivity provideUpdateHabitatActivity();

    /**
     * Provides the relevant activity.
     * @return ViewAnimalsForHabitatActivity
     */
    ViewAnimalsForHabitatActivity provideViewAnimalsForHabitatActivity();

    /**
     * Provides the relevant activity.
     * @return AddAnimalToHabitatActivity
     */
    AddAnimalToHabitatActivity provideAddAnimalToHabitatActivity();

    /**
     * Provides the relevant activity.
     * @return RemoveAnimalFromHabitatActivity
     */
    RemoveAnimalFromHabitatActivity provideRemoveAnimalFromHabitatActivity();

    /**
     * Provides the relevant activity.
     * @return SearchHabitatsActivity
     */
    SearchHabitatsActivity provideSearchHabitatsActivity();

    /**
     * Provides the relevant activity.
     * @return ViewAllHabitatsActivity
     */
    ViewAllHabitatsActivity provideViewAllHabitatsActivity();

    /**
     * Provides the relevant activity.
     * @return ViewHabitatEnrichmentsActivity
     */
    ViewHabitatEnrichmentsActivity provideViewHabitatEnrichmentsActivity();

    /**
     * Provides the relevant activity.
     * @return AddEnrichmentToHabitatActivity
     */
    AddEnrichmentToHabitatActivity provideAddEnrichmentToHabitatActivity();
}
