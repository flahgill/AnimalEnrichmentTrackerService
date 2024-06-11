import AnimalEnrichmentTrackerClient from '../api/animalEnrichmentTrackerClient';
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
 * Logic needed for the view enrichmentActivity page of the website.
 */
class ViewHabitat extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['clientLoaded', 'mount', 'addActivityToPage', 'redirectToUpdateActivity',
        'removeActivity', 'redirectToHabitat'], this);
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.addActivityToPage);
        this.header = new Header(this.dataStore);
        console.log("viewEnrichmentActivity constructor");
    }

    /**
     * Once the client is loaded, get the EnrichmentActivity metadata.
     */
    async clientLoaded() {
        const urlParams = new URLSearchParams(window.location.search);
        const activityId = urlParams.get('activityId');
        document.getElementById('activity-name').innerText = "Loading Activity ...";
        const enrichmentActivity = await this.client.getEnrichmentActivity(activityId);
        this.dataStore.set('enrichmentActivity', enrichmentActivity);
    }

    /**
     * Add the header to the page and load the AnimalEnrichmentTrackerClient.
     */
    mount() {
        this.header.addHeaderToPage();
        document.getElementById('update-activity').addEventListener("click", this.redirectToUpdateActivity);
        document.getElementById('remove-activity').addEventListener("click", this.removeActivity);
        document.getElementById('view-habitat').addEventListener("click", this.redirectToHabitat);

        this.client = new AnimalEnrichmentTrackerClient();
        this.clientLoaded();

        document.getElementById('ok-button').addEventListener("click", this.closeModal);
    }

    /**
     * When the habitat is updated in the datastore, update the habitat metadata on the page.
     */
    addActivityToPage() {
        const enrichmentActivity = this.dataStore.get('enrichmentActivity');
        if (enrichmentActivity == null) {
            return;
        }

        document.getElementById('activity-name').innerText = enrichmentActivity.activityName;
        document.getElementById('activity-description').innerText = enrichmentActivity.description;
        document.getElementById('activity-date').innerText = enrichmentActivity.dateCompleted;
        document.getElementById('activity-complete').innerText = enrichmentActivity.isComplete;
        document.getElementById('activity-rating').innerText = enrichmentActivity.keeperRating;
        document.getElementById('activity-id').innerText = enrichmentActivity.activityId;
        document.getElementById('habitat-id').innerText = enrichmentActivity.habitatId;
    }

    /**
    * when the update button is clicked, redirects to update habitat page.
    */
    async redirectToUpdateActivity(e) {
        const activity = this.dataStore.get('enrichmentActivity');
        const activityId = activity.activityId;
        const habitatId = activity.habitatId;
        const updateButton = e.target;

        updateButton.innerText = "Loading...";

        if (updateButton != null) {
            window.location.href = `/updateHabitatEnrichment.html?habitatId=${habitatId}&activityId=${activityId}`;
        }
    }

    async removeActivity(e) {
        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        const activity = this.dataStore.get('enrichmentActivity');
        const activityId = activity.activityId;

        const removeButton = e.target;
        removeButton.innerText = "Removing...";

        let errorOccurred = false;
        await this.client.removeEnrichmentActivity(activityId, (error) => {
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
            errorOccurred = true;
            this.showErrorModal(error.message);
        });

        if (!errorOccurred) {
            removeButton.innerText = "Delete Activity";
            this.showErrorModal("Activity Deleted");
            window.location.href = `/index.html`;
        }
    }

    async redirectToHabitat(e) {
        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        const activity = this.dataStore.get('enrichmentActivity');
        const habitatId = activity.habitatId;

        const habitatButton = e.target;
        habitatButton.innerText = "Redirecting...";

        window.location.href = `/habitat.html?habitatId=${habitatId}`;
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
    const viewHabitat = new ViewHabitat();
    viewHabitat.mount();
};

window.addEventListener('DOMContentLoaded', main);