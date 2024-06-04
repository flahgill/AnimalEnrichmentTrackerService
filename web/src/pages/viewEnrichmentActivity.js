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
        'removeActivity'], this);
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

        document.getElementById('activity-name').innerText = enrichmentActivity.name;
        document.getElementById('activity-description').innerText = enrichmentActivity.description;
        document.getElementById('activity-date').innerText = enrichmentActivity.dateCompleted;
        document.getElementById('activity-complete').innerText = enrichmentActivity.isComplete;
        document.getElementById('activity-rating').innerText = enrichmentActivity.keeperRating;
        document.getElementById('activity-id').innerText = enrichmentActivity.activityId;
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
//
//        try {
//            await this.client.removeEnrichmentActivity(activityId);
//
//            const reloadActivity = await this.client.getEnrichmentActivity(activityId);
//            if (!reloadActivity) {
//                window.location.href = 'index.html';
//            } else {
//                this.showErrorModal(`Error: ${error.message}`);
//                removeButton.innerText = "Delete Activity";
//            }
//        } catch (error) {
//            await this.showErrorModal(`Error: ${error.message}`);
//            removeButton.innerText = "Delete Activity"
//        }

        await this.client.removeEnrichmentActivity(activityId, (error) => {
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
            this.showErrorModal(error.message);
        });

        removeButton.innerText = "Delete Activity";
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