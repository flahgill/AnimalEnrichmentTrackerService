# Design Doc 
## Animal Enrichment Tracker Service (SafariJoy)

## 1. Problem Statement
Enrichment activities are essential for the well-being of animals at the St. Louis Zoo, mandated by the zoo's policy to provide at least two activities per week for each animal. The responsibility falls on teams of zookeepers to administer these activities and assess their effectiveness based on a current rating system.
To streamline this process, the proposed Animal Enrichment Tracker Service (SafariJoy) aims to automate activity tracking for each animal. This service will empower zookeepers to efficiently monitor animals, select appropriate enrichments, record enrichment activities administered, and evaluate each activity's impact through a rating system.

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
- U13. As a user, I would like the ability to search for habitats by habitat name, species, or animals in the habitat.
- U14. As a user, I would like the ability to search for enrichments by name.
- [STRETCH GOALS]
- U15. As a user, I would like the ability to add an animal object with specific attributes (more than just a string name) to a habitat.
- U16. As a user, I would like the ability to remove an animal object with specific attributes (more than just a string name) from a habitat.
- U17. As a user, I would like the ability to update an animal object with specific attributes (more than just a string name) in a habitat.
- U18. As a user, I would like the ability to view all animal objects in a habitat.

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
- We will use API Gateway and Lambda to create fifteen endpoints (AddHabitat, RemoveHabitat, UpdateHabitat, ViewHabitat, AddAnimalToHabitat, RemoveAnimalFromHabitat, AddEnrichmentToHabitat, RemoveEnrichmentFromHabitat, UpdateHabitatEnrichment, viewUserHabitats, ViewHabitatEnrichments, SearchHabitats, SearchEnrichments, DeactivateHabitat, ViewInactiveHabitats) that will handle the creation, update, and retrieval of habitats and enrichments to satisfy our requirements.
- We will store newly created enrichment activities and habitats in DynamoDB. For simpler enrichment retrieval, we will store the list of enrichment activities in a given habitat directly in the habitats table.
Animal Enrichment Tracker Service will also provide a web interface for users to manage habitats and enrichment activities. A main page providing a list view of all the keeper’s habitats will let them add/remove habitats. This will link off to pages per-habitat to update metadata and add/remove animals and/or enrichment activities.


## 6. API

### 6.1. Public Models

// HabitatModel

- String habitatId;
- String habitatName;
- String isActive;
- List<String> species;
- String keeperManagerName;
- String keeperManagerId;
- Int totalAnimals;
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

### 6.3. RemoveHabitat Endpoint (Hard Delete)

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
   - If the habitat is not found, will throw a HabitatNotFoundException

### 6.6. AddAnimalToHabitat Endpoint

- Accepts PUT requests to /habitats/:habitatId/animals
- Accepts a habitatId and an animal to be added.
   - If the habitat is not found, will throw a HabitatNotFoundException
   - If the animal to be added is already present within the habitat, will throw a DuplicateAnimalException
  - For security concerns, we will validate the provided animal name does not contain any invalid characters: " ' \
    - If the animal name contains any of the invalid characters, will throw an InvalidCharacterException.

### 6.7. RemoveAnimalFromHabitat Endpoint

- Accepts PUT requests to /habitats/:habitatId/animals
- Accepts a habitatId and an animal to be removed.
   - If the habitat is not found, will throw a HabitatNotFoundException
   - If the animal is not found, will throw an AnimalNotFoundException

### 6.8. AddEnrichmentToHabitat Endpoint

- Accepts POST requests to /habitats/:habitatId/enrichments
- Accepts a habitatId and an enrichment activity to be added, enrichment is specified by enrichmentId.
   - If the habitat is not found, will throw a HabitatNotFoundException
   - If the enrichment is not found, will throw an EnrichmentActivityNotFoundException
   - If the enrichment is not acceptable for the habitat, will throw an UnsuitableEnrichmentForHabitatException

### 6.9. UpdateHabitatEnrichment Endpoint

- Accepts POST requests to /habitats/:habitatId/enrichments
- Accepts data to update an enrichment activity(keeperRating), a habitatId and an enrichment activity to be updated, enrichment is specified by enrichmentId.
   - If the habitat is not found, will throw a HabitatNotFoundException
   - If the enrichment is not found, will throw an EnrichmentActivityNotFoundException

### 6.10. RemoveEnrichmentFromHabitat Endpoint

- Accepts DELETE requests to /habitats/:habitatId/enrichments
- Accepts data to remove an enrichment activity, enrichment is specified by enrichmentId.
  - If the habitat is not found, will throw a HabitatNotFoundException
  - If the enrichment is not found, will throw an EnrichmentActivityNotFoundException

### 6.11. ViewHabitatsforUser Endpoint

- Accepts GET requests to /habitats/:keeperManagerId
- Accepts a keeperManagerId and returns a list of Habitats created by that manager.
   - If the given manager has not created any habitats, an empty list will be returned

### 6.12. ViewEnrichmentsforHabitat Endpoint

- Accepts GET requests to /habitats/:habitatId/enrichments
- Accepts a habitatId and returns a list of corresponding EnrichmentModels
   - If the habitat is not found, will throw a HabitatNotFoundException

### 6.13. SearchHabitats Endpoint

- Accepts GET requests to /habitats/search
- Accepts a search criteria and returns a list of associated Habitats

### 6.14 DeactivateHabitat Endpoint (Soft Delete)

- Accepts PUT requests to /habitats/:habitatId
- Accepts data to update a habitat including the active status associated with the habitat. Returns the updated habitat.
- If the habitatId is not found, will throw a HabitatNotFoundException.

### 6.15 ViewAllHabitats Endpoint

- Accepts GET requests to /habitats
- returns a list of Habitats, and user will input the activity status they want to view.

## 7. Tables

### 7.1. Habitats

- habitatId // partition key, string
- keeperManagerId // string (keeperManagerId-habitatId-index)
- keeperManagerName // string
- habitatName // string
- species // list
- isActive // string (habitatId-isActive-index)
- totalAnimals // number
- animalsInHabitat // list
- acceptableEnrichmentIds // list (habitatId-acceptableEnrichmentsIds-index)
- completedEnrichments // list

   * keeperManagerId-habitatId-index includes ALL attributes
   * habitatId-isActive-index includes ALL attributes
   * habitatId-acceptableEnrichmentIds-index KEYS_ONLY

### 7.2. Enrichments

- enrichmentId // partition key, string 
- name // string  
- keeperRating // number (keeperRating-enrichmentId-index)
- description // string  
- dateCompleted // string

  * keeperRating-enrichmentId-index includes ALL attributes

## 8. Link to Google doc with images
[FULL DESIGN DOC](https://docs.google.com/document/d/1qD5lhMVNko6C9FZJOiNMof6X5cuzQFkSKtgWBlDoVaY/edit)


