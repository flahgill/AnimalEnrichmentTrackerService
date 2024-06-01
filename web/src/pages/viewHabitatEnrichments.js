import AnimalEnrichmentTrackerClient from '../api/animalEnrichmentTrackerClient';
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
 * Logic needed for the view a habitat's enrichment activites page of the website.
 */
class ViewHabitatEnrichments extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['clientLoaded', 'mount', 'addEnrichmentsToPage', 'addEnrichment'], this);
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
        document.getElementById('habitat-enrichments').innerText = "(loading enrichments...)"
        const completedEnrich = await this.client.getHabitatEnrichments(habitatId);
        this.dataStore.set('completed-enrichments', completedEnrich);
        console.log('Completed Enrichments:', completedEnrich);
    }

    /**
     * Add the header to the page and load the AnimalEnrichmentTrackerClient.
     */
    mount() {
        document.getElementById('add-enrichment').addEventListener('click', this.addEnrichment);
        this.header.addHeaderToPage();

        this.client = new AnimalEnrichmentTrackerClient();
        this.clientLoaded();
    }

    /**
     * When the list of enrichments activites are updated in the datastore, update the enrichments metadata on the page.
     */
    addEnrichmentsToPage() {
        const completedEnrich = this.dataStore.get('completed-enrichments');
        const habitat = this.dataStore.get('habitat');
        if (completedEnrich == null || habitat == null) {
            return;
        }

        document.getElementById('habitat-name').innerText = habitat.habitatName;
        document.getElementById('habitat-owner').innerText = habitat.keeperManagerId;
        document.getElementById('habitat-id').innerText = habitat.habitatId;

        let speciesHtml = '';
        let spec;
        for (spec of habitat.species) {
            speciesHtml += `<div class="species">${spec}</div>`;
        }
        document.getElementById('species').innerHTML = speciesHtml;

        let acceptEnrichIdHtml = '';
        let id;
        for (id of habitat.acceptableEnrichmentIds) {
            acceptEnrichIdHtml += `<div class="accept-ids">${id}</div>`;
        }
        document.getElementById('acceptable-enrichment-ids').innerHTML = 'Acceptable Enrichment Ids:' + acceptEnrichIdHtml;

        let enrichHtml = '<table id="enrichments-table"><tr><th>Date Completed</th><th>Activity</th><th>Description</th><th>Enrichment Id</th><th>Rating</th><th>Activity Id</th></tr>';
        let enrich;
        for (enrich of completedEnrich) {
            enrichHtml += `
               <tr>
                   <td>${enrich.dateCompleted}</td>
                   <td>${enrich.name}</td>
                   <td>${enrich.description}</td>
                   <td>${enrich.enrichmentId}</td>
                   <td>${enrich.keeperRating}</td>
                   <td>${enrich.activityId}</td>
               </tr>`;
        }

        enrichHtml += '</table>';
        document.getElementById('habitat-enrichments').innerHTML = enrichHtml;
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
        const enrichId = document.getElementById('enrichment-id').value;
        const keeperRating = document.getElementById('keeper-rating').value;
        const dateCompleted = document.getElementById('date-completed').value;

        const completedEnrich = await this.client.addEnrichmentToHabitat(habitatId, enrichId, keeperRating,
        dateCompleted, (error) => {
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
        });

        this.dataStore.set('completed-enrichments', completedEnrich);

        document.getElementById('add-enrichment').innerText = 'Add';
        document.getElementById('add-enrichment-form').reset();
    }

}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const viewHabitatEnrichments = new ViewHabitatEnrichments();
    viewHabitatEnrichments.mount();
};

window.addEventListener('DOMContentLoaded', main);