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
        this.bindClassMethods(['clientLoaded', 'mount', 'addAnimalsToPage', 'addAnimal'], this);
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
        console.log(habitatAnimals);
        this.dataStore.set('habitat-animals', habitatAnimals);
    }

    /**
     * Add the header to the page and load the AnimalEnrichmentTrackerClient.
     */
    mount() {
        document.getElementById('add-animal-btn').addEventListener('click', this.addAnimal);

        this.header.addHeaderToPage();

        this.client = new AnimalEnrichmentTrackerClient();
        this.clientLoaded();

        document.getElementById('ok-button').addEventListener("click", this.closeModal);

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

        let animalsHtml = '<table id="animals-table"><tr><th>ID</th><th>Name</th><th>Age</th><th>Sex</th><th>Species</th></tr>';
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
                   <td>${animal.species}</td>
               </tr>`;
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