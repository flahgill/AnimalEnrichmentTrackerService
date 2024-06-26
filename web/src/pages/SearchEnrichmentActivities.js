import AnimalEnrichmentTrackerClient from '../api/animalEnrichmentTrackerClient';
import SearchHabitats from './searchHabitats.js'
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

const SEARCH_CRITERIA_KEY = 'search-activities-criteria';
const SEARCH_RESULTS_KEY = 'search-activities-results';
const EMPTY_DATASTORE_STATE = {
    [SEARCH_CRITERIA_KEY]: '',
    [SEARCH_RESULTS_KEY]: [],
};


/**
 * Logic needed for the search habitats page of the website.
 */
export default class SearchEnrichmentActivities extends BindingClass {
    constructor(client) {
        super();
        this.client = client;

        this.bindClassMethods(['mount', 'search', 'displaySearchResults', 'getHTMLForSearchResults'], this);

        // Create a enw datastore with an initial "empty" state.
        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.dataStore.addChangeListener(this.displaySearchResults);
        console.log("searchEnrichmentActivities constructor");
    }

    /**
     * load the AnimalEnrichmentTrackerClient.
     */
    mount() {
        // Wire up the form's 'submit' event and the button's 'click' event to the search method.
        document.getElementById('search-activities-form').addEventListener('submit', this.search);
        document.getElementById('search-activities-btn').addEventListener('click', this.search);
    }

    /**
     * Uses the client to perform the search,
     * then updates the datastore with the criteria and results.
     * @param evt The "event" object representing the user-initiated event that triggered this method.
     */
    async search(evt) {
        // Prevent submitting the from from reloading the page.
        evt.preventDefault();

        const searchCriteria = document.getElementById('search-activities-criteria').value;
        const previousSearchCriteria = this.dataStore.get(SEARCH_CRITERIA_KEY);

        // If the user didn't change the search criteria, do nothing
        if (previousSearchCriteria === searchCriteria) {
            return;
        }

        document.getElementById('search-activities-btn').innerText = "Loading...";

        if (searchCriteria) {
            const results = await this.client.searchEnrichmentActivities(searchCriteria);

            this.dataStore.setState({
                [SEARCH_CRITERIA_KEY]: searchCriteria,
                [SEARCH_RESULTS_KEY]: results,
            });
        } else {
            this.dataStore.setState(EMPTY_DATASTORE_STATE);
        }

        document.getElementById('search-activities-btn').innerText = "Search";
    }

    /**
     * Pulls search results from the datastore and displays them on the html page.
     */
    displaySearchResults() {
        const searchCriteria = this.dataStore.get(SEARCH_CRITERIA_KEY);
        const searchResults = this.dataStore.get(SEARCH_RESULTS_KEY);

        const searchResultsContainer = document.getElementById('search-activities-results-container');
        const searchCriteriaDisplay = document.getElementById('search-activities-criteria-display');
        const searchResultsDisplay = document.getElementById('search-activities-results-display');

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

        let enrichHtml = '<table id="enrichments-table"><tr><th>Date Completed</th><th>Activity</th><th>Activity ID</th><th>Description</th><th>Rating</th><th>Completed</th><th>Habitat ID</th><th>Enrichment ID</th><th>Currently On Habitat</th></tr>';
        let enrich;
        for (enrich of searchResults) {
            enrichHtml += `
               <tr id="${enrich.activityId + enrich.habitatId}">
                   <td>${enrich.dateCompleted}</td>
                   <td>
                       <a href="enrichmentActivity.html?activityId=${enrich.activityId}">${enrich.activityName}</a>
                   </td>
                   <td>${enrich.activityId}</td>
                   <td>${enrich.description}</td>
                   <td>${enrich.keeperRating}</td>
                   <td>${enrich.isComplete}</td>
                   <td>
                       <a href="habitat.html?habitatId=${enrich.habitatId}">${enrich.habitatId}</a>
                   </td>
                   <td>${enrich.enrichmentId}</td>
                   <td>
                   <label class="container">
                   <input type="checkbox" ${enrich.onHabitat ? 'checked' : ''} disabled>
                   <span class="checkmark"></span>
                   </label>
                   </td>
               </tr>`;
        }

        return enrichHtml;
    }

}
