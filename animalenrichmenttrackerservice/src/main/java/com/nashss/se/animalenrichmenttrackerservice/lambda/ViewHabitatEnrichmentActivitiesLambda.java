package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewHabitatEnrichmentActivitesRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewHabitatEnrichmentActivitesResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;


public class ViewHabitatEnrichmentActivitiesLambda
        extends LambdaActivityRunner<ViewHabitatEnrichmentActivitesRequest, ViewHabitatEnrichmentActivitesResult>
        implements RequestHandler<LambdaRequest<ViewHabitatEnrichmentActivitesRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(LambdaRequest<ViewHabitatEnrichmentActivitesRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromPath(path ->
                    ViewHabitatEnrichmentActivitesRequest.builder()
                            .withHabitatId(path.get("habitatId"))
                            .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideViewHabitatEnrichmentActivitiesActivity().handleRequest(request)
        );
    }
}
