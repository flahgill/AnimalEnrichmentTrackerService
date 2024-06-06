package com.nashss.se.animalenrichmenttrackerservice.dependency;

import com.nashss.se.animalenrichmenttrackerservice.activity.AddAnimalToHabitatActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.AddEnrichmentActivityToHabitatActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.AddHabitatActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.ReAddEnrichmentActivityToHabitatActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.RemoveAnimalFromHabitatActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.RemoveEnrichmentActivityActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.RemoveEnrichmentActivityFromHabitatActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.RemoveHabitatActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.SearchEnrichmentActivitiesActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.SearchEnrichmentsActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.SearchHabitatsActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.UpdateHabitatActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.UpdateHabitatEnrichmentActivityActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.ViewAllEnrichmentActivitiesActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.ViewAllHabitatsActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.ViewAnimalsForHabitatActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.ViewEnrichmentActivityActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.ViewHabitatActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.ViewHabitatEnrichmentActivitiesActivity;
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
     * @return ViewHabitatEnrichmentActivitiesActivity
     */
    ViewHabitatEnrichmentActivitiesActivity provideViewHabitatEnrichmentActivitiesActivity();

    /**
     * Provides the relevant activity.
     * @return AddEnrichmentToHabitatActivity
     */
    AddEnrichmentActivityToHabitatActivity provideAddEnrichmentToHabitatActivity();

    /**
     * Provides the relevant activity.
     * @return RemoveEnrichmentActivityFromHabitatActivity
     */
    RemoveEnrichmentActivityFromHabitatActivity provideRemoveEnrichmentActivityFromHabitatActivity();

    /**
     * Provides the relevant activity.
     * @return UpdateHabitatEnrichmentActivityActivity
     */
    UpdateHabitatEnrichmentActivityActivity provideUpdateHabitatEnrichmentActivityActivity();

    /**
     * Provides the relevant activity.
     * @return ViewEnrichmentActivityActivity
     */
    ViewEnrichmentActivityActivity provideViewEnrichmentActivityActivity();

    /**
     * Provides the relevant activity.
     * @return ViewAllEnrichmentActivitiesActivity
     */
    ViewAllEnrichmentActivitiesActivity provideViewAllEnrichmentActivitiesActivity();

    /**
     * Provides the relevant activity.
     * @return RemoveEnrichmentActivityActivity
     */
    RemoveEnrichmentActivityActivity provideRemoveEnrichmentActivityActivity();

    /**
     * Provides the relevant activity.
     * @return SearchEnrichmentActivitiesActivity
     */
    SearchEnrichmentActivitiesActivity provideSearchEnrichmentActivitiesActivity();

    /**
     * Provides the relevant activity.
     * @return SearchEnrichmentsActivity
     */
    SearchEnrichmentsActivity provideSearchEnrichmentsActivity();

    /**
     * Provides the relevant activity.
     * @return ReAddEnrichmentActivityToHabitatActivity
     */
    ReAddEnrichmentActivityToHabitatActivity provideReAddEnrichmentActivityToHabitatActivity();
}
