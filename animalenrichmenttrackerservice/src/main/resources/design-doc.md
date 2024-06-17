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
- U12. As a user, I would like the ability to search for habitats by habitat name, species, or animals in the habitat.
- U13. As a user, I would like the ability to search for enrichments by name.
- [STRETCH GOALS]
- U14. As a user, I would like the ability to add an animal object with specific attributes (more than just a string name) to a habitat.
- U15. As a user, I would like the ability to remove an animal object with specific attributes (more than just a string name) from a habitat.
- U16. As a user, I would like the ability to update an animal object with specific attributes (more than just a string name) in a habitat.
- U17. As a user, I would like the ability to view all animal objects in a habitat.
- U18. As a user, I would like the ability to view all enrichment activities sorted by ratings and habitat.


## 4. Project Scope

### 4.1. In Scope

- Creating, retrieving, and updating a habitat
- Adding to and retrieving a saved habitat’s list of enrichments
- Removing from a saved habitat’s list of enrichments
- Adding to and retrieving a saved habitat’s list of animals
- Removing from a saved habitat’s list of animals

### 4.2. Out of Scope

- Keeping track of the number of enrichment activities completed in a given week, and clearing that at the start of every week.
- Giving managers of keeper teams the ability to accept/deny scheduled enrichments posted by a member of the keeper team.
- Giving multiple keepers access to add/remove enrichment activities to habitats they work on instead of just manager per habitat.

## 5. Proposed Architecture Overview

- This initial iteration will provide the minimum lovable product (MLP) including creating, retrieving, and updating a habitat, as well as adding to and retrieving a saved habitat’s enrichment activities.
- We will use API Gateway and Lambda to create multiple endpoints (AddHabitat, RemoveHabitat, UpdateHabitat, ViewHabitat, AddAnimalToHabitat, RemoveAnimalFromHabitat, AddEnrichmentActivityToHabitat, RemoveEnrichmentActivityFromHabitat, UpdateHabitatEnrichmentActivity, ViewUserHabitats, ViewHabitatEnrichmentActivities, SearchHabitats, ViewAllHabitats, RemoveEnrichmentActivity, SearchEnrichmentActivities, SearchEnrichments, ViewAllEnrichmentActivities, ViewAcceptableEnrichmentIds, AddAcceptableId, RemoveAcceptableId, ReAddEnrichmentActivityToHabitat, RemoveAnimal, ViewAnimal, ViewSpeciesList, AddSpecies, RemoveSpecies, UpdateAnimal, ViewAllAnimals, SearchAnimals, UpdateAnimal, ViewHabitatAnimals, ViewEnrichmentActivity, ReactivateAnimal) that will handle the creation, update, and retrieval of habitats and enrichments to satisfy our requirements.
- We will store newly created enrichment activities and habitats in DynamoDB. For simpler enrichment retrieval, we will store the list of enrichment activities in a given habitat directly in the habitats table.
- Animal Enrichment Tracker Service will also provide a web interface for users to manage habitats and enrichment activities. A main page providing a list view of all the keeper’s habitats will let them add/remove habitats. This will link off to pages per-habitat to update metadata and add/remove animals and/or enrichment activities.


## 6. API

### 6.1. Public Models

// HabitatModel
- String habitatId;
- String habitatName;
- String isActive;
- List<String> species;
- String keeperManagerId;
- String keeperName;
- Int totalAnimals;
- List<String> animalsInHabitat;
- List<String> acceptableEnrichmentIds;
- List<Enrichment> completedEnrichments;

// EnrichmentModel
- String enrichmentId;
- String activityName;
- String description;

// EnrichmentActivityModel
- String activityId;
- String enrichmentId;
- String activityName;
- String keeperRating;
- String description;
- LocalDate dateCompleted;
- String habitatId;
- String isComplete;
- Boolean onHabitat;

// AnimalModel
- String animalId;
- String animalName;
- int age;
- String sex;
- String species;
- String isActive;
- String habitatId;
- Boolean onHabitat;


### 6.2. AddHabitat Endpoint

- Accepts POST requests to /habitats
- Accepts data to create a new habitat(habitatName, species). Returns the new habitat, including a unique habitat Id assigned by the Animal Enrichment Tracker Service.
- For security concerns, we will validate the provided habitat and species name does not contain any invalid characters: " ' \
   - If the habitat or species name contains any of the invalid characters, will throw an InvalidCharacterException.

### 6.3. RemoveHabitat Endpoint (Hard Delete)

- Accepts DELETE requests to /habitats/:habitatId
- accepts a habitat to be removed, habitat is specified by habitatId
   - If the given habitatId is not found, will throw HabitatNotFoundException
   - If the keeper removing the habitat is not the owner, will throw UserSecurityException

### 6.4. UpdateHabitat Endpoint

- Accepts PUT requests to /habitats/:habitatId
- Accepts data to update a habitat(name, species, active status(soft delete)). Returns the updated habitat.
   - If the habitatId is not found, will throw a HabitatNotFoundException
   - If the keeper updating the habitat is not the owner, will throw UserSecurityException
   - For security concerns, we will validate the provided habitat and species name does not contain any invalid characters: " ' \
      - If the habitat or species name contains any of the invalid characters, will throw an InvalidCharacterException.

### 6.5. ViewHabitat Endpoint

- Accepts GET requests to /habitats/:habitatId
- accepts a habitat id and returns corresponding HabitatModel
   - If the habitat is not found, will throw a HabitatNotFoundException

### 6.6. AddAnimalToHabitat Endpoint

- Accepts POST requests to /habitats/:habitatId/animals
- Accepts a habitatId and an animal to be added.
   - If the habitat is not found, will throw a HabitatNotFoundException
   - If the animal to be added is already present within the habitat, will throw a DuplicateAnimalException
   - If the keeper adding the animal is not the owner of the habitat, will throw UserSecurityException
  - For security concerns, we will validate the provided animal name does not contain any invalid characters: " ' \
    - If the animal name contains any of the invalid characters, will throw an InvalidCharacterException.

### 6.7. RemoveAnimalFromHabitat Endpoint

- Accepts DELETE requests to /habitats/:habitatId/animals
- Accepts a habitatId and an animal to be removed.
   - If the keeper removing the animal is not the owner of the habitat, will throw a UserSecurityException
   - If the habitat is not found, will throw a HabitatNotFoundException
   - If the animal is not found, will throw an AnimalNotFoundException

### 6.8. AddEnrichmentActivityToHabitat Endpoint

- Accepts POST requests to /habitats/:habitatId/enrichmentActivities
- Accepts a habitatId and an enrichment activity to be added, enrichment is specified by enrichmentId.
   - If the keeper adding the activity to the habitat is not the owner, will throw UserSecurityException
   - If the habitat is not found, will throw a HabitatNotFoundException
   - If the enrichment is not found, will throw an EnrichmentNotFoundException
   - If the enrichment is not acceptable for the habitat, will throw an UnsuitableEnrichmentForHabitatException

### 6.9. UpdateHabitatEnrichmentActivity Endpoint

- Accepts POST requests to /habitats/:habitatId/enrichmentActivities
- Accepts data to update an enrichment activity(keeperRating, dateCompleted, completion status), a habitatId and an enrichment activity to be updated, enrichment is specified by activityId.
   - If the keeper updating the activity is not the owner of the habitat it is on or was originally added to, will throw UserSecurityException
   - If the habitat is not found, will throw a HabitatNotFoundException
   - If the enrichment activity is not found, will throw an EnrichmentActivityNotFoundException

### 6.10. RemoveEnrichmentActivityFromHabitat Endpoint (Soft Delete)

- Accepts DELETE requests to /habitats/:habitatId/enrichmentActivities
- Accepts data to remove an enrichment activity, enrichment is specified by activityId.
  - If the keeper removing the activity from the habitat is not the owner, will throw UserSecurityException
  - If the habitat is not found, will throw a HabitatNotFoundException
  - If the enrichment activity is not found, will throw an EnrichmentActivityNotFoundException

### 6.11. ViewHabitatsforUser Endpoint

- Accepts GET requests to /habitats/:keeperManagerId
- Accepts a keeperManagerId and returns a list of Habitats created by that manager.
   - If the given manager has not created any habitats, an empty list will be returned

### 6.12. ViewHabitatEnrichments Endpoint

- Accepts GET requests to /habitats/:habitatId/enrichmentActivities
- Accepts a habitatId and returns a list of corresponding EnrichmentActivityModels
   - If the habitat is not found, will throw a HabitatNotFoundException
   - If the given habitat does not have any enrichment activities, an empty list will be returned

### 6.13. SearchHabitats Endpoint

- Accepts GET requests to /habitats/search
- Accepts a search criteria and returns a list of associated Habitats
  - If none of the habitats match the search criteria, an empty list will be returned

### 6.14 ViewAllHabitats Endpoint

- Accepts GET requests to /habitats
- Accepts habitat activity status and returns a list of HabitatModels that have that activity status

### 6.15 RemoveEnrichmentActivity Endpoint (Hard Delete)

- Accepts DELETE requests to /enrichmentActivities/:activityId
- Accepts data to remove an enrichment activity, activity is specified by activityId.
  - If the enrichmentActivity is not found, will thrown an EnrichmentActivityNotFoundException

### 6.16 SearchEnrichmentActivities Endpoint

- Accepts GET requests to /enrichmentActivities/search
- Accepts a search criteria and returns a list of associated EnrichmentActivityModels
  - If none of the enrichment activities match the search criteria, an empty list will be returned

### 6.17 SearchEnrichments Endpoint

- Accepts GET requests to /enrichments/search
- Accepts a search criteria and returns a list of associated EnrichmentModels
  - If none of the enrichment match the search criteria, an empty list will be returned

### 6.18 ViewAllEnrichmentActivities Endpoint

- Accepts GET requests to /enrichmentActivities
- Accepts activity completion status and returns a list of EnrichmentActivityModels that have that completion status

### 6.19 ViewAcceptableEnrichmentIds Endpoint

- Accepts GET requests to /habitats/:habitatId/acceptableIds
- Accepts a habitatId and returns a habitat's saved list of acceptableEnrichmentIds
  - If the given habitatId is not found, throws HabitatNotFoundException

### 6.20 AddAcceptableId Endpoint

- Accepts PUT requests to /habitats/:habitatId/acceptableIds
- Accepts a habitatId and an enrichmentId to add. Returns a habitat's saved list of acceptableEnrichmentIds
  - If the keeper adding the acceptableId to the habitat is not the owner, will throw UserSecurityException
  - If the given habitatId is not found, throws HabitatNotFoundException

### 6.21 RemoveAcceptableId Endpoint

- Accepts DELETE requests to /habitats/:habitatId/acceptableIds
- Accepts a habitatId and an enrichmentId to remove. Returns a habitat's saved list of acceptableEnrichmentIds
  - If the keeper removing the acceptableId from the habitat is not the owner, will throw UserSecurityException
  - If the given habitatId is not found, throws HabitatNotFoundException
  - If the acceptableId being removed is not in the habitat's saved list, will throw AcceptableIdNotFoundException

### 6.22 ReAddEnrichmentActivityToHabitat Endpoint

- Accepts PUT requests to /habitats/:habitatId/enrichmentActivities
- Accepts a habitatId and activityId to re-add to the habitat. Returns a list of completedEnrichments on habitat.
  - If the keeper reintroducing the activity is not the owner of the habitat, will throw UserSecurityException
  - If the enrichment activity is not found, will throw an EnrichmentActivityNotFoundException
  - If the habitatId is not found, will throw a HabitatNotFoundException.
  - If the activity is already on a habitat, will throw a EnrichmentActivityCurrentlyOnHabitatException.
  - If the habitatId does not match the habitatId that the activity was originally added to, will throw a IncompatibleHabitatIdException.

### 6.23. RemoveAnimal Endpoint (Hard Delete)

- Accepts DELETE requests to /animals/:animalId
- Accepts an animalId to be removed.
  - If the animal is not found, will throw an AnimalNotFoundException
  - If the animal is currently on a habitat, will throw an AnimalCurrentlyOnHabitatException

### 6.24. ViewAnimal Endpoint

- Accepts GET requests to /animals/:animalId
- Accepts an animalId used to retrieve animal
  - If the animal is not found, will throw an AnimalNotFoundException

### 6.25. ViewSpeciesList Endpoint

- Accepts GET requests to /habitats/:habitatId/species
- Accepts a habitatId and returns a list of species saved on the retrieved habitat.
  - If the habitat is not found, will throw an HabitatNotFoundException

### 6.26. AddSpecies Endpoint

- Accepts POST requests to /habitats/:habitatId/species
- Accepts a habitatId and a species to add, returns habitat’s updated list of species
  - If the habitat is not found, will throw an HabitatNotFoundException
  - If the species is already present in the habitat, will throw a DuplicateSpeciesException.
  - If the keeper adding the species is not the owner of the habitat, will throw an UserSecurityException.

### 6.27. RemoveSpecies Endpoint

- Accepts DELETE requests to /habitats/:habitatId/species
- Accepts a habitatId and a species to remove, returns habitat’s updated list of species
  - If the habitat is not found, will throw an HabitatNotFoundException
  - If the species is not present in the habitat, will throw a SpeciesNotFoundException.
  - If the keeper removing the species is not the owner of the habitat, it will throw an UserSecurityException.

### 6.28. ViewAllAnimals Endpoint

- Accepts GET requests to /animals
- Accepts active status and returns a list of AnimalModels that have that active status

### 6.29. SearchAnimals Endpoint

- Accepts GET requests to /animals/search
- Accepts a search criteria and returns a list of associated Animals

### 6.30. UpdateAnimal Endpoint

- Accepts PUT requests to /animals/:animalId
- Accepts an animalId, and user inputs to update the animal (animalName, age, sex, and species)
  - If the animal is not found, will throw an AnimalNotFoundException
  - If the provided animalName contains illegal characters, it will throw an InvalidCharacterException.
  - If the animal being updated is in a habitat, and the keeper is not the owner of that habitat, will throw an UserSecurityException.
  - If the animal being updated is in a habitat, and the animal name is already present on the habitat, will throw an DuplicateAnimalException.

### 6.31. ViewHabitatAnimals Endpoint

- Accepts PUT requests to /habitats/:habitatId/animals
- Accepts a habitatId, and returns the list of animals present on the habitat.
  - If the habitat is not found, will throw an HabitatNotFoundException

### 6.32. ViewEnrichmentActivity Endpoint

- Accepts PUT requests to /enrichmentActivities/:activityId
- Accepts an activityId, and returns the EnrichmentActivity associated with that Id.
  - If the activity is not found, will throw an EnrichmentActivityNotFoundException

### 6.33. ReactiveAnimal Endpoint

- Accepts PUT requests to /animals/:animalId
- Accepts an animalId, habitatId, and keeperManagerId to reactivate an animal to a specific habitat.
  - If the animal is not found, will throw an AnimalNotFoundException
  - If the habitat is not found, will throw a HabitatNotFoundException
  - If the keeper does not own the habitat they are trying to add the animal to, will throw a UserSecurityException
  - If the species of the animal is not included on the requested habitat’s list of species, will throw an IncompatibleSpeciesException
  - If the animal name is already listed on the habitat, will throw a DuplicateAnimalException

## 7. Tables

### 7.1. Habitats

- habitatId // partition key, string
- keeperManagerId // string (keeperManagerId-habitatId-index)
- keeperName // string
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
- activityName // string  
- description // string  

  * keeperRating-enrichmentId-index includes ALL attributes

### 7.3. EnrichmentActivities

- activityId // partition key, string
- enrichmentId // string
- activityName // string
- keeperRating // number (keeperRating-enrichmentId-index)
- description // string
- dateCompleted // string
- habitatId // string
- isCompleted // string (activityId-isComplete-index)
- onHabitat // bool

  * keeperRating-enrichmentId-index includes ALL attributes
  * activityId-isComplete-index includes ALL attributes

### 7.4 Animals
- animalId // partition key, string
- animalName // string
- age // int
- sex // string
- species // string
- isActive // string (animalId-isActive-index)
- habitatId // string
- onHabitat // bool

  *animalId-isActive-index includes ALL attributes

## 8. Link to Google doc with images
[FULL DESIGN DOC](https://docs.google.com/document/d/1qD5lhMVNko6C9FZJOiNMof6X5cuzQFkSKtgWBlDoVaY/edit)


