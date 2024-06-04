import AnimalEnrichmentTrackerClient from '../api/animalEnrichmentTrackerClient';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";


const COMPLETE_CRITERIA_KEY = 'complete-criteria';
const COMPLETE_RESULTS_KEY = 'complete-results';
const EMPTY_DATASTORE_STATE = {
    [COMPLETE_CRITERIA_KEY]: 'complete',
    [COMPLETE_RESULTS_KEY]: [],
};


/**
 * Logic needed for the view all enrichment activities page of the website.
 */
class ViewAllEnrichmentActivities extends BindingClass {
    constructor() {
        super();

        this.bindClassMethods(['mount', 'toggleComplete', 'displayActivitiesResults', 'getHTMLForCompleteResults'], this);

        // Create a new datastore with an initial "empty" state.
        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.dataStore.addChangeListener(this.displayActivitiesResults);
        console.log("ViewAllEnrichmentActivities constructor");
    }

    /**
     * Load the AnimalEnrichmentTrackerClient.
     */
    mount() {
        document.getElementById('toggle-complete-btn').addEventListener('click', this.toggleComplete);

        this.client = new AnimalEnrichmentTrackerClient();

        this.toggleComplete();
    }

    /**
     * Toggle the complete criteria between 'complete' and 'incomplete',
     * then perform the filter and update the datastore with the results.
     * @param evt The "event" object representing the user-initiated event that triggered this method.
     */
    async toggleComplete(evt) {
        if (evt) {
            evt.preventDefault();
        }

        const currentCriteria = this.dataStore.get(COMPLETE_CRITERIA_KEY);
        const newCriteria = currentCriteria === 'complete' ? 'incomplete' : 'complete';

        const results = await this.client.getAllEnrichmentActivities(newCriteria);

        this.dataStore.setState({
            [COMPLETE_CRITERIA_KEY]: newCriteria,
            [COMPLETE_RESULTS_KEY]: results,
        });
    }

    /**
     * Pulls results from the datastore and displays them on the HTML page.
     */
    displayActivitiesResults() {
        const completeCriteria = this.dataStore.get(COMPLETE_CRITERIA_KEY);
        const completeResults = this.dataStore.get(COMPLETE_RESULTS_KEY);

        const completeResultsContainer = document.getElementById('complete-results-container');
        const completeResultsDisplay = document.getElementById('complete-results-display');
        const completeCriteriaDisplay = document.getElementById('complete-criteria-display');

        completeCriteriaDisplay.textContent = `Showing ${completeCriteria} activities`;

        if (completeResults.length === 0) {
            completeResultsContainer.classList.add('hidden');
            completeResultsDisplay.innerHTML = '<h4>No Activities Found</h4>';
        } else {
            completeResultsContainer.classList.remove('hidden');
            completeResultsDisplay.innerHTML = this.getHTMLForCompleteResults(completeResults);
        }
    }

    /**
     * Create appropriate HTML for displaying completeResults on the page.
     * @param completeResults An array of habitats objects to be displayed on the page.
     * @returns A string of HTML suitable for being dropped on the page.
     */
    getHTMLForCompleteResults(completeResults) {
        if (completeResults.length === 0) {
            return '<h4>No Activities found</h4>';
        }

        let enrichHtml = '<table id="enrichments-table"><tr><th>Date Completed</th><th>Activity</th><th>Description</th><th>Enrichment Id</th><th>Rating</th><th>Activity Id</th><th>Completed</th><th>Habitat Id</th><th>Currently On Habitat</th></tr>';
        let enrich;
        for (enrich of completeResults) {
            enrichHtml += `
               <tr id="${enrich.activityId + enrich.habitatId}">
                   <td>${enrich.dateCompleted}</td>
                   <td>
                       <a href="enrichmentActivity.html?activityId=${enrich.activityId}">${enrich.name}</a>
                   </td>
                   <td>${enrich.description}</td>
                   <td>${enrich.enrichmentId}</td>
                   <td>${enrich.keeperRating}</td>
                   <td>${enrich.activityId}</td>
                   <td>${enrich.isComplete}</td>
                   <td>
                       <a href="habitat.html?habitatId=${enrich.habitatId}">${enrich.habitatId}</a>
                   </td>
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

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const viewAllEnrichmentActivities = new ViewAllEnrichmentActivities();
    viewAllEnrichmentActivities.mount();
};

window.addEventListener('DOMContentLoaded', main);