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
        document.getElementById('animals').innerText = "(loading animals...)"
        const animals = await this.client.getAnimalsForHabitat(habitatId);
        this.dataStore.set('animals', animals);
    }

    /**
     * Add the header to the page and load the AnimalEnrichmentTrackerClient.
     */
    mount() {
        document.getElementById('add-animal-btn').addEventListener('click', this.addAnimal);

        this.header.addHeaderToPage();

        this.client = new AnimalEnrichmentTrackerClient();
        this.clientLoaded();
    }

    /**
     * When the list of animals are updated in the datastore, update the animals metadata on the page.
     */
    addAnimalsToPage() {
        const animals = this.dataStore.get('animals');
        const habitat = this.dataStore.get('habitat');
        if (animals == null || habitat == null) {
            return;
        }

        document.getElementById('habitat-name').innerText = habitat.habitatName;
        document.getElementById('habitat-owner').innerText = habitat.keeperManagerId;
        document.getElementById('habitat-id').innerText = habitat.habitatId;

        let speciesHtml = '';
        let spec;
        for (spec of habitat.species) {
            speciesHtml += `<div class="species">${spec}</div>`;
        }
        document.getElementById('species').innerHTML = speciesHtml;

        let animalsHtml = '<table id="animals-table"><tr><th>Animal Name</th></tr>';
        let animal;
        for (animal of animals) {
            animalsHtml += `
               <tr>
                   <td>${animal}</td>
               </tr>`;
        }

        animalsHtml += '</table>';
        document.getElementById('animals').innerHTML = animalsHtml;
    }

    /*
    * method to run when the add book submit button is pressed. Call the AnimalEnrichmentTrackerClient to add
    * an animal to a habitat's list of animals.
    */
    async addAnimal() {
        const errorMessageDisplay = document.getElementById('add-animal-error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        const habitat = this.dataStore.get('habitat');
        if (habitat == null) {
            return;
        }

        document.getElementById('add-animal-btn').innerText = 'Adding...';
        const animalToBeAdded = document.getElementById('animal-name').value;
        const habitatId = habitat.habitatId;

        let shouldReload = false;

         const animals = await this.client.addAnimalToHabitat(habitatId, animalToBeAdded, (error) => {
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
         });

         this.dataStore.set('animals', animals);

         document.getElementById('add-animal-btn').innerText = 'Add';
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