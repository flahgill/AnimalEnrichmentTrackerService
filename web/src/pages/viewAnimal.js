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
        this.bindClassMethods(['clientLoaded', 'mount', 'addAnimalToPage', 'removeAnimal'], this);
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.addAnimalToPage);
        this.header = new Header(this.dataStore);
        console.log("viewAnimal constructor");
    }

    /**
     * Once the client is loaded, get the playlist metadata and song list.
     */
    async clientLoaded() {
        const urlParams = new URLSearchParams(window.location.search);
        const animalId = urlParams.get('animalId');
        document.getElementById('animal-name').innerText = "Loading Animal ...";
        const animal = await this.client.getAnimal(animalId);
        this.dataStore.set('animal', animal);
    }

    /**
     * Add the header to the page and load the MusicPlaylistClient.
     */
    mount() {
        this.header.addHeaderToPage();
        document.getElementById('remove-animal').addEventListener("click", this.removeAnimal);

        this.client = new AnimalEnrichmentTrackerClient();
        this.clientLoaded();

        document.getElementById('ok-button').addEventListener("click", this.closeModal);
    }

    /**
     * When the habitat is updated in the datastore, update the habitat metadata on the page.
     */
    addAnimalToPage() {
        const animal = this.dataStore.get('animal');
        if (animal == null) {
            return;
        }

        document.getElementById('animal-name').innerText = animal.animalName;
        document.getElementById('animal-id').innerText = animal.animalId;
        document.getElementById('animal-age').innerText = animal.age;
        document.getElementById('animal-sex').innerText = animal.sex;
        document.getElementById('animal-species').innerText = animal.species;
        document.getElementById('active-status').innerText = animal.isActive;
        document.getElementById('animal-habitat-id').innerText = animal.habitatId;
    }

    /**
    * when remove button is clicked, removes habitat.
    */
    async removeAnimal(e) {
        const animalId = this.dataStore.get('animal').animalId;

        const removeButton = e.target;
        removeButton.innerText = "Deleting...";

        let errorOccurred = false;
        const animal = await this.client.removeAnimal(animalId, (error) => {
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
            errorOccurred = true;
            this.showErrorModal(error.message);
            removeButton.innerText = "Delete Animal";
        });

        if (!errorOccurred) {
            window.location.href = '/index.html';
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

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const viewHabitat = new ViewHabitat();
    viewHabitat.mount();
};

window.addEventListener('DOMContentLoaded', main);