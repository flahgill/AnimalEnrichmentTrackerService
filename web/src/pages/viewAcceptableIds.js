import AnimalEnrichmentTrackerClient from '../api/animalEnrichmentTrackerClient';
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
 * Logic needed for the view animals page of the website.
 */
class ViewAcceptableIds extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['clientLoaded', 'mount', 'addAnimalsToPage', 'addId'], this);
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.addAnimalsToPage);
        this.header = new Header(this.dataStore);
        console.log("viewAcceptableIds constructor");
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
        document.getElementById('acceptable-ids').innerText = "(loading Ids...)"
        const acceptableIds = await this.client.getAcceptableIds(habitatId);
        this.dataStore.set('acceptableIds', acceptableIds);
    }

    /**
     * Add the header to the page and load the AnimalEnrichmentTrackerClient.
     */
    mount() {
        document.getElementById('add-id-btn').addEventListener('click', this.addId);
        this.header.addHeaderToPage();

        this.client = new AnimalEnrichmentTrackerClient();
        this.clientLoaded();

        document.getElementById('ok-button').addEventListener("click", this.closeModal);

    }

    /**
     * When the list of animals are updated in the datastore, update the animals metadata on the page.
     */
    addAnimalsToPage() {
        const acceptableIds = this.dataStore.get('acceptableIds');
        const habitat = this.dataStore.get('habitat');
        if (acceptableIds == null || habitat == null) {
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

        let idsHtml = '<table id="animals-table"><tr><th>Id</th></tr>';
        let id;
        for (id of acceptableIds) {
            idsHtml += `
               <tr id="id-${id}">
                   <td>${id}</td>
               </tr>`;
        }

        idsHtml += '</table>';
        document.getElementById('acceptable-ids').innerHTML = idsHtml;
    }

    /*
    * method to run when the add id submit button is pressed. Call the AnimalEnrichmentTrackerClient to add
    * an id to a habitat's list of acceptable enrichment ids.
    */
    async addId() {
        const errorMessageDisplay = document.getElementById('add-id-error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        const habitat = this.dataStore.get('habitat');
        if (habitat == null) {
            return;
        }

        document.getElementById('add-id-btn').innerText = 'Adding...';
        const idToBeAdded = document.getElementById('acceptable-id').value;
        const habitatId = habitat.habitatId;

        const acceptableIds = await this.client.addAcceptableId(habitatId, idToBeAdded, (error) => {
           errorMessageDisplay.innerText = `Error: ${error.message}`;
           errorMessageDisplay.classList.remove('hidden');
           this.showErrorModal(error.message);
        });

        this.dataStore.set('acceptableIds', acceptableIds);
        document.getElementById('add-id-btn').innerText = 'Add';

        if (acceptableIds.includes(idToBeAdded)) {
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
    const viewAcceptableIds = new ViewAcceptableIds();
    viewAcceptableIds.mount();
};

window.addEventListener('DOMContentLoaded', main);