# Design Doc 
## Animal Enrichment Tracker Service

## 1. Problem Statement
Enrichment activities are essential for the well-being of animals at the St. Louis Zoo, mandated by the zoo's policy to provide at least two activities per week for each animal. The responsibility falls on teams of zookeepers to administer these activities and assess their effectiveness based on a current rating system.
To streamline this process, the proposed Animal Enrichment Tracker Service aims to automate activity tracking for each animal. This service will empower zookeepers to efficiently monitor animals, select appropriate enrichments, record enrichment activities administered, and evaluate each activity's impact through a rating system.

## 2. Use Cases
- U1. As a user, I would like the ability to add a habitat to the system.
- U2. As a user, I would like the ability to remove a habitat from the system.
- U3. As a user, I would like the ability to update a habitat.
- U4. As a user, I would like the ability to add an animal to a habitat.
- U5. As a user, I would like the ability to remove an animal from a habitat.
- U6. As a user, I would like the ability to view all animals in a habitat.
- U7. As a user, I would like the ability to add an enrichment activity to a habitat.
- U8. As a user, I would like the ability to remove an enrichment activity from a habitat.
- U9. As a user, I would like the ability to update an enrichment activity.
- U10. As a user, I would like the ability to view all enrichments for a habitat.
- U11. As a user, I would like the ability to view all enrichment activities sorted by date administered.
- U12. As a user, I would like the ability to view all enrichment activities sorted by ratings and habitat.
- [STRETCH GOALS]
- U13. As a user, I would like the ability to add an animal object with specific attributes (more than just a string name) to a habitat.
- U14. As a user, I would like the ability to remove an animal object with specific attributes (more than just a string name) from a habitat.
- U15. As a user, I would like the ability to update an animal object with specific attributes (more than just a string name) in a habitat.
- U16. As a user, I would like the ability to view all animal objects in a habitat.

## 4. Project Scope

### 4.1. In Scope

- Creating, retrieving, and updating a habitat
- Adding to and retrieving a saved habitat’s list of enrichments
- Removing from a saved habitat’s list of enrichments
- Adding to and retrieving a saved habitat’s list of animals
- Removing from a saved habitat’s list of animals

### 4.2. Out of Scope

- Keeping track of the number of enrichment activities completed in a given week, and clearing that at the start of every week.
- Scheduling enrichment activities for specific habitats in advance for a team of keepers to access.
- Giving managers of keeper teams the ability to accept/deny scheduled enrichments posted by a member of the keeper team.
- Giving multiple keepers access to add/remove enrichment activities to habitats they work on instead of just manager per habitat.

## 5. Proposed Architecture Overview

- This initial iteration will provide the minimum lovable product (MLP) including creating, retrieving, and updating a habitat, as well as adding to and retrieving a saved habitat’s enrichment activities.
We will use API Gateway and Lambda to create ten endpoints (AddHabitat, RemoveHabitat, UpdateHabitat, viewHabitat, AddAnimalToHabitat, RemoveAnimalFromHabitat, AddEnrichment, RemoveEnrichment, UpdateEnrichment, viewEnrichment) that will handle the creation, update, and retrieval of habitats and enrichments to satisfy our requirements.

- We will store newly created enrichment activities and habitats in DynamoDB. For simpler enrichment retrieval, we will store the list of enrichment activities in a given habitat directly in the habitats table.
Animal Enrichment Tracker Service will also provide a web interface for users to manage habitats and enrichment activities. A main page providing a list view of all the keeper’s habitats will let them add/remove habitats. This will link off to pages per-habitat to update metadata and add/remove animals and/or enrichment activities.


## 6. API

### 6.1. Public Models

// HabitatModel

- String habitatId;
- String habitatName;
- List<String> species;
- String keeperManagerId;
- int totalAnimals;
- List<String> animalsInHabitat;
- List<String> acceptableEnrichmentIds;
- List<Enrichment> completedEnrichments;

// EnrichmentModel

- String enrichmentId;
- String name;
- Integer keeperRating;
- String description;
- LocalDate dateCompleted;

### 6.2. AddHabitat Endpoint

- Accepts POST requests to /habitats
- Accepts data to create a new habitat(habitatName, species, list of Animals). Returns the new habitat, including a unique habitat Id assigned by the Animal Enrichment Tracker Service.
- For security concerns, we will validate the provided habitat and species name does not contain any invalid characters: " ' \
   - If the habitat or species name contains any of the invalid characters, will throw an InvalidCharacterException.

### 6.3. RemoveHabitat Endpoint

- Accepts DELETE requests to /habitats/:habitatId
- accepts a habitat to be removed, habitat is specified by habitatId
   - will throw HabitatNotFoundException if the given habitat id is not found

### 6.4. UpdateHabitat Endpoint

- Accepts PUT requests to /habitats/:habitatId
- Accepts data to update a habitat including an updated habitat name and updated species name associated with the habitat. Returns the updated habitat.
   - If the habitatId is not found, will throw a HabitatNotFoundException
   - For security concerns, we will validate the provided habitat and species name does not contain any invalid characters: " ' \
      - If the habitat or species name contains any of the invalid characters, will throw an InvalidCharacterException.

### 6.5. ViewHabitat Endpoint

- Accepts GET requests to /habitats/:habitatId
- accepts a habitat id and returns corresponding HabitatModel
   - will throw HabitatNotFoundException if the given habitatId is not found

### 6.6. AddAnimalToHabitat Endpoint

- Accepts PUT requests to /habitats/:habitatId/animals
- Accepts a habitatId and an animal to be added.
   - If the habitat is not found, will throw a HabitatNotFoundException

### 6.7. RemoveAnimalFromHabitat Endpoint

- Accepts PUT requests to /habitats/:habitatId/animals
- Accepts a habitatId and an animal to be removed.
   - If the habitat is not found, will throw a HabitatNotFoundException

### 6.8. AddEnrichment Endpoint

- Accepts POST requests to /habitats/:habitatId/enrichments
- Accepts a habitatId and an enrichment activity to be added, enrichment is specified by enrichmentId.
   - If the habitat is not found, will throw a HabitatNotFoundException
   - If the enrichment is not found, will throw an EnrichmentActivityNotFoundException
   - If the enrichment is not acceptable for the habitat, will throw an UnsuitableEnrichmentForHabitatException

### 6.9. UpdateEnrichment Endpoint

- Accepts POST requests to /habitats/:habitatId/enrichments
- Accepts data to update an enrichment activity(keeperRating), a habitatId and an enrichment activity to be updated, enrichment is specified by enrichmentId.
   - If the habitat is not found, will throw a HabitatNotFoundException
   - If the enrichment is not found, will throw an EnrichmentActivityNotFoundException

### 6.10. ViewHabitatsforUser Endpoint

- Accepts GET requests to /habitats/:keeperManagerId
- Accepts a keeperManagerId and returns a list of Habitats created by that manager.
   - If the given manager has not created any habitats, an empty list will be returned


### 6.11. ViewEnrichmentsforHabitat Endpoint
- Accepts GET requests to /habitats/:habitatId/enrichments
- Accepts a habitatId and returns a list of corresponding EnrichmentModels
   - If the habitat is not found, will throw a HabitatNotFoundException

## 7. Tables

### 7.1. Habitats

- habitatId // partition key, string 
- habitatName // string 
- species // list
- keeperManagerId // sort key, string (keeperManagerId-habitatId-index) 
- totalAnimals // number 
- animalsInHabitat // list 
- acceptableEnrichmentIds // list 
- completedEnrichments // list

   * keeperManagerId-habitatId-index includes ALL attributes

### 7.2. Enrichments

- enrichmentId // partition key, string 
- name // string  
- keeperRating // number 
- description // string  
- dateCompleted // string

## 8. Link to Google doc with images
[FULL DESIGN DOC](https://docs.google.com/document/d/1qD5lhMVNko6C9FZJOiNMof6X5cuzQFkSKtgWBlDoVaY/edit)

