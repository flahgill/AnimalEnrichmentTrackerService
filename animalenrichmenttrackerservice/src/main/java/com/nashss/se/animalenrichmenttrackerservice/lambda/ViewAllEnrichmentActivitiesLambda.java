package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewAllEnrichmentActivitiesRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewAllEnrichmentActivitiesResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ViewAllEnrichmentActivitiesLambda
        extends LambdaActivityRunner<ViewAllEnrichmentActivitiesRequest, ViewAllEnrichmentActivitiesResult>
        implements RequestHandler<LambdaRequest<ViewAllEnrichmentActivitiesRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(LambdaRequest<ViewAllEnrichmentActivitiesRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromQuery(query ->
                    ViewAllEnrichmentActivitiesRequest.builder()
                            .withIsComplete(query.get("isComplete"))
                            .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideViewAllEnrichmentActivitiesActivity().handleRequest(request)
        );
    }
}
