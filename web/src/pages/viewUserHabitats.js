import AnimalEnrichmentTrackerClient from '../api/animalEnrichmentTrackerClient';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
 * Logic needed for the view user habitats page of the website.
 */
  class ViewUserHabitats extends BindingClass {
     constructor() {
             super();
             this.bindClassMethods(['clientLoaded', 'mount', 'addHabitatsToPage', 'removeHabitat',
             'redirectToUpdateHabitat', 'checkLoginStatus'], this);
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
      document.getElementById('habitats').addEventListener("click", this.removeHabitat);
      document.getElementById('habitats').addEventListener("click", this.redirectToUpdateHabitat);

      this.client = new AnimalEnrichmentTrackerClient();
      this.checkLoginStatus();
  }

  /**
  * Check user login status.
  */
  async checkLoginStatus() {
        const user = await this.client.getIdentity();
        const userHabitatsSection = document.querySelector('.card.hidden');

        if (user) {
            userHabitatsSection.classList.remove('hidden');
            this.clientLoaded();
        } else {
            userHabitatsSection.classList.add('hidden');
        }
  }

  /**
   * When the habitat is updated in the datastore, update the habitat metadata on the page.
   */
   addHabitatsToPage() {
        const habitats = this.dataStore.get('habitats');

        if (habitats == null) {
            return;
        }

        let habitatsHtml = '<table id="habitats-table"><tr><th>Name</th><th>Total Animals</th><th>Species</th><th>Habitat Id</th><th>Update Habitat</th><th>Delete Habitat</th></tr>';
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
                <td><button data-id="${habitat.habitatId}" class="button update-habitat">Update</button></td>
                <td><button data-id="${habitat.habitatId}" class="button remove-habitat">Delete</button></td>
            </tr>`;
        }

        document.getElementById('habitats').innerHTML = habitatsHtml;

        document.getElementById('habitat-owner').innerText = habitat.keeperManagerId;
   }

   /**
    * when remove button is clicked, removes habitat.
    */
    async removeHabitat(e) {
        const removeButton = e.target;
        if (!removeButton.classList.contains('remove-habitat')) {
             return;
        }

        removeButton.innerText = "Removing...";

        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        await this.client.removeHabitat(removeButton.dataset.id, (error) => {
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
        });

        document.getElementById(removeButton.dataset.id).remove();
    }

    /**
    * when the update button is clicked, redirects to update habitat page.
    */
    async redirectToUpdateHabitat(e) {
        const updateButton = e.target;
        if (!updateButton.classList.contains("update-habitat")) {
            return;
        }

        updateButton.innerText = "Loading...";

        if (updateButton != null) {
            window.location.href = `/updateHabitat.html?habitatId=${updateButton.dataset.id}`;
        }
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