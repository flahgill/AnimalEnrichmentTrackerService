import AnimalEnrichmentTrackerClient from '../api/animalEnrichmentTrackerClient';
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
 * Logic needed for the view playlist page of the website.
 */
class ViewHabitat extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['clientLoaded', 'mount', 'addHabitatToPage', 'redirectToViewAnimals', 'removeHabitat',
        'redirectToUpdateHabitat', 'redirectToViewHabitatEnrichments'], this);
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.addHabitatToPage);
        this.header = new Header(this.dataStore);
        console.log("viewHabitat constructor");
    }

    /**
     * Once the client is loaded, get the playlist metadata and song list.
     */
    async clientLoaded() {
        const urlParams = new URLSearchParams(window.location.search);
        const habitatId = urlParams.get('habitatId');
        document.getElementById('habitat-name').innerText = "Loading Habitat ...";
        const habitat = await this.client.getHabitat(habitatId);
        this.dataStore.set('habitat', habitat);
    }

    /**
     * Add the header to the page and load the MusicPlaylistClient.
     */
    mount() {
        this.header.addHeaderToPage();
        document.getElementById('view-animals-button').addEventListener("click", this.redirectToViewAnimals);
        document.getElementById('remove-habitat').addEventListener("click", this.removeHabitat);
        document.getElementById('update-habitat').addEventListener("click", this.redirectToUpdateHabitat);
        document.getElementById('view-habitat-enrichments-button').addEventListener("click", this.redirectToViewHabitatEnrichments);

        this.client = new AnimalEnrichmentTrackerClient();
        this.clientLoaded();
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
        document.getElementById('total-animals').innerText = habitat.totalAnimals;
        document.getElementById('active-status').innerText = habitat.isActive;

        let speciesHtml = '';
        let spec;
        for (spec of habitat.species) {
            speciesHtml += `<div class="species">${spec}</div>`;
        }
        document.getElementById('species').innerHTML = speciesHtml;
    }

    async redirectToViewAnimals(e) {
        const habitat = this.dataStore.get('habitat');
        const habitatId = habitat.habitatId;
        window.location.href = `viewAnimals.html?habitatId=${habitatId}`;
    }

    /**
    * when remove button is clicked, removes habitat.
    */
    async removeHabitat(e) {
        const habitatId = this.dataStore.get('habitat').habitatId;

        const removeButton = e.target;
        removeButton.innerText = "Removing...";

        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = '';
        errorMessageDisplay.classList.add('hidden');

        try {
            await this.client.removeHabitat(habitatId);
            window.location.href = '/index.html';
        } catch (error) {
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
            removeButton.innerText = "Remove Habitat"; // Reset button text in case of error
        }
    }

    /**
    * when the update button is clicked, redirects to update habitat page.
    */
    async redirectToUpdateHabitat(e) {
        const habitatId = this.dataStore.get('habitat').habitatId;
        const updateButton = e.target;

        updateButton.innerText = "Loading...";

        if (updateButton != null) {
            window.location.href = `/updateHabitat.html?habitatId=${habitatId}`;
        }
    }

    async redirectToViewHabitatEnrichments(e) {
        const habitat = this.dataStore.get('habitat');
        const habitatId = habitat.habitatId;
        window.location.href = `viewHabitatEnrichments.html?habitatId=${habitatId}`;
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const viewHabitat = new ViewHabitat();
    viewHabitat.mount();
};

window.addEventListener('DOMContentLoaded', main);