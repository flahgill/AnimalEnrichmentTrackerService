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
        'redirectToUpdateHabitat', 'redirectToViewHabitatEnrichments', 'redirectToViewAcceptableIds', 'redirectToViewSpecies'], this);
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
        document.getElementById('view-acceptable-ids-button').addEventListener("click", this.redirectToViewAcceptableIds)
        document.getElementById('remove-habitat').addEventListener("click", this.removeHabitat);
        document.getElementById('update-habitat').addEventListener("click", this.redirectToUpdateHabitat);
        document.getElementById('view-habitat-enrichments-button').addEventListener("click", this.redirectToViewHabitatEnrichments);
        document.getElementById('view-species-button').addEventListener("click", this.redirectToViewSpecies);

        this.client = new AnimalEnrichmentTrackerClient();
        this.clientLoaded();

        document.getElementById('ok-button').addEventListener("click", this.closeModal);
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
        document.getElementById('habitat-owner').innerText = habitat.keeperName;
        document.getElementById('owner-email').innerText = habitat.keeperManagerId;
        document.getElementById('habitat-id').innerText = habitat.habitatId;
        document.getElementById('total-animals').innerText = habitat.totalAnimals;
        document.getElementById('active-status').innerText = habitat.isActive;

        let acceptEnrichIdHtml = 'Acceptable Enrichments for Habitat: ';
        for (const id of habitat.acceptableEnrichmentIds) {
            acceptEnrichIdHtml += `<div class="accept-ids">${id}</div>`;
        }
        document.getElementById('acceptable-enrichment-ids').innerHTML = acceptEnrichIdHtml;

        let speciesHtml = '';
        for (const spec of habitat.species) {
            speciesHtml += `<div class="species">${spec}</div>`;
        }
        document.getElementById('species').innerHTML = speciesHtml;
    }

    /**
    * when view animals button is clicked, redirects to view animals page.
    */
    async redirectToViewAnimals(e) {
        const habitat = this.dataStore.get('habitat');
        const habitatId = habitat.habitatId;
        window.location.href = `viewAnimals.html?habitatId=${habitatId}`;
    }

    /**
    * when view acceptable ids button is clicked, redirects to view Acceptable Ids page.
    */
    async redirectToViewAcceptableIds(e) {
        const habitat = this.dataStore.get('habitat');
        const habitatId = habitat.habitatId;
        window.location.href = `viewAcceptableIds.html?habitatId=${habitatId}`;
    }

    /**
    * when view species button is clicked, redirects to view species page.
    */
    async redirectToViewSpecies(e) {
        const habitat = this.dataStore.get('habitat');
        const habitatId = habitat.habitatId;
        window.location.href = `viewSpeciesList.html?habitatId=${habitatId}`;
    }

    /**
    * when remove button is clicked, removes habitat.
    */
    async removeHabitat(e) {
        const habitatId = this.dataStore.get('habitat').habitatId;

        const removeButton = e.target;
        removeButton.innerText = "Removing...";

        try {
            await this.client.removeHabitat(habitatId);

            const habitat = await this.client.getHabitat(habitatId);
            if (!habitat) {
                window.location.href = '/index.html';
            } else {
                this.showErrorModal("You must own this habitat to delete it.");
                removeButton.innerText = "Delete Habitat";
            }
        } catch (error) {
            await this.showErrorModal(`Error: ${error.message}`);
            removeButton.innerText = "Delete Habitat";
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
    const viewHabitat = new ViewHabitat();
    viewHabitat.mount();
};

window.addEventListener('DOMContentLoaded', main);