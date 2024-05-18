import AnimalEnrichmentTrackerClient from '../api/animalEnrichmentTrackerClient';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
 * Logic needed for the view user habitats page of the website.
 */
  class ViewUserHabitats extends BindingClass {
     constructor() {
             super();
             this.bindClassMethods(['clientLoaded', 'mount', 'addHabitatsToPage'], this);
             this.dataStore = new DataStore();
             console.log("viewUserHabitats constructor");
     }

 /**
  * Once the client is loaded, get the habitat metadata and habitat list.
  */
 async clientLoaded() {
     const urlParams = new URLSearchParams(window.location.search);
     const keeperManagerId = urlParams.get('email');
     document.getElementById('habitats').innerText = "Loading Habitats ...";
     const habitats = await this.client.getUserHabitats(keeperManagerId);
     this.dataStore.set('habitats', habitats);
     this.addHabitatsToPage();
 }

 /**
  * Load the AnimalEnrichmentTrackerClient.
  */
  mount() {
      this.client = new AnimalEnrichmentTrackerClient();
      this.clientLoaded();
  }

  /**
   * When the habitat is updated in the datastore, update the habitat metadata on the page.
   */
   addHabitatsToPage() {
        const habitats = this.dataStore.get('habitats');

        if (habitats == null) {
            return;
        }

        let habitatsHtml = '<table id="habitats-table"><tr><th>Name</th><th>Total Animals</th><th>Species</th><th>Habitat Id</th></tr>';
        let habitat;
        for (habitat of habitats) {
            habitatsHtml += `
            <tr id= "${habitat.habitatId}">
                <td>
                    <a href="habitat.html?habitatId=${habitat.habitatId}">${habitat.habitatName}</a>
                </td>
                <td>${habitat.totalAnimals}</td>
                <td>${habitat.species?.join(', ')}</td>
                <td>${habitat.habitatId}</td>
            </tr>`;
        }

        document.getElementById('habitats').innerHTML = habitatsHtml;

        document.getElementById('habitat-owner').innerText = habitat.keeperManagerId;
    }
}

 /**
  * Main method to run when the page contents have loaded.
  */
  const main = async () => {
        const viewUserHabitats = new ViewUserHabitats();
        viewUserHabitats.mount();
  };

  window.addEventListener('DOMContentLoaded', main);