import AnimalEnrichmentTrackerClient from '../api/animalEnrichmentTrackerClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

/**
 * Logic needed for the create habitat page of the website.
 */
class CreateHabitat extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'submit', 'redirectToViewHabitat'], this);
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.redirectToViewHabitat);
        this.header = new Header(this.dataStore);
        console.log("createHabitat constructor");
    }

    /**
     * Add the header to the page and load the AnimalEnrichmentTrackerClient.
     */
    mount() {
        document.getElementById('create').addEventListener('click', this.submit);

        this.header.addHeaderToPage();

        this.client = new AnimalEnrichmentTrackerClient();
        document.getElementById('ok-button').addEventListener("click", this.closeModal);
    }

    /**
     * Method to run when the create habitat submit button is pressed. Call the AnimalEnrichmentTrackerClient to create the
     * habitat.
     */
    async submit(evt) {
        evt.preventDefault();

        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        const createButton = document.getElementById('create');
        const origButtonText = createButton.innerText;
        createButton.innerText = 'Loading...';

        const habitatName = document.getElementById('habitat-name').value;
        const speciesText = document.getElementById('species').value;

        let species;
        if (speciesText.length < 1) {
            species = null;
        } else {
            species = speciesText.split(/\s*,\s*/);
        }

        let errorOccurred = false;
        const habitat = await this.client.createHabitat(habitatName, species, (error) => {
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
            errorOccurred = true;
            this.showErrorModal(error.message);
            createButton.innerText = origButtonText;
        });

        if (!errorOccurred) {
            this.dataStore.set('habitat', habitat);
        }
    }

    /**
     * When the habitat is updated in the datastore, redirect to the view habitat page.
     */
    redirectToViewHabitat() {
        const habitat = this.dataStore.get('habitat');
        if (habitat != null) {
            window.location.href = `/habitat.html?habitatId=${habitat.habitatId}`;
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
    const createHabitat = new CreateHabitat();
    createHabitat.mount();
};

window.addEventListener('DOMContentLoaded', main);