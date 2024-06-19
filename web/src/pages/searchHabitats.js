import AnimalEnrichmentTrackerClient from '../api/animalEnrichmentTrackerClient';
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

const SEARCH_CRITERIA_KEY = 'search-criteria';
const SEARCH_RESULTS_KEY = 'search-results';
const EMPTY_DATASTORE_STATE = {
    [SEARCH_CRITERIA_KEY]: '',
    [SEARCH_RESULTS_KEY]: [],
};

export default class SearchHabitats extends BindingClass {
    constructor(client) {
        super();
        this.client = client;

        this.bindClassMethods(['mount', 'search', 'displaySearchResults', 'getHTMLForSearchResults'], this);

        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.header = new Header(this.dataStore);
        this.dataStore.addChangeListener(this.displaySearchResults);
        console.log("searchHabitats constructor");
    }

    mount() {
        document.getElementById('search-habitats-form').addEventListener('submit', this.search);
        document.getElementById('search-btn').addEventListener('click', this.search);

        this.header.addHeaderToPage();
    }

    async search(evt) {
        evt.preventDefault();

        const searchCriteria = document.getElementById('search-criteria').value;
        const previousSearchCriteria = this.dataStore.get(SEARCH_CRITERIA_KEY);

        if (previousSearchCriteria === searchCriteria) {
            return;
        }

        document.getElementById('search-btn').innerText = "Loading...";

        if (searchCriteria) {
            const results = await this.client.search(searchCriteria);

            this.dataStore.setState({
                [SEARCH_CRITERIA_KEY]: searchCriteria,
                [SEARCH_RESULTS_KEY]: results,
            });
        } else {
            this.dataStore.setState(EMPTY_DATASTORE_STATE);
        }

        document.getElementById('search-btn').innerText = "Search";
    }

    displaySearchResults() {
        const searchCriteria = this.dataStore.get(SEARCH_CRITERIA_KEY);
        const searchResults = this.dataStore.get(SEARCH_RESULTS_KEY);

        const searchResultsContainer = document.getElementById('search-results-container');
        const searchCriteriaDisplay = document.getElementById('search-criteria-display');
        const searchResultsDisplay = document.getElementById('search-results-display');

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

    getHTMLForSearchResults(searchResults) {
        if (searchResults.length === 0) {
            return '<h4>No results found</h4>';
        }

        let html = '<table id="habitats-table"><tr><th>Habitat</th><th>Habitat ID</th><th>Species</th><th>Total Animals</th><th>Animals</th><th>Status</th><th>Keeper</th><th>Keeper Email</th></tr>';
        for (const res of searchResults) {
            html += `
            <tr>
                <td>
                    <a href="habitat.html?habitatId=${res.habitatId}">${res.habitatName}</a>
                </td>
                <td>${res.habitatId}</td>
                <td>${res.species?.join(', ')}</td>
                <td>${res.totalAnimals}</td>
                <td>${res.animalsInHabitat?.join(', ')}</td>
                <td>${res.isActive}</td>
                <td>${res.keeperName}</td>
                <td>${res.keeperManagerId}</td>
            </tr>`;
        }
        html += '</table>';

        return html;
    }
}