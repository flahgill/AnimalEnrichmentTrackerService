import AnimalEnrichmentTrackerClient from '../api/animalEnrichmentTrackerClient';
import BindingClass from "../util/bindingClass";
import Header from '../components/header';
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

        this.bindClassMethods(['mount', 'toggleComplete', 'displayActivitiesResults', 'getHTMLForCompleteResults',
         'removeActivity', 'reAddActivity', 'redirectToUpdateActivity'], this);

        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.dataStore.addChangeListener(this.displayActivitiesResults);
        this.header = new Header(this.dataStore);
        console.log("ViewAllEnrichmentActivities constructor");
    }

    /**
     * Load the AnimalEnrichmentTrackerClient.
     */
    mount() {
        document.getElementById('toggle-complete-btn').addEventListener('click', this.toggleComplete);
        document.getElementById('all-activities').addEventListener("click", this.removeActivity);
        document.getElementById('all-activities').addEventListener("click", this.reAddActivity);
        document.getElementById('all-activities').addEventListener("click", this.redirectToUpdateActivity);

        this.client = new AnimalEnrichmentTrackerClient();

        this.toggleComplete();
        this.header.addHeaderToPage();

        document.getElementById('ok-button').addEventListener("click", this.closeModal);
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

        let enrichHtml = '<table id="enrichments-table"><tr><th>Date Completed</th><th>Activity</th><th>Description</th><th>Enrichment Id</th><th>Rating</th><th>Activity Id</th><th>Completed</th><th>Habitat Id</th><th>Currently On Habitat</th><th>Update Activity</th><th>Restore To Habitat</th><th>Delete Permanently</th></tr>';
        let enrich;
        for (enrich of completeResults) {
            enrichHtml += `
               <tr id="${enrich.activityId + enrich.habitatId}">
                   <td>${enrich.dateCompleted}</td>
                   <td>
                       <a href="enrichmentActivity.html?activityId=${enrich.activityId}">${enrich.activityName}</a>
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
                   <td><button data-activity-id="${enrich.activityId}" data-habitat-id="${enrich.habitatId}" class="button update-activity">Update</button></td>
                   <td><button data-activity-id="${enrich.activityId}" data-habitat-id="${enrich.habitatId}" class="button readd-activity">Restore</button></td>
                   <td><button data-activity-id="${enrich.activityId}" class="button remove-activity">Delete</button></td>
               </tr>`;
        }

        return enrichHtml;
    }

    /**
    * when delete button is clicked, deletes activity permanently
    */
    async removeActivity(e) {
        const removeButton = e.target;

        if (!removeButton.classList.contains('remove-activity')) {
            return;
        }

        const activityId = removeButton.dataset.activityId;
        const rowId = activityId + removeButton.closest('tr').querySelector('a[href*="habitat.html"]').href.split('habitatId=')[1];

        removeButton.innerText = "DELETING..."

        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        let errorOccured = false;
        const updatedActivities = await this.client.removeEnrichmentActivity(activityId, (error) => {
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
            errorOccured = true;
            this.showErrorModal(error.message);
            removeButton.innerText = "Delete";
        });

        if (!errorOccured) {
            document.getElementById(rowId).remove();
        }
    }

    /**
    * when re-add button is clicked, re-establishes activity to habitat
    */
    async reAddActivity(e) {
        const reAddButton = e.target;
        if (!reAddButton.classList.contains('readd-activity')) {
            return;
        }

        const activityId = reAddButton.dataset.activityId;
        const habitatId = reAddButton.dataset.habitatId;
        console.log(activityId);
        console.log(habitatId);

        reAddButton.innerText = "Adding...";

        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        let errorOccured = false;
        const updatedActivities = await this.client.reAddActivityToHabitat(habitatId, activityId, (error) => {
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
            errorOccured = true;
            this.showErrorModal(error.message);
            reAddButton.innerText = "Restore";
        });

        if (!errorOccured) {
            window.location.href = `/viewHabitatEnrichments.html?habitatId=${habitatId}`;
        }
    }

    /**
    * When the update button is clicked, redirects to update activity page.
    */
    async redirectToUpdateActivity(e) {
        const updateButton = e.target;
        if (!updateButton.classList.contains("update-activity")) {
            return;
        }

        updateButton.innerText = "Loading...";

        if (updateButton != null) {
            window.location.href = `/updateHabitatEnrichment.html?habitatId=${updateButton.dataset.habitatId}&activityId=${updateButton.dataset.activityId}`;
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
    const viewAllEnrichmentActivities = new ViewAllEnrichmentActivities();
    viewAllEnrichmentActivities.mount();
};

window.addEventListener('DOMContentLoaded', main);
