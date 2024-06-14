import axios from "axios";
import BindingClass from "../util/bindingClass";
import Authenticator from "./authenticator";

/**
 * Client to call the AnimalEnrichmentTrackerService.
 *
 * This could be a great place to explore Mixins. Currently the client is being loaded multiple times on each page,
 * which we could avoid using inheritance or Mixins.
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Classes#Mix-ins
 * https://javascript.info/mixins
  */
export default class AnimalEnrichmentTrackerClient extends BindingClass {

    constructor(props = {}) {
        super();

        const methodsToBind = ['clientLoaded', 'getIdentity', 'login', 'logout', 'getHabitat', 'createHabitat', 'getUserHabitats',
        'removeHabitat', 'updateHabitat', 'getAnimalsForHabitat', 'addAnimalToHabitat', 'removeAnimalFromHabitat', 'getAllHabitats',
        'getHabitatEnrichments', 'addEnrichmentToHabitat', 'removeEnrichmentActivityFromHabitat', 'getEnrichmentActivity', 'getAllEnrichmentActivities',
        'removeEnrichmentActivity', 'searchEnrichmentActivities', 'searchEnrichments', 'reAddActivityToHabitat', 'getAcceptableIds', 'addAcceptableId',
        'removeAcceptableId', 'getAnimal', 'removeAnimal', 'getAllAnimals', 'getSpeciesList', 'addSpecies', 'removeSpecies'];
        this.bindClassMethods(methodsToBind, this);

        this.authenticator = new Authenticator();;
        this.props = props;

        axios.defaults.baseURL = process.env.API_BASE_URL;
        this.axiosClient = axios;
        this.clientLoaded();
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     */
    clientLoaded() {
        if (this.props.hasOwnProperty("onReady")) {
            this.props.onReady(this);
        }
    }

    /**
     * Get the identity of the current user
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The user information for the current user.
     */
    async getIdentity(errorCallback) {
        try {
            const isLoggedIn = await this.authenticator.isUserLoggedIn();

            if (!isLoggedIn) {
                return undefined;
            }

            return await this.authenticator.getCurrentUserInfo();
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async login() {
        this.authenticator.login();
    }

    async logout() {
        this.authenticator.logout();
    }

    async getTokenOrThrow(unauthenticatedErrorMessage) {
        const isLoggedIn = await this.authenticator.isUserLoggedIn();
        if (!isLoggedIn) {
            throw new Error(unauthenticatedErrorMessage);
        }

        return await this.authenticator.getUserToken();
    }

    /**
     * Search for a habitat.
     * @param criteria A string containing search criteria to pass to the API.
     * @returns The habitats that match the search criteria.
     */
    async search(criteria, errorCallback) {
        try {
            const queryParams = new URLSearchParams({ q: criteria })
            const queryString = queryParams.toString();

            const response = await this.axiosClient.get(`habitats/search?${queryString}`);

            return response.data.habitats;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    /**
     * Search for an enrichment activity.
     * @param criteria A string containing search criteria to pass to the API.
     * @returns The activities that match the search criteria.
     */
    async searchEnrichmentActivities(criteria, errorCallback) {
        try {
            const queryParams = new URLSearchParams({ q: criteria })
            const queryString = queryParams.toString();

            const response = await this.axiosClient.get(`enrichmentActivities/search?${queryString}`);

            return response.data.enrichmentActivities;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    /**
     * Search for an enrichment.
     * @param criteria A string containing search criteria to pass to the API.
     * @returns The enrichments that match the search criteria.
     */
    async searchEnrichments(criteria, errorCallback) {
        try {
            const queryParams = new URLSearchParams({ q: criteria })
            const queryString = queryParams.toString();

            const response = await this.axiosClient.get(`enrichments/search?${queryString}`);

            return response.data.enrichments;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    /**
     * gets all habitats saved in DB.
     * @param isActive the requested active status to sort habitats by.
     * @returns a list of saved habitats.
     */
     async getAllHabitats(isActive, errorCallback) {
        try {
            const queryParams = new URLSearchParams({ isActive: isActive});
            const queryString = queryParams.toString();

            const response = await this.axiosClient.get(`habitats?${queryString}`);

            return response.data.habitats;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
     }

    /**
     * Gets the habitat for the given ID.
     * @param id Unique identifier for a habitat
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The habitat's metadata.
     */
    async getHabitat(habitatId, errorCallback) {
        try {
            const response = await this.axiosClient.get(`habitats/${habitatId}`);
            return response.data.habitat;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    /**
     * Create a new Habitat owned by the current user.
     * @param name The name of the habitat to create.
     * @param tags Metadata species to associate with a habitat.
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The habitat that has been created.
     */
    async createHabitat(habitatName, species, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can create habitats.");
            const response = await this.axiosClient.post(`habitats`, {
                habitatName: habitatName,
                species: species
            }, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            return response.data.habitat;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    /**
     * removes a habitat.
     * @param habitatId The id of the habitat.
     * @returns the list of habitats.
     */
    async removeHabitat(habitatId, errorCallback) {
        try {
                const token = await this.getTokenOrThrow("Only authenticated users can remove a habitat.");
                const response = await this.axiosClient.delete(`habitats/${habitatId}`, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    },
                    data: {
                        habitatId: habitatId
                    }
                });
                return response.data.habitats;
            } catch (error) {
                if (errorCallback) {
                    errorCallback(error);
                }
                this.handleError(error, errorCallback);
            }
    }

    /**
     * Get the habitats of a given keeper.
     * @param keeperManagerId Unique identifier for a keeper
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The list of habitats associated with a user.
     */
     async getUserHabitats(keeperManagerId, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can view their habitats.");
            const response = await this.axiosClient.get(`userHabitats`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
                });
            return response.data.habitats;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
     }

     /**
      * Update an existing Habitat.
      * @param habitatId the Id of the habitat to update.
      * @param habitatName the name of the habitat to update.
      * @param isActive the active status of the habitat to update.
      * @param errorCallback (Optional) A function to execute if the call fails.
      * @returns The habitat that has been created.
      */
     async updateHabitat(habitatId, habitatName, isActive, errorCallback) {
     try {
            const token = await this.getTokenOrThrow("Only authenticated users can update their habitat");
            const response = await this.axiosClient.put(`habitats/${habitatId}`, {
                habitatId: habitatId,
                habitatName: habitatName,
                isActive: isActive
            }, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            return response.data.habitat;
         } catch (error) {
            this.handleError(error, errorCallback)
         }
     }

     /**
      * Gets the list of animals for the given habitat ID.
      * @param habitatId Unique identifier for a habitat
      * @param errorCallback (Optional) A function to execute if the call fails.
      * @returns A list of animals associated with the habitat Id.
      */
     async getAnimalsForHabitat(habitatId, errorCallback) {
         try {
             const response = await this.axiosClient.get(`habitats/${habitatId}/animals`);
             return response.data.animalsInHabitat;
         } catch (error) {
             this.handleError(error, errorCallback)
         }
     }

     /**
      * Gets the animal for the given ID.
      * @param animalId Unique identifier for a animal
      * @param errorCallback (Optional) A function to execute if the call fails.
      * @returns The animal's metadata.
      */
     async getAnimal(animalId, errorCallback) {
         try {
             const response = await this.axiosClient.get(`animals/${animalId}`);
             return response.data.animal;
         } catch (error) {
             this.handleError(error, errorCallback)
         }
     }

     /**
      * Add an animal to an existing Habitat and to the animals DDB table.
      * @param habitatId the Id of the habitat to add the animal to.
      * @param animalName name of the animal to add.
      * @param age age of the animal to add.
      * @param sex sex of the animal to add.
      * @param species species of the animal to add.
      * @param errorCallback (Optional) A function to execute if the call fails.
      * @returns newly added animal.
      */
     async addAnimalToHabitat(habitatId, animalName, age, sex, species, errorCallback) {
     try {
            const token = await this.getTokenOrThrow("Only authenticated users add an animal to their habitat");
            const response = await this.axiosClient.post(`habitats/${habitatId}/animals`, {
                habitatId: habitatId,
                animalName: animalName,
                age: age,
                species: species,
                sex: sex
            }, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            return response.data.animal;
         } catch (error) {
            this.handleError(error, errorCallback)
         }
     }

     /**
      * removes an animal from a habitat.
      * @param habitatId The id of the habitat.
      * @param animalId the id of the animal.
      * @returns removed animal.
      */
     async removeAnimalFromHabitat(habitatId, animalId, errorCallback) {
         try {
             const token = await this.getTokenOrThrow("Only authenticated users can remove an animal from their habitat.");
             const response = await this.axiosClient.delete(`habitats/${habitatId}/animals`, {
                 headers: {
                     Authorization: `Bearer ${token}`
                 },
                 data: {
                     habitatId: habitatId,
                     animalId: animalId
                 }
                 });
             return response.data.animal;
         } catch (error) {
             this.handleError(error, errorCallback)
         }
     }

     /**
      * removes an animal (Hard Delete).
      * @param animalId The id of the animal.
      * @returns removed animal.
      */
      async removeAnimal(animalId, errorCallback) {
         try {
             const token = await this.getTokenOrThrow("Only authenticated users can delete an animal.");
             const response = await this.axiosClient.delete(`animals/${animalId}`, {
                 headers: {
                     Authorization: `Bearer ${token}`
                 },
                 data: {
                     animalId: animalId
                 }
                 });
             return response.data.animal;
         } catch (error) {
             this.handleError(error, errorCallback)
         }
      }

      /**
       * gets all animals saved in DB.
       * @param isActive the requested active status to sort animals by.
       * @returns a list of saved animals.
       */
       async getAllAnimals(isActive, errorCallback) {
          try {
              const queryParams = new URLSearchParams({ isActive: isActive});
              const queryString = queryParams.toString();

              const response = await this.axiosClient.get(`animals?${queryString}`);

              return response.data.animals;
          } catch (error) {
              this.handleError(error, errorCallback)
          }
       }

     /**
      * Gets the habitat's list of completedEnrichments for the given ID.
      * @param habitatId Unique identifier for a habitat
      * @param errorCallback (Optional) A function to execute if the call fails.
      * @returns the habitat's list of completedEnrichments
      */
      async getHabitatEnrichments(habitatId, errorCallback) {
         try {
             const response = await this.axiosClient.get(`habitats/${habitatId}/enrichmentActivities`);
             return response.data.completedEnrichments;
         } catch (error) {
             this.handleError(error, errorCallback)
         }
      }

      /**
       * Add an Enrichment activity to a habitat owned by the current user.
       * @param habitatId The id of of the habitat to add the enrichment to.
       * @param enrichmentId the id of the enrichment to add.
       * @param keeperRating the keeper rating.
       * @param dateCompleted the date the enrichment activity was completed.
       * @param errorCallback (Optional) A function to execute if the call fails.
       * @returns The habitat's updated list of completedEnrichments.
       */
      async addEnrichmentToHabitat(habitatId, enrichmentId, keeperRating, dateCompleted, isComplete, errorCallback) {
          try {
              const token = await this.getTokenOrThrow("Only authenticated users can add an enrichment activity to their habitat.");
              const response = await this.axiosClient.post(`habitats/${habitatId}/enrichmentActivities`, {
                  habitatId: habitatId,
                  enrichmentId: enrichmentId,
                  keeperRating: keeperRating,
                  dateCompleted: dateCompleted,
                  isComplete: isComplete
              }, {
                  headers: {
                      Authorization: `Bearer ${token}`
                  }
              });
              return response.data.completedEnrichments;
          } catch (error) {
              this.handleError(error, errorCallback)
          }
      }

      /**
       * removes an Enrichment Activity from a habitat (soft delete).
       * @param habitatId The id of the habitat.
       * @param activityId the id of the activity to be removed.
       * @returns The habitat's updated list of completedEnrichments.
       */
      async removeEnrichmentActivityFromHabitat(habitatId, activityId, errorCallback) {
          try {
              const token = await this.getTokenOrThrow("Only authenticated users can remove an activity from their habitat.");
              const response = await this.axiosClient.delete(`habitats/${habitatId}/enrichmentActivities`, {
                  headers: {
                      Authorization: `Bearer ${token}`
                  },
                  data: {
                      habitatId: habitatId,
                      activityId: activityId
                  }
                  });
              return response.data.completedEnrichments;
          } catch (error) {
              this.handleError(error, errorCallback)
          }
      }

      /**
       * Update an existing EnrichmentActivity on a habitat.
       * @param habitatId the Id of the habitat's list of completedEnrichments to update.
       * @param activityId the Id of EnichmentActivity to update.
       * @param dateCompleted the date to update.
       * @param isComplete the completion status to update.
       * @param keeperRating the rating to update.
       * @param errorCallback (Optional) A function to execute if the call fails.
       * @returns The habitat's updated list of completedEnrichments.
       */
      async updateHabitatEnrichmentActivity(habitatId, activityId, keeperRating, dateCompleted, isComplete, errorCallback) {
      try {
             const token = await this.getTokenOrThrow("Only authenticated users can update an enrichment activity");
             const response = await this.axiosClient.put(`habitats/${habitatId}/enrichmentActivities/${activityId}`, {
                 habitatId: habitatId,
                 activityId: activityId,
                 keeperRating: keeperRating,
                 dateCompleted: dateCompleted,
                 isComplete: isComplete
             }, {
                 headers: {
                     Authorization: `Bearer ${token}`
                 }
             });
             return response.data.completedEnrichments;
          } catch (error) {
             this.handleError(error, errorCallback)
          }
      }

      /**
       * Gets the EnrichmentActivity for the given ID.
       * @param activityId Unique identifier for an activity.
       * @param errorCallback (Optional) A function to execute if the call fails.
       * @returns The EnrichmentActivity's metadata.
       */
      async getEnrichmentActivity(activityId, errorCallback) {
          try {
              const response = await this.axiosClient.get(`enrichmentActivities/${activityId}`);
              return response.data.enrichmentActivity;
          } catch (error) {
              this.handleError(error, errorCallback)
          }
      }

      /**
       * gets all Enrichment Activities saved in DB.
       * @param isActive the requested completion status to sort activities by.
       * @returns a list of saved enrichment activities.
       */
       async getAllEnrichmentActivities(isComplete, errorCallback) {
          try {
              const queryParams = new URLSearchParams({ isComplete: isComplete});
              const queryString = queryParams.toString();

              const response = await this.axiosClient.get(`enrichmentActivities?${queryString}`);

              return response.data.enrichmentActivities;
          } catch (error) {
              this.handleError(error, errorCallback)
          }
       }

       /**
        * removes an enrichment activity.
        * @param activityId The id of the activity to remove.
        * @returns the list of enrichment activities.
        */
       async removeEnrichmentActivity(activityId, errorCallback) {
           try {
                   const token = await this.getTokenOrThrow("Only authenticated users can remove an enrichment activity.");
                   const response = await this.axiosClient.delete(`enrichmentActivities/${activityId}`, {
                       headers: {
                           Authorization: `Bearer ${token}`
                       },
                       data: {
                           activityId: activityId
                       }
                   });
                   return response.data.enrichmentActivities;
               } catch (error) {
                   if (errorCallback) {
                       errorCallback(error);
                   }
                   this.handleError(error, errorCallback);
               }
       }

       /**
        * Re-adds an existing activity back to it's associated habitat.
        * @param habitatId the Id of the habitat to add back to.
        * @param activityId the Id of the activity to add.
        * @param errorCallback (Optional) A function to execute if the call fails.
        * @returns The habitat that has been created.
        */
       async reAddActivityToHabitat(habitatId, activityId, errorCallback) {
       try {
              const token = await this.getTokenOrThrow("Only authenticated users can reactivate an enrichment activity");
              const response = await this.axiosClient.put(`habitats/${habitatId}/enrichmentActivities`, {
                  habitatId: habitatId,
                  activityId: activityId
              }, {
                  headers: {
                      Authorization: `Bearer ${token}`
                  }
              });
              return response.data.completedEnrichments;
           } catch (error) {
              this.handleError(error, errorCallback)
           }
       }

       /**
        * Gets the habitat's list of acceptable enrichment Ids for the given ID.
        * @param habitatId Unique identifier for a habitat
        * @param errorCallback (Optional) A function to execute if the call fails.
        * @returns {Promise<string[]>} The habitat's list of acceptable enrichment Ids.
        */
       async getAcceptableIds(habitatId, errorCallback) {
           try {
               const response = await this.axiosClient.get(`habitats/${habitatId}/acceptableIds`);
               return response.data.acceptableEnrichmentIds;
           } catch (error) {
               this.handleError(error, errorCallback)
           }
       }

       /**
        * Add an acceptable enrichment id to an existing Habitat.
        * @param habitatId the Id of the habitat to update.
        * @param idToAdd the id to be added
        * @param errorCallback (Optional) A function to execute if the call fails.
        * @returns {Promise<string[]>} The habitat's list of acceptable enrichment ids.
        */
        async addAcceptableId(habitatId, idToAdd, errorCallback) {
            try {
                const token = await this.getTokenOrThrow("Only authenticated users can add an acceptable id to their habitat.");
                const response = await this.axiosClient.put(`habitats/${habitatId}/acceptableIds`, {
                    habitatId: habitatId,
                    idToAdd: idToAdd
                }, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });
                return response.data.acceptableEnrichmentIds;
            } catch (error) {
               this.handleError(error, errorCallback)
            }
        }

        /**
         * removes an acceptable Id from a habitat.
         * @param habitatId The id of the habitat.
         * @param idToRemove the id to be removed.
         * @returns {Promise<string[]>} The habitat's list of acceptable enrichment ids.
         */
         async removeAcceptableId(habitatId, idToRemove, errorCallback) {
            try {
                const token = await this.getTokenOrThrow("Only authenticated users can remove an acceptable Id from their habitat.");
                const response = await this.axiosClient.delete(`habitats/${habitatId}/acceptableIds`, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    },
                    data: {
                        habitatId: habitatId,
                        idToRemove: idToRemove
                    }
                    });
                return response.data.acceptableEnrichmentIds;
            } catch (error) {
                this.handleError(error, errorCallback)
            }
         }

         /**
         * Gets the habitat's list of species for the given ID.
         * @param habitatId Unique identifier for a habitat
         * @param errorCallback (Optional) A function to execute if the call fails.
         * @returns {Promise<string[]>} The habitat's list of species.
         */
        async getSpeciesList(habitatId, errorCallback) {
            try {
                const response = await this.axiosClient.get(`habitats/${habitatId}/species`);
                return response.data.speciesList;
            } catch (error) {
                this.handleError(error, errorCallback)
            }
        }

        /**
        * Add a new species to an existing Habitat.
        * @param habitatId the Id of the habitat to update.
        * @param speciesToAdd the species to be added
        * @param errorCallback (Optional) A function to execute if the call fails.
        * @returns {Promise<string[]>} The habitat's list of species.
        */
        async addSpecies(habitatId, speciesToAdd, errorCallback) {
            try {
                const token = await this.getTokenOrThrow("Only authenticated users can add a species type to their habitat");
                const response = await this.axiosClient.post(`habitats/${habitatId}/species`, {
                    habitatId: habitatId,
                    speciesToAdd: speciesToAdd
                }, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });
                return response.data.speciesList;
            } catch (error) {
               this.handleError(error, errorCallback)
            }
        }

        /**
         * removes species from a habitat.
         * @param habitatId The id of the habitat.
         * @param speciesToRemove the species to be removed.
         * @returns {Promise<string[]>} The habitat's list of species.
         */
         async removeSpecies(habitatId, speciesToRemove, errorCallback) {
            try {
                const token = await this.getTokenOrThrow("Only authenticated users can remove a species type from their habitat.");
                const response = await this.axiosClient.delete(`habitats/${habitatId}/species`, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    },
                    data: {
                        habitatId: habitatId,
                        speciesToRemove: speciesToRemove
                    }
                    });
                return response.data.speciesList;
            } catch (error) {
                this.handleError(error, errorCallback)
            }
         }

    /**
     * Helper method to log the error and run any error functions.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(error, errorCallback) {
        console.error(error);

        const errorFromApi = error?.response?.data?.error_message;
        if (errorFromApi) {
            console.error(errorFromApi)
            error.message = errorFromApi;
        }

        if (errorCallback) {
            errorCallback(error);
        }
    }
}