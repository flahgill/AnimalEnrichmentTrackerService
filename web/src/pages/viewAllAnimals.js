import AnimalEnrichmentTrackerClient from '../api/animalEnrichmentTrackerClient';
import BindingClass from "../util/bindingClass";
import Header from '../components/header';
import DataStore from "../util/DataStore";


const FILTER_CRITERIA_KEY = 'filter-criteria';
const FILTER_RESULTS_KEY = 'filter-results';
const EMPTY_DATASTORE_STATE = {
    [FILTER_CRITERIA_KEY]: 'active',
    [FILTER_RESULTS_KEY]: [],
};


/**
 * Logic needed for the view all animals page of the website.
 */
class ViewAllAnimals extends BindingClass {
    constructor() {
        super();

        this.bindClassMethods(['mount', 'toggleFilter', 'displayAnimalResults', 'getHTMLForFilterResults', 'removeAnimal',
        'redirectToUpdateAnimal'], this);

        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.dataStore.addChangeListener(this.displayAnimalResults);
        this.header = new Header(this.dataStore);
        console.log("ViewAllAnimals constructor");
    }

    /**
     * Load the AnimalEnrichmentTrackerClient.
     */
    mount() {
        document.getElementById('toggle-filter-btn').addEventListener('click', this.toggleFilter);
        document.getElementById('all-animals').addEventListener('click', this.removeAnimal);
        document.getElementById('all-animals').addEventListener('click', this.redirectToUpdateAnimal);

        this.client = new AnimalEnrichmentTrackerClient();

        this.toggleFilter();
        this.header.addHeaderToPage();

        document.getElementById('ok-button').addEventListener("click", this.closeModal);
    }

    /**
     * Toggle the filter criteria between 'active' and 'inactive',
     * then perform the filter and update the datastore with the results.
     * @param evt The "event" object representing the user-initiated event that triggered this method.
     */
    async toggleFilter(evt) {
        if (evt) {
            evt.preventDefault();
        }

        const currentCriteria = this.dataStore.get(FILTER_CRITERIA_KEY);
        const newCriteria = currentCriteria === 'active' ? 'inactive' : 'active';

        const results = await this.client.getAllAnimals(newCriteria);

        this.dataStore.setState({
            [FILTER_CRITERIA_KEY]: newCriteria,
            [FILTER_RESULTS_KEY]: results,
        });
    }

    /**
     * Pulls filter results from the datastore and displays them on the HTML page.
     */
    displayAnimalResults() {
        const filterCriteria = this.dataStore.get(FILTER_CRITERIA_KEY);
        const filterResults = this.dataStore.get(FILTER_RESULTS_KEY);

        const filterResultsContainer = document.getElementById('filter-results-container');
        const filterResultsDisplay = document.getElementById('filter-results-display');
        const filterCriteriaDisplay = document.getElementById('filter-criteria-display');

        filterCriteriaDisplay.textContent = `Showing ${filterCriteria} Animals`;

        if (filterResults.length === 0) {
            filterResultsContainer.classList.add('hidden');
            filterResultsDisplay.innerHTML = '<h4>No Animals Found</h4>';
        } else {
            filterResultsContainer.classList.remove('hidden');
            filterResultsDisplay.innerHTML = this.getHTMLForFilterResults(filterResults);
        }
    }

    /**
     * Create appropriate HTML for displaying filterResults on the page.
     * @param filterResults An array of animals objects to be displayed on the page.
     * @returns A string of HTML suitable for being dropped on the page.
     */
    getHTMLForFilterResults(filterResults) {
        if (filterResults.length === 0) {
            return '<h4>No Animals found</h4>';
        }

        let html = '<table id="animals-table"><tr><th>ID</th><th>Name</th><th>Age</th><th>Sex</th><th>Species</th><th>Habitat ID</th><th>Status</th><th>Currently On Habitat</th><th>Update Animal</th><th>Delete Permanently</th></tr>';
        for (const res of filterResults) {
            const habitatId = res.habitatId || '';
            html += `
            <tr id= "${res.animalId}">
                <td>${res.animalId}</td>
                <td>
                    <a href="animal.html?animalId=${res.animalId}">${res.animalName}</a>
                </td>
                <td>${res.age}</td>
                <td>${res.sex}</td>
                <td>${res.species}</td>
                <td>
                    <a href="habitat.html?habitatId=${habitatId}">${habitatId}</a>
                </td>
                <td>${res.isActive}</td>
                <td>
                <label class="container">
                <input type="checkbox" ${res.onHabitat ? 'checked' : ''} disabled>
                <span class="checkmark"></span>
                </label>
                </td>
                <td><button data-id="${res.animalId}" class="button update-animal">Update</button></td>
                <td><button data-id="${res.animalId}" class="button remove-animal">Delete</button></td>
            </tr>`;
        }
        html += '</table>';

        return html;
    }

    /**
    * when delete button is clicked, deletes animal permanently
    */
    async removeAnimal(e) {
        const removeButton = e.target;

        if (!removeButton.classList.contains('remove-animal')) {
            return;
        }

        const animalId = removeButton.dataset.id;

        removeButton.innerText = "DELETING...";

        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        let errorOccurred = false;
        const updatedAnimals = await this.client.removeAnimal(animalId, (error) => {
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
            errorOccurred = true;
            this.showErrorModal(error.message);
            removeButton.innerText = "Delete";
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
    const viewAllAnimals = new ViewAllAnimals();
    viewAllAnimals.mount();
};

window.addEventListener('DOMContentLoaded', main);
