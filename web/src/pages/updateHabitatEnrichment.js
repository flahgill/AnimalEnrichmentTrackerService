import AnimalEnrichmentTrackerClient from '../api/animalEnrichmentTrackerClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

/**
 * Logic needed for the update activity page of the website.
 */
class UpdateHabitatEnrichment extends BindingClass {

    urlParams = new URLSearchParams(window.location.search);
    habitatId = this.urlParams.get('habitatId');
    activityId = this.urlParams.get('activityId');

    constructor() {
        super();
        this.bindClassMethods(['mount', 'clientLoaded', 'submit', 'addEnrichmentToPage', 'redirectToHabitatEnrichments'], this);
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.addEnrichmentToPage);
        this.header = new Header(this.dataStore);
        console.log("updateHabitatEnrichment constructor");
    }

    /**
     * Add the header to the page and load the AnimalEnrichmentTrackerClient.
     */
    mount() {
        document.getElementById('update-activity').addEventListener('click', this.submit);

        this.header.addHeaderToPage();

        this.client = new AnimalEnrichmentTrackerClient();
        this.clientLoaded();
    }

    /**
     * Once the client is loaded, get the habitat metadata.
     */
    async clientLoaded() {
        document.getElementById('activity-name').innerText = "Loading Activity ...";

        const enrichmentActivity = await this.client.getEnrichmentActivity(this.activityId);
        this.dataStore.set('enrichmentActivity', enrichmentActivity);
    }

    /**
     * When the habitat is updated in the datastore, update the habitat metadata on the page.
     */
    addEnrichmentToPage() {
        const activity = this.dataStore.get('enrichmentActivity');
        if (activity == null) {
            return;
        }

        document.getElementById('activity-name').innerText = activity.name;
        document.getElementById('activity-description').innerText = activity.description;
        document.getElementById('activity-date').innerText = activity.dateCompleted;
        document.getElementById('activity-complete').innerText = activity.isComplete;
        document.getElementById('activity-rating').innerText = activity.keeperRating;
        document.getElementById('activity-id').innerText = this.activityId;
    }

    /**
     * Method to run when the update habitat submit button is pressed. Call the AnimalEnrichmentTrackerClient to update the
     * habitat.
     */
    async submit(evt) {
        evt.preventDefault();

        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        const updateButton = document.getElementById('update-activity');
        const origButtonText = updateButton.innerText;
        updateButton.innerText = 'Loading...';

        const newDate = document.getElementById('date-completed').value;
        const newComplete = document.getElementById('is-complete').value;
        const newRating = document.getElementById('keeper-rating').value;

        const activity = this.dataStore.get('enrichmentActivity');
        const habitatId = activity.habitatId;


        const enrichmentActivity = await this.client.updateHabitatEnrichmentActivity(habitatId, this.activityId, newRating, newDate, newComplete, (error) => {
            updateButton.innerText = origButtonText;
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
        });

        this.dataStore.set('enrichment-activity', enrichmentActivity);
        this.redirectToHabitatEnrichments(this.activityId);
    }

    /**
     * When the habitat is updated in the datastore, redirect back to the habitat page.
     */
    redirectToHabitatEnrichments(activityId) {
        window.location.href = `/enrichmentActivity.html?activityId=${activityId}`;
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const updateHabitatEnrichment = new UpdateHabitatEnrichment();
    updateHabitatEnrichment.mount();
};

window.addEventListener('DOMContentLoaded', main);
