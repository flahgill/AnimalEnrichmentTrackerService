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
        'removeHabitat', 'updateHabitat', 'getAnimalsForHabitat', 'addAnimalToHabitat', 'removeAnimalFromHabitat'];
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
            const token = await this.getTokenOrThrow("Only authenticated users can remove a booklist.");
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
            this.handleError(error, errorCallback)
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
      * @param species the species of the habitat to update.
      * @param errorCallback (Optional) A function to execute if the call fails.
      * @returns The habitat that has been created.
      */
     async updateHabitat(habitatId, habitatName, species, errorCallback) {
     try {
            const token = await this.getTokenOrThrow("Only authenticated users can update their habitat");
            const response = await this.axiosClient.put(`habitats/${habitatId}`, {
                habitatId: habitatId,
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
      * Gets the habitat's list of animals for the given ID.
      * @param habitatId Unique identifier for a habitat
      * @param errorCallback (Optional) A function to execute if the call fails.
      * @returns {Promise<string[]>} The habitat's list of animals.
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
      * Add an animal to an existing Habitat.
      * @param habitatId the Id of the habitat to update.
      * @param animalToAdd the animal to be removed
      * @param errorCallback (Optional) A function to execute if the call fails.
      * @returns {Promise<string[]>} The habitat's list of animals.
      */
     async addAnimalToHabitat(habitatId, animalToAdd, errorCallback) {
     try {
            const token = await this.getTokenOrThrow("Only authenticated users can update their habitat");
            const response = await this.axiosClient.put(`habitats/${habitatId}/animals`, {
                habitatId: habitatId,
                animalToAdd: animalToAdd
            }, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            return response.data.animalsInHabitat;
         } catch (error) {
            this.handleError(error, errorCallback)
         }
     }

     /**
      * removes an animal from a habitat.
      * @param habitatId The id of the habitat.
      * @param animalToRemove the animal to be removed.
      * @@returns {Promise<string[]>} The habitat's list of animals.
      */
     async removeAnimalFromHabitat(habitatId, animalToRemove, errorCallback) {
         try {
             const token = await this.getTokenOrThrow("Only authenticated users can remove an animal from a habitat.");
             const response = await this.axiosClient.delete(`habitats/${habitatId}/animals`, {
                 headers: {
                     Authorization: `Bearer ${token}`
                 },
                 data: {
                     habitatId: habitatId,
                     animalToRemove: animalToRemove
                 }
                 });
             return response.data.animalsInHabitat;
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