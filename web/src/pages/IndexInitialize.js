import AnimalEnrichmentTrackerClient from '../api/animalEnrichmentTrackerClient';
import SearchHabitats from './searchHabitats.js';
import SearchEnrichmentActivities from './SearchEnrichmentActivities.js';
import SearchEnrichments from './searchEnrichments.js';
import ViewUserHabitats from './viewUserHabitats.js';
import SearchAnimals from './searchAnimals.js';


const initialize = async () => {
    const client = new AnimalEnrichmentTrackerClient();
    const searchEnrichmentActivities = new SearchEnrichmentActivities(client);
    searchEnrichmentActivities.mount();
    const searchHabitats = new SearchHabitats(client);
    searchHabitats.mount();
    const searchEnrichments = new SearchEnrichments(client);
    searchEnrichments.mount();
    const viewUserHabitats = new ViewUserHabitats(client);
    viewUserHabitats.mount();
    const searchAnimals = new SearchAnimals(client);
    searchAnimals.mount();
};

window.addEventListener('DOMContentLoaded', initialize);