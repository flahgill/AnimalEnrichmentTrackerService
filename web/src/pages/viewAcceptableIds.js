import AnimalEnrichmentTrackerClient from '../api/animalEnrichmentTrackerClient';
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
 * Logic needed for the view animals page of the website.
 */
export default class ViewAcceptableIds extends BindingClass {
    constructor(client) {
        super();
        this.client = client;
        this.bindClassMethods(['clientLoaded', 'mount', 'addIdsToPage', 'addId', 'removeId'], this);
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.addIdsToPage);
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
        document.getElementById('acceptable-ids').addEventListener('click', this.removeId);

        this.header.addHeaderToPage();

        this.clientLoaded();

        document.getElementById('ok-button').addEventListener("click", this.closeModal);

    }

    /**
     * When the list of acceptable Ids are updated in the datastore, update the Ids metadata on the page.
     */
    addIdsToPage() {
        const acceptableIds = this.dataStore.get('acceptableIds');
        const habitat = this.dataStore.get('habitat');
        if (acceptableIds == null || habitat == null) {
            return;
        }

        const enrichmentMapping = {
            "01": "Amazing Graze",
            "02": "Boomer Ball",
            "03": "Jolly Ball",
            "04": "Bubbles",
            "05": "Chalk Drawings",
            "06": "Cap Feeder",
            "07": "Hay Play Feeder",
            "08": "Honeycomb Feeder",
            "09": "Jolly Stall",
            "10": "Keg",
            "11": "Looks Lou",
            "12": "Paper Chains",
            "13": "Planter Bucket",
            "14": "PVC Roller",
            "15": "Slow Feeder Bowls",
            "16": "Scent/Spices",
            "17": "Sprinkler",
            "18": "Wiffle Ball Feeder",
            "19": "Weeble",
            "20": "Squiggle Feeder"
        };

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

        let idsHtml = '<table id="animals-table"><tr><th>Id</th><th>Enrichment Activity</th><th>Remove</th></tr>';
        let id;
        for (id of acceptableIds) {
            const enrichName = enrichmentMapping[id] || "unknown";
            idsHtml += `
               <tr id="id-${id}">
                   <td>${id}</td>
                   <td>${enrichName}</td>
                   <td><button data-id="${id}" class="button remove-id">Remove</button></td>
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

    /*
    * method to run when the remove id button is clicked. Call the AnimalEnrichmentTrackerClient to remove
    * an id from a habitat's list of acceptable enrichment ids.
    */
    async removeId(e) {
        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        const habitat = this.dataStore.get('habitat');
        if (habitat == null) {
            return;
        }

        const removeButton = e.target;
        if (!removeButton.classList.contains("remove-id")) {
            return;
        }

        removeButton.innerText = "Removing...";
        const habitatId = habitat.habitatId;

        const idToRemove = removeButton.dataset.id;

        const acceptableIds = await this.client.removeAcceptableId(habitatId, idToRemove, (error) => {
           errorMessageDisplay.innerText = `Error: ${error.message}`;
           errorMessageDisplay.classList.remove('hidden');
           this.showErrorModal(error.message);
        });

        this.dataStore.set('acceptableIds', acceptableIds);
        removeButton.innerText = "Remove";
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