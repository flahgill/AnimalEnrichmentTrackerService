import AnimalEnrichmentTrackerClient from '../api/animalEnrichmentTrackerClient';
import SearchEnrichments from './searchEnrichments.js';
import ViewHabitatEnrichments from './viewHabitatEnrichments.js';

const habitatEAsInitialize = async () => {
    const client = new AnimalEnrichmentTrackerClient();
    const searchEnrichments = new SearchEnrichments(client);
    searchEnrichments.mount();
    const viewHabitatEnrichments = new ViewHabitatEnrichments(client);
    viewHabitatEnrichments.mount();
}

window.addEventListener('DOMContentLoaded', habitatEAsInitialize);