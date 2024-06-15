import AnimalEnrichmentTrackerClient from '../api/animalEnrichmentTrackerClient';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
* Logic needed for the view user habitats page of the website.
*/
export default class ViewUserHabitats extends BindingClass {
     constructor(client) {
         super();
         this.client = client;

         this.bindClassMethods(['clientLoaded', 'mount', 'addHabitatsToPage',
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
       async mount() {
          document.getElementById('habitats').addEventListener("click", this.redirectToUpdateHabitat);

          await this.checkLoginStatus();

          document.getElementById('ok-button').addEventListener("click", this.closeModal);
       }

      /**
      * Check user login status.
      */
      async checkLoginStatus() {
            const user = await this.client.getIdentity();
            const userHabitatsSection = document.querySelector('.card.hidden');

            if (user) {
                userHabitatsSection.classList.remove('hidden');
                await this.clientLoaded();
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

            let habitatsHtml = '<table id="habitats-table"><tr><th>Name</th><th>Habitat ID</th><th>Species</th><th>Total Animals</th><th>Animals</th><th>Update Habitat</th></tr>';
            let habitat;
            for (habitat of habitats) {
                habitatsHtml += `
                <tr id="${habitat.habitatId}">
                    <td>
                        <a href="habitat.html?habitatId=${habitat.habitatId}">${habitat.habitatName}</a>
                    </td>
                    <td>${habitat.habitatId}</td>
                    <td>${habitat.species?.join(', ')}</td>
                    <td>${habitat.totalAnimals}</td>
                    <td>${habitat.animalsInHabitat?.join(', ')}</td>
                    <td><button data-id="${habitat.habitatId}" class="button update-habitat">Update</button></td>
                </tr>`;
            }

            document.getElementById('habitats').innerHTML = habitatsHtml;

            document.getElementById('habitat-owner').innerText = habitat.keeperName;
            document.getElementById('owner-email').innerText = habitat.keeperManagerId;
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

        async showErrorModal(message) {
            const modal = document.getElementById('error-modal');
            const modalMessage = document.getElementById('error-modal-message');
            modalMessage.innerText = message;
            modal.style.display = "block";
        }

        async closeModal() {
            const modal = document.getElementById('error-modal');
            modal.style.display = "none";
        }
}