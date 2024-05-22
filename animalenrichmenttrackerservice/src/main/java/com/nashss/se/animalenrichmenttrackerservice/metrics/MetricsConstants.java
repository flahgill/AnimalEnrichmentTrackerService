package com.nashss.se.animalenrichmenttrackerservice.metrics;

/**
 * Constant values for use with metrics.
 */
public class MetricsConstants {
    public static final String GETHABITAT_HABTITATNOTFOUND = "GetHabitat.HabitatNotFoundException.Count";
    public static final String GETENRICHMENT_ENRICHMENTNOTFOUND = "GetEnrichment.EnrichmentNotFoundException.Count";
    public static final String UPDATEHABITAT_INVALIDCHARACTEREXCEPTION =
            "UpdateHabitat.InvalidCharacterException.Count";
    public static final String UPDATEHABITAT_USERSECURITYEXCEPTION = "UpdateHabitat.UserSecurityException.Count";
    public static final String SERVICE = "Service";
    public static final String SERVICE_NAME = "AnimalEnrichmentTrackerService";
    public static final String NAMESPACE_NAME = "U3/AnimalEnrichmentTrackerService";
}
