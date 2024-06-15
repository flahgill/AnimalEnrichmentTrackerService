import AnimalEnrichmentTrackerClient from '../api/animalEnrichmentTrackerClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

/**
 * Logic needed for the update habitat page of the website.
 */
class UpdateAnimal extends BindingClass {

    urlParams = new URLSearchParams(window.location.search);
    animalId = this.urlParams.get('animalId');

    constructor() {
        super();
        this.bindClassMethods(['mount', 'clientLoaded', 'submit', 'addAnimalToPage', 'redirectToAnimal'], this);
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.addAnimalToPage);
        this.header = new Header(this.dataStore);
        console.log("updateAnimal constructor");
    }

    /**
     * Add the header to the page and load the AnimalEnrichmentTrackerClient.
     */
    mount() {
        document.getElementById('update-animal').addEventListener('click', this.submit);
        document.getElementById('increase-age').addEventListener('click', () => this.adjustAge(1));
        document.getElementById('decrease-age').addEventListener('click', () => this.adjustAge(-1));

        this.header.addHeaderToPage();

        this.client = new AnimalEnrichmentTrackerClient();
        this.clientLoaded();

        document.getElementById('ok-button').addEventListener("click", this.closeModal);
    }

    /**
     * Once the client is loaded, get the animal metadata.
     */
    async clientLoaded() {
        document.getElementById('animal-name').innerText = "Loading Animal ...";

        const animal = await this.client.getAnimal(this.animalId);
        this.dataStore.set('animal', animal);

        if (animal.habitatId) {
            const habitat = await this.client.getHabitat(animal.habitatId);
            const speciesDropdown = document.getElementById('species-dropdown');
            habitat.species.forEach(species => {
                const option = document.createElement('option');
                option.value = species;
                option.text = species;
                speciesDropdown.add(option);
            });

            speciesDropdown.classList.remove('hidden');
            document.getElementById('species-textbox').classList.add('hidden');
        } else {
            document.getElementById('species-dropdown').classList.add('hidden');
            document.getElementById('species-textbox').classList.remove('hidden');
        }

        document.getElementById('new-age').value = animal.age;
    }

    /**
     * When the animal is updated in the datastore, update the animal metadata on the page.
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
     * Method to run when the update animal submit button is pressed. Call the AnimalEnrichmentTrackerClient to update the
     * animal.
     */
    async submit(evt) {
        evt.preventDefault();

        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        const updateButton = document.getElementById('update-animal');
        const origButtonText = updateButton.innerText;
        updateButton.innerText = 'Loading...';

        const newName = document.getElementById('new-name').value;
        const newAge = document.getElementById('new-age').value;
        const newSex = document.getElementById('new-sex').value;
        let newSpecies;

        if (!document.getElementById('species-dropdown').classList.contains('hidden')) {
            newSpecies = document.getElementById('species-dropdown').value;
        } else {
            newSpecies = document.getElementById('species-textbox').value;
        }

        let errorOccurred = false;
        const animal = await this.client.updateAnimal(this.animalId, newName, newAge, newSex, newSpecies, (error) => {
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
            errorOccurred = true;
            this.showErrorModal(error.message);
            updateButton.innerText = origButtonText;
        });

        if (!errorOccurred) {
            this.dataStore.set('animal', animal);
            this.redirectToAnimal(this.animalId);
        }
    }

    /**
     * Adjust the age input field by a given increment.
     */
    adjustAge(increment) {
        const ageInput = document.getElementById('new-age');
        const currentAge = parseInt(ageInput.value, 10);
        const newAge = Math.max(0, currentAge + increment); // Ensure age doesn't go below 0
        ageInput.value = newAge;
    }

    /**
     * When the animal is updated in the datastore, redirect back to the animal page.
     */
    redirectToAnimal(animalId) {
        window.location.href = `/animal.html?animalId=${animalId}`;
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
    const updateAnimal = new UpdateAnimal();
    updateAnimal.mount();
};

window.addEventListener('DOMContentLoaded', main);
