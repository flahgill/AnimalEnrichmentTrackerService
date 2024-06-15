import AnimalEnrichmentTrackerClient from '../api/animalEnrichmentTrackerClient';
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
 * Logic needed for the view a habitat's enrichment activities page of the website.
 */
export default class ViewHabitatEnrichments extends BindingClass {
    constructor(client) {
        super();
        this.client = client;

        this.bindClassMethods(['clientLoaded', 'mount', 'addEnrichmentsToPage', 'addEnrichment', 'removeEnrichment',
            'redirectToUpdateActivity'], this);
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.addEnrichmentsToPage);
        this.header = new Header(this.dataStore);
        console.log("viewHabitatEnrichments constructor");
    }

    /**
     * Once the client is loaded, get the habitat metadata and enrichments list.
     */
    async clientLoaded() {
        const urlParams = new URLSearchParams(window.location.search);
        const habitatId = urlParams.get('habitatId');
        document.getElementById('habitat-name').innerText = "Loading Habitat ...";
        const habitat = await this.client.getHabitat(habitatId);
        this.dataStore.set('habitat', habitat);
        document.getElementById('habitat-enrichments').innerText = "(loading enrichments...)";
        const completedEnrich = await this.client.getHabitatEnrichments(habitatId);
        this.dataStore.set('completed-enrichments', completedEnrich);
    }

    /**
     * Add the header to the page and load the AnimalEnrichmentTrackerClient.
     */
    mount() {
        document.getElementById('add-enrichment').addEventListener('click', this.addEnrichment);
        document.getElementById('habitat-enrichments').addEventListener('click', this.removeEnrichment);
        document.getElementById('habitat-enrichments').addEventListener('click', this.redirectToUpdateActivity);
        this.header.addHeaderToPage();

        this.clientLoaded();

        document.getElementById('ok-button').addEventListener("click", this.closeModal);
    }

    /**
     * When the list of enrichments activities are updated in the datastore, update the enrichments metadata on the page.
     */
    addEnrichmentsToPage() {
        const completedEnrich = this.dataStore.get('completed-enrichments');
        const habitat = this.dataStore.get('habitat');
        if (completedEnrich == null || habitat == null) {
            return;
        }

        document.getElementById('habitat-name').innerText = habitat.habitatName;
        document.getElementById('habitat-owner').innerText = habitat.keeperName;
        document.getElementById('owner-email').innerText = habitat.keeperManagerId;
        document.getElementById('habitat-id').innerText = habitat.habitatId;

        let speciesHtml = '';
        for (const spec of habitat.species) {
            speciesHtml += `<div class="species">${spec}</div>`;
        }
        document.getElementById('species').innerHTML = speciesHtml;

        let acceptEnrichIdHtml = '';
        for (const id of habitat.acceptableEnrichmentIds) {
            acceptEnrichIdHtml += `<div class="accept-ids">${id}</div>`;
        }
        document.getElementById('acceptable-enrichment-ids').innerHTML = 'Acceptable Enrichment Ids:' + acceptEnrichIdHtml;

        let enrichHtml = '<table id="enrichments-table"><tr><th>Date Completed</th><th>Activity</th><th>Activity ID</th><th>Description</th><th>Rating</th><th>Completed</th><th>Update Activity</th><th>Remove From Habitat</th></tr>';
        for (const enrich of completedEnrich) {
            enrichHtml += `
               <tr id="${enrich.activityId + enrich.habitatId}">
                   <td>${enrich.dateCompleted}</td>
                   <td>
                       <a href="enrichmentActivity.html?activityId=${enrich.activityId}">${enrich.activityName}</a>
                   </td>
                   <td>${enrich.activityId}</td>
                   <td>${enrich.description}</td>
                   <td>${enrich.keeperRating}</td>
                   <td>${enrich.isComplete}</td>
                   <td><button data-activity-id="${enrich.activityId}"  data-habitat-id="${enrich.habitatId}" class ="button update-enrich">Update</button></td>
                   <td><button data-activity-id="${enrich.activityId}"  data-habitat-id="${enrich.habitatId}" class ="button remove-enrich">Remove</button></td>
               </tr>`;
        }

        enrichHtml += '</table>';
        document.getElementById('habitat-enrichments').innerHTML = enrichHtml;

        const enrichIdsSelect = document.getElementById('accept-enrichment-id');
        enrichIdsSelect.innerHTML = '';

        const uniqueSortedIds = Array.from(new Set(habitat.acceptableEnrichmentIds)).sort();
        for (const id of uniqueSortedIds) {
            const option = document.createElement('option');
            option.value = id;
            option.textContent = id;
            enrichIdsSelect.appendChild(option);
        }
    }

    /**
    * Method to run when the add enrichment submit button is clicked. Call the AnimalEnrichmentTrackerService to add
    * the enrichment to the habitat.
    */
    async addEnrichment() {
        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        const habitat = this.dataStore.get('habitat');
        if (habitat == null) {
            return;
        }

        document.getElementById('add-enrichment').innerText = 'Adding...';
        const habitatId = habitat.habitatId;
        const enrichId = document.getElementById('accept-enrichment-id').value;
        const keeperRating = document.getElementById('keeper-rating').value;
        const dateCompleted = document.getElementById('date-completed').value;
        const isComplete = document.getElementById('is-complete').value;

        let errorOccurred = false;
        const completedEnrich = await this.client.addEnrichmentToHabitat(habitatId, enrichId, keeperRating, dateCompleted, isComplete, (error) => {
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
            errorOccurred = true;
            this.showErrorModal(error.message);
        });

        if (!errorOccurred) {
            this.dataStore.set('completed-enrichments', completedEnrich);

            document.getElementById('add-enrichment').innerText = 'Add';
            document.getElementById('add-enrichment-form').reset();
            this.addEnrichmentsToPage();
        }
    }

    /**
    * Method to run when the remove enrichment button is clicked. Call the AnimalEnrichmentTrackerService to remove
    * the enrichment from the habitat.
    */
    async removeEnrichment(e) {
        const removeButton = e.target;
        if (!removeButton.classList.contains("remove-enrich")) {
            return;
        }

        removeButton.innerText = "Removing...";

        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        let errorOccurred = false;
        await this.client.removeEnrichmentActivityFromHabitat(removeButton.dataset.habitatId, removeButton.dataset.activityId, (error) => {
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
            errorOccurred = true;
            this.showErrorModal(error.message);
            removeButton.innerText = "Remove";
        });

        if (!errorOccurred) {
            document.getElementById(removeButton.dataset.activityId + removeButton.dataset.habitatId).remove();
        }
    }

    /**
    * When the update button is clicked, redirects to update activity page.
    */
    async redirectToUpdateActivity(e) {
        const updateButton = e.target;
        if (!updateButton.classList.contains("update-enrich")) {
            return;
        }

        updateButton.innerText = "Loading...";

        if (updateButton != null) {
            window.location.href = `/updateHabitatEnrichment.html?habitatId=${updateButton.dataset.habitatId}&activityId=${updateButton.dataset.activityId}`;
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
