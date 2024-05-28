import AnimalEnrichmentTrackerClient from '../api/animalEnrichmentTrackerClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

/**
 * Logic needed for the update habitat page of the website.
 */
class UpdateHabitat extends BindingClass {

    urlParams = new URLSearchParams(window.location.search);
    habitatId = this.urlParams.get('habitatId');

    constructor() {
        super();
        this.bindClassMethods(['mount', 'clientLoaded', 'submit', 'addHabitatToPage', 'redirectToHabitat'], this);
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.addHabitatToPage);
        this.header = new Header(this.dataStore);
        console.log("updateHabitat constructor");
    }

    /**
     * Add the header to the page and load the AnimalEnrichmentTrackerClient.
     */
    mount() {
        document.getElementById('update-habitat').addEventListener('click', this.submit);

        this.header.addHeaderToPage();

        this.client = new AnimalEnrichmentTrackerClient();
        this.clientLoaded();
    }

    /**
     * Once the client is loaded, get the habitat metadata.
     */
    async clientLoaded() {
        document.getElementById('habitat-name').innerText = "Loading Habitat ...";

        const habitat = await this.client.getHabitat(this.habitatId);
        this.dataStore.set('habitat', habitat);
    }

    /**
     * When the habitat is updated in the datastore, update the habitat metadata on the page.
     */
    addHabitatToPage() {
        const habitat = this.dataStore.get('habitat');
        if (habitat == null) {
            return;
        }

        document.getElementById('habitat-name').innerText = habitat.habitatName;
        document.getElementById('habitat-owner').innerText = habitat.keeperManagerId;
        document.getElementById('habitat-id').innerText = habitat.habitatId;
        document.getElementById('species').innerText = habitat.species;
    }

    /**
     * Method to run when the update habitat submit button is pressed. Call the AnimalEnrichmentTrackerClient to update the
     * habitat.
     */
    async submit(evt) {
        evt.preventDefault();

        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        const updateButton = document.getElementById('update-habitat');
        const origButtonText = updateButton.innerText;
        updateButton.innerText = 'Loading...';

        const keeperId = document.getElementById('habitat-owner').value;
        const newName = document.getElementById('new-name').value;
        const newSpecies = document.getElementById('new-species').value;
        const newActive = document.getElementById('active-status').value;

        let species;
        if (newSpecies.length < 1) {
            species = null;
        } else {
            species = newSpecies.split(/\s*,\s*/);
        }

        const habitat = await this.client.updateHabitat(this.habitatId, newName, species, newActive, (error) => {
            updateButton.innerText = origButtonText;
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
        });
        this.dataStore.set('habitat', habitat);
        this.redirectToHabitat(this.habitatId);
    }

    /**
     * When the habitat is updated in the datastore, redirect back to the habitat page.
     */
    redirectToHabitat(habitatId) {
         window.location.href = `/habitat.html?habitatId=${habitatId}`;

    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const updateHabitat = new UpdateHabitat();
    updateHabitat.mount();
};

window.addEventListener('DOMContentLoaded', main);