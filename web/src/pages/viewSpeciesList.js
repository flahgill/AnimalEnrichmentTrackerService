import AnimalEnrichmentTrackerClient from '../api/animalEnrichmentTrackerClient';
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
 * Logic needed for the view animals page of the website.
 */
class ViewSpeciesList extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['clientLoaded', 'mount', 'addSpeciesToPage', 'addSpecies', 'removeSpecies'], this);
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.addSpeciesToPage);
        this.header = new Header(this.dataStore);
        console.log("viewSpeciesList constructor");
    }

    /**
     * Once the client is loaded, get the habitat metadata and animals list.
     */
    async clientLoaded() {
        const urlParams = new URLSearchParams(window.location.search);
        const habitatId = urlParams.get('habitatId');
        document.getElementById('habitat-name').innerText = "Loading Habitat ...";
        const habitat = await this.client.getHabitat(habitatId);
        this.dataStore.set('habitat', habitat);
        document.getElementById('species-list').innerText = "(loading species...)"
        const speciesList = await this.client.getSpeciesList(habitatId);
        this.dataStore.set('species', speciesList);
    }

    /**
     * Add the header to the page and load the AnimalEnrichmentTrackerClient.
     */
    mount() {
        document.getElementById('add-species-btn').addEventListener('click', this.addSpecies);
        document.getElementById('species-list').addEventListener('click', this.removeSpecies);

        this.header.addHeaderToPage();

        this.client = new AnimalEnrichmentTrackerClient();
        this.clientLoaded();

        document.getElementById('ok-button').addEventListener("click", this.closeModal);

    }

    /**
     * When the list of animals are updated in the datastore, update the animals metadata on the page.
     */
    addSpeciesToPage() {
        const speciesList = this.dataStore.get('species');
        const habitat = this.dataStore.get('habitat');
        if (speciesList == null || habitat == null) {
            return;
        }

        document.getElementById('habitat-name').innerText = habitat.habitatName;
        document.getElementById('habitat-owner').innerText = habitat.keeperName;
        document.getElementById('owner-email').innerText = habitat.keeperManagerId;
        document.getElementById('habitat-id').innerText = habitat.habitatId;

        let speciesHtml = '<table id="species-table"><tr><th></th><th>Remove</th></tr>';
        let spec;
        for (spec of speciesList) {
            speciesHtml += `
               <tr id="spec-${spec}">
                   <td>${spec}</td>
                   <td><button data-spec="${spec}" class="button remove-species">Remove</button></td>
               </tr>`;
        }

        speciesHtml += '</table>';
        document.getElementById('species-list').innerHTML = speciesHtml;
    }

    /*
    * method to run when the add id submit button is pressed. Call the AnimalEnrichmentTrackerClient to add
    * an id to a habitat's list of acceptable enrichment ids.
    */
    async addSpecies() {
        const errorMessageDisplay = document.getElementById('add-species-error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        const habitat = this.dataStore.get('habitat');
        if (habitat == null) {
            return;
        }

        document.getElementById('add-species-btn').innerText = 'Adding...';
        const specToAdd = document.getElementById('add-species').value;
        const habitatId = habitat.habitatId;

        const speciesList = await this.client.addSpecies(habitatId, specToAdd, (error) => {
           errorMessageDisplay.innerText = `Error: ${error.message}`;
           errorMessageDisplay.classList.remove('hidden');
           this.showErrorModal(error.message);
        });

        this.dataStore.set('species', speciesList);
        document.getElementById('add-species-btn').innerText = 'Add';

        if (speciesList.includes(specToAdd)) {
            location.reload();
        }
    }

    /*
    * method to run when the remove id button is clicked. Call the AnimalEnrichmentTrackerClient to remove
    * an id from a habitat's list of acceptable enrichment ids.
    */
    async removeSpecies(e) {
        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        const habitat = this.dataStore.get('habitat');
        if (habitat == null) {
            return;
        }

        const removeButton = e.target;
        if (!removeButton.classList.contains("remove-species")) {
            return;
        }

        removeButton.innerText = "Removing...";
        const habitatId = habitat.habitatId;

        const specToRemove = removeButton.dataset.spec;

        const speciesList = await this.client.removeSpecies(habitatId, specToRemove, (error) => {
           errorMessageDisplay.innerText = `Error: ${error.message}`;
           errorMessageDisplay.classList.remove('hidden');
           this.showErrorModal(error.message);
        });

        this.dataStore.set('species', speciesList);
        removeButton.innerText = "Remove";
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

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const viewSpeciesList = new ViewSpeciesList();
    viewSpeciesList.mount();
};

window.addEventListener('DOMContentLoaded', main);