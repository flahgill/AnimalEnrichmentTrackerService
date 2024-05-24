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
        this.bindClassMethods(['clientLoaded', 'mount', 'addHabitatToPage', 'redirectToViewAnimals'], this);
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
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const viewHabitat = new ViewHabitat();
    viewHabitat.mount();
};

window.addEventListener('DOMContentLoaded', main);