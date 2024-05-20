package com.nashss.se.animalenrichmenttrackerservice.dependency;
//CHECKSTYLE:OFF

import com.nashss.se.animalenrichmenttrackerservice.activity.AddHabitatActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.RemoveHabitatActivity;
import com.nashss.se.animalenrichmenttrackerservice.activity.ViewHabitatActivity;
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
}
