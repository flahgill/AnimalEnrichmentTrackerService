import AnimalEnrichmentTrackerClient from '../api/animalEnrichmentTrackerClient';
import SearchEnrichments from './searchEnrichments.js';
import ViewAcceptableIds from './viewAcceptableIds.js';

const initializeAcceptIds = async () => {
    const client = new AnimalEnrichmentTrackerClient();
    const searchEnrichments = new SearchEnrichments(client);
    searchEnrichments.mount();
    const viewAcceptableIds = new ViewAcceptableIds(client);
    viewAcceptableIds.mount();
}

window.addEventListener('DOMContentLoaded', initializeAcceptIds);