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
 * Logic needed for the view all habitats page of the website.
 */
class ViewAllHabitats extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'toggleFilter', 'displayHabitatResults', 'getHTMLForFilterResults', 'redirectToUpdateHabitat',
        'checkLoginStatus'], this);
        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.dataStore.addChangeListener(this.displayHabitatResults);
        this.header = new Header(this.dataStore);
        console.log("ViewAllHabitats constructor");
    }

    async checkLoginStatus() {
        const user = await this.client.getIdentity();
        this.isLoggedIn = !!user;
    }

    /**
     * Load the AnimalEnrichmentTrackerClient.
     */
    async mount() {
        this.client = new AnimalEnrichmentTrackerClient();

        await this.checkLoginStatus();

        document.getElementById('toggle-filter-btn').addEventListener('click', this.toggleFilter);
        document.getElementById('filter-results-display').addEventListener("click", this.redirectToUpdateHabitat);

        this.toggleFilter();
        this.header.addHeaderToPage();
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

        const results = await this.client.getAllHabitats(newCriteria);

        this.dataStore.setState({
            [FILTER_CRITERIA_KEY]: newCriteria,
            [FILTER_RESULTS_KEY]: results,
        });
    }

    /**
     * Pulls filter results from the datastore and displays them on the HTML page.
     */
    displayHabitatResults() {
        const filterCriteria = this.dataStore.get(FILTER_CRITERIA_KEY);
        const filterResults = this.dataStore.get(FILTER_RESULTS_KEY);

        const filterResultsContainer = document.getElementById('filter-results-container');
        const filterResultsDisplay = document.getElementById('filter-results-display');
        const filterCriteriaDisplay = document.getElementById('filter-criteria-display');

        filterCriteriaDisplay.textContent = `Showing ${filterCriteria} habitats`;

        if (filterResults.length === 0) {
            filterResultsContainer.classList.add('hidden');
            filterResultsDisplay.innerHTML = '<h4>No Habitats Found</h4>';
        } else {
            filterResultsContainer.classList.remove('hidden');
            filterResultsDisplay.innerHTML = this.getHTMLForFilterResults(filterResults);
        }
    }

    /**
     * Create appropriate HTML for displaying filterResults on the page.
     * @param filterResults An array of habitats objects to be displayed on the page.
     * @returns A string of HTML suitable for being dropped on the page.
     */
    getHTMLForFilterResults(filterResults) {
        if (filterResults.length === 0) {
            return '<h4>No habitats found</h4>';
        }

        let html = '<table id="habitats-table"><tr><th>Habitat</th><th>Habitat ID</th><th>Species</th><th>Total Animals</th><th>Animals</th><th>Status</th><th>Keeper</th><th>Keeper Email</th>';
        if (this.isLoggedIn) {
            html += '<th>Update Habitat</th>';
        }
        html += '</tr>';
        for (const res of filterResults) {
            html += `
            <tr id="${res.habitatId}">
                <td>
                    <a href="habitat.html?habitatId=${res.habitatId}">${res.habitatName}</a>
                </td>
                <td>${res.habitatId}</td>
                <td>${res.species?.join(', ')}</td>
                <td>${res.totalAnimals}</td>
                <td>${res.animalsInHabitat?.join(', ')}</td>
                <td>${res.isActive}</td>
                <td>${res.keeperName}</td>
                <td>${res.keeperManagerId}</td>`;
                if (this.isLoggedIn) {
                    html += `
                        <td><button data-id="${res.habitatId}" class="button update-habitat">Update</button></td>`
                }
                html += `</tr>`;
        }

        html += '</table>';

        return html;
    }

    /**
    * when the update button is clicked, redirects to update habitat page.
    */
    async redirectToUpdateHabitat(e) {
        const updateButton = e.target;
        if (!updateButton.classList.contains("update-habitat")) {
            return;
        }

        updateButton.innerText = "Loading...";

        if (updateButton != null) {
            window.location.href = `/updateHabitat.html?habitatId=${updateButton.dataset.id}`;
        }
    }

}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const viewAllHabitats = new ViewAllHabitats();
    viewAllHabitats.mount();
};

window.addEventListener('DOMContentLoaded', main);
