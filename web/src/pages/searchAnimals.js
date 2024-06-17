import AnimalEnrichmentTrackerClient from '../api/animalEnrichmentTrackerClient';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/*
The code below this comment is equivalent to...
const EMPTY_DATASTORE_STATE = {
    'search-criteria': '',
    'search-results': [],
};

...but uses the "KEY" constants instead of "magic strings".
The "KEY" constants will be reused a few times below.
*/

const SEARCH_CRITERIA_KEY = 'search-animals-criteria';
const SEARCH_RESULTS_KEY = 'search-animals-results';
const EMPTY_DATASTORE_STATE = {
    [SEARCH_CRITERIA_KEY]: '',
    [SEARCH_RESULTS_KEY]: [],
};


/**
 * Logic needed for the search habitats page of the website.
 */
export default class SearchAnimals extends BindingClass {
    constructor(client) {
        super();
        this.client = client;

        this.bindClassMethods(['mount', 'search', 'displaySearchResults', 'getHTMLForSearchResults'], this);

        // Create a enw datastore with an initial "empty" state.
        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.dataStore.addChangeListener(this.displaySearchResults);
        console.log("searchAnimals constructor");
    }

    /**
     * load the AnimalEnrichmentTrackerClient.
     */
    mount() {
        // Wire up the form's 'submit' event and the button's 'click' event to the search method.
        document.getElementById('search-animals-form').addEventListener('submit', this.search);
        document.getElementById('search-animals-btn').addEventListener('click', this.search);
    }

    /**
     * Uses the client to perform the search,
     * then updates the datastore with the criteria and results.
     * @param evt The "event" object representing the user-initiated event that triggered this method.
     */
    async search(evt) {
        // Prevent submitting the from from reloading the page.
        evt.preventDefault();

        const searchCriteria = document.getElementById('search-animals-criteria').value;
        const previousSearchCriteria = this.dataStore.get(SEARCH_CRITERIA_KEY);

        // If the user didn't change the search criteria, do nothing
        if (previousSearchCriteria === searchCriteria) {
            return;
        }

        document.getElementById('search-animals-btn').innerText = "Loading...";

        if (searchCriteria) {
            const results = await this.client.searchAnimals(searchCriteria);

            this.dataStore.setState({
                [SEARCH_CRITERIA_KEY]: searchCriteria,
                [SEARCH_RESULTS_KEY]: results,
            });
        } else {
            this.dataStore.setState(EMPTY_DATASTORE_STATE);
        }

        document.getElementById('search-animals-btn').innerText = "Search";
    }

    /**
     * Pulls search results from the datastore and displays them on the html page.
     */
    displaySearchResults() {
        const searchCriteria = this.dataStore.get(SEARCH_CRITERIA_KEY);
        const searchResults = this.dataStore.get(SEARCH_RESULTS_KEY);

        const searchResultsContainer = document.getElementById('search-animals-results-container');
        const searchCriteriaDisplay = document.getElementById('search-animals-criteria-display');
        const searchResultsDisplay = document.getElementById('search-animals-results-display');

        if (searchCriteria === '') {
            searchResultsContainer.classList.add('hidden');
            searchCriteriaDisplay.innerHTML = '';
            searchResultsDisplay.innerHTML = '';
        } else {
            searchResultsContainer.classList.remove('hidden');
            searchCriteriaDisplay.innerHTML = `"${searchCriteria}"`;
            searchResultsDisplay.innerHTML = this.getHTMLForSearchResults(searchResults);
        }
    }

    /**
     * Create appropriate HTML for displaying searchResults on the page.
     * @param searchResults An array of habitats objects to be displayed on the page.
     * @returns A string of HTML suitable for being dropped on the page.
     */
    getHTMLForSearchResults(searchResults) {
        if (searchResults.length === 0) {
            return '<h4>No Results found</h4>';
        }

        let html = '<table id="animals-table"><tr><th>ID</th><th>Name</th><th>Age</th><th>Sex</th><th>Species</th><th>Habitat ID</th><th>Status</th><th>Currently On Habitat</th></tr>';
        let res;
        for (res of searchResults) {
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
            </tr>`;
        }
        html += '</table>';

        return html;
    }

}