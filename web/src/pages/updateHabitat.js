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
        this.bindClassMethods(['mount', 'clientLoaded', 'submit', 'handleCheckboxChange', 'addHabitatToPage', 'redirectToHabitat'], this);
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
        document.getElementById('activate-status').addEventListener('change', this.handleCheckboxChange);
        document.getElementById('deactivate-status').addEventListener('change', this.handleCheckboxChange);

        this.header.addHeaderToPage();

        this.client = new AnimalEnrichmentTrackerClient();
        this.clientLoaded();

        document.getElementById('ok-button').addEventListener("click", this.closeModal);
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
        document.getElementById('habitat-owner').innerText = habitat.keeperName;
        document.getElementById('owner-email').innerText = habitat.keeperManagerId;
        document.getElementById('habitat-id').innerText = habitat.habitatId;
        document.getElementById('curr-active-status').innerText = habitat.isActive;

        let speciesHtml = '';
        for (const spec of habitat.species) {
            speciesHtml += `<div class="species">${spec}</div>`;
        }
        document.getElementById('species').innerHTML = speciesHtml;

        document.getElementById('activate-status').checked = habitat.isActive;
        document.getElementById('deactivate-status').checked = !habitat.isActive;
    }

    /**
     * Ensure only one checkbox is checked at a time.
     */
    handleCheckboxChange() {
        const activateCheckbox = document.getElementById('activate-status');
        const deactivateCheckbox = document.getElementById('deactivate-status');

        if (activateCheckbox.checked) {
            deactivateCheckbox.checked = false;
        }

        if (deactivateCheckbox.checked) {
            activateCheckbox.checked = false;
        }
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

        const newName = document.getElementById('new-name').value;
        const newActive = document.getElementById('activate-status').checked ? "active" : "inactive";

        let errorOccurred = false;
        const habitat = await this.client.updateHabitat(this.habitatId, newName, newActive, (error) => {
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
            errorOccurred = true;
            this.showErrorModal(error.message);
            updateButton.innerText = origButtonText;
        });

        if (!errorOccurred) {
            this.dataStore.set('habitat', habitat);
            this.redirectToHabitat(this.habitatId);
        }
    }

    /**
     * When the habitat is updated in the datastore, redirect back to the habitat page.
     */
    redirectToHabitat(habitatId) {
        window.location.href = `/habitat.html?habitatId=${habitatId}`;
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
    const updateHabitat = new UpdateHabitat();
    updateHabitat.mount();
};

window.addEventListener('DOMContentLoaded', main);
