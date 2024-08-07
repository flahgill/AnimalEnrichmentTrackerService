import AnimalEnrichmentTrackerClient from '../api/animalEnrichmentTrackerClient';
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
 * Logic needed for the view animals page of the website.
 */
class ViewAnimals extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['clientLoaded', 'mount', 'addAnimalsToPage', 'addAnimal', 'removeAnimalFromHabitat',
        'redirectToUpdateAnimal', 'checkLoginStatus'], this);
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.addAnimalsToPage);
        this.header = new Header(this.dataStore);
        console.log("viewAnimals constructor");
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
        document.getElementById('animals').innerText = "(loading animals...)";
        const habitatAnimals = await this.client.getAnimalsForHabitat(habitatId);
        this.dataStore.set('habitat-animals', habitatAnimals);
    }

    async checkLoginStatus() {
        const user = await this.client.getIdentity();
        this.isLoggedIn = !!user;
        const addAnimalFormSection = document.querySelector('.b.card');

        if (user) {
            addAnimalFormSection.classList.remove('hidden');
        }

        this.addAnimalsToPage();
    }

    /**
     * Add the header to the page and load the AnimalEnrichmentTrackerClient.
     */
    mount() {
        document.getElementById('add-animal-btn').addEventListener('click', this.addAnimal);
        document.getElementById('animals').addEventListener('click', this.removeAnimalFromHabitat);
        document.getElementById('animals').addEventListener('click', this.redirectToUpdateAnimal);


        this.header.addHeaderToPage();

        this.client = new AnimalEnrichmentTrackerClient();
        this.clientLoaded();

        document.getElementById('ok-button').addEventListener("click", this.closeModal);

        this.checkLoginStatus();

    }

    /**
     * When the list of animals are updated in the datastore, update the animals metadata on the page.
     */
    addAnimalsToPage() {
        const habitatAnimals = this.dataStore.get('habitat-animals');
        const habitat = this.dataStore.get('habitat');
        if (habitatAnimals == null || habitat == null) {
            return;
        }

        document.getElementById('habitat-name').innerText = habitat.habitatName;
        document.getElementById('habitat-owner').innerText = habitat.keeperName;
        document.getElementById('owner-email').innerText = habitat.keeperManagerId;
        document.getElementById('habitat-id').innerText = habitat.habitatId;

        let speciesHtml = '';
        let spec;
        for (spec of habitat.species) {
            speciesHtml += `<div class="species">${spec}</div>`;
        }
        document.getElementById('species').innerHTML = speciesHtml;

        let animalsHtml = '<table id="animals-table"><tr><th>ID</th><th>Name</th><th>Age</th><th>Sex</th><th>Species</th>';
        if (this.isLoggedIn) {
            animalsHtml += '<th>Update Animal</th><th>Remove From Habitat</th>';
        }
        animalsHtml += '</tr>';
        let animal;
        for (animal of habitatAnimals) {
            animalsHtml += `
                <tr id="${animal.animalId}">
                    <td>${animal.animalId}</td>
                    <td>
                        <a href="animal.html?animalId=${animal.animalId}">${animal.animalName}</a>
                    </td>
                    <td>${animal.age}</td>
                    <td>${animal.sex}</td>
                    <td>${animal.species}</td>`;
            if (this.isLoggedIn) {
                animalsHtml += `
                    <td><button data-id="${animal.animalId}" class="button update-animal">Update</button></td>
                    <td><button data-id="${animal.animalId}" class="button remove-animal">Remove</button></td>`;
            }
            animalsHtml += `</tr>`;
        }

        animalsHtml += '</table>';
        document.getElementById('animals').innerHTML = animalsHtml;

        const speciesSelect = document.getElementById('animal-species');
        speciesSelect.innerHTML = '';

        const uniqueSortedSpecies = Array.from(new Set(habitat.species)).sort();
        for (const sp of uniqueSortedSpecies) {
            const option = document.createElement('option');
            option.value = sp;
            option.textContent = sp;
            speciesSelect.appendChild(option);
        }
    }

    /*
    * method to run when the add animal submit button is pressed. Call the AnimalEnrichmentTrackerClient to add
    * an animal to a habitat's list of animals.
    */
    async addAnimal() {
        const errorMessageDisplay = document.getElementById('add-animal-error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        const habitat = this.dataStore.get('habitat');
        const habitatAnimals = this.dataStore.get('habitat-animals');
        if (habitatAnimals == null || habitat == null) {
            return;
        }

        document.getElementById('add-animal-btn').innerText = 'Adding...';
        const animalName = document.getElementById('animal-name').value;
        const age = document.getElementById('age').value;
        const sex = document.getElementById('sex').value;
        const species = document.getElementById('animal-species').value;
        const habitatId = habitat.habitatId;

        let errorOccurred = false;
        const newAnimal = await this.client.addAnimalToHabitat(habitatId, animalName, age, sex, species, (error) => {
           errorMessageDisplay.innerText = `Error: ${error.message}`;
           errorMessageDisplay.classList.remove('hidden');
           errorOccurred = true;
           this.showErrorModal(error.message);
           document.getElementById('add-animal-btn').innerText = 'Add';
        });

        if (!errorOccurred) {
            this.dataStore.set('habitat-animals', habitatAnimals);

            document.getElementById('add-animal-btn').innerText = 'Add';
            location.reload();
        }
    }

    /**
    * method to run when the remove animal from habitat button is cliecked. class the AnimalEnrichmentTrackerService to
    * remove the animal from the habitat.
    */
    async removeAnimalFromHabitat(e) {
        const habitat = this.dataStore.get('habitat');
        const habitatId = habitat.habitatId;

        const removeButton = e.target;
        if (!removeButton.classList.contains("remove-animal")) {
            return;
        }

        removeButton.innerText = "Removing...";

        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        let errorOccurred = false;
        await this.client.removeAnimalFromHabitat(habitatId, removeButton.dataset.id, (error) => {
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
            errorOccurred = true;
            this.showErrorModal(error.message);
            removeButton.innerText = "Remove";
        });

        if (!errorOccurred) {
            document.getElementById(removeButton.dataset.id).remove();
        }
    }

    /**
    * when the update button is clicked, redirects to update animal page.
    */
    async redirectToUpdateAnimal(e) {
        const updateButton = e.target;
        if (!updateButton.classList.contains("update-animal")) {
            return;
        }

        updateButton.innerText = "Loading...";

        if (updateButton != null) {
            window.location.href = `/updateAnimal.html?animalId=${updateButton.dataset.id}`;
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
    const viewAnimals = new ViewAnimals();
    viewAnimals.mount();
};

window.addEventListener('DOMContentLoaded', main);