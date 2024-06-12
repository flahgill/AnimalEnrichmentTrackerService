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
        this.bindClassMethods(['clientLoaded', 'mount', 'addAnimalsToPage'], this);
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
                   <td>${animal.animalName}</td>
                   <td>${animal.age}</td>
                   <td>${animal.sex}</td>
                   <td>${animal.species}</td>
               </tr>`;
        }

        animalsHtml += '</table>';
        document.getElementById('animals').innerHTML = animalsHtml;
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