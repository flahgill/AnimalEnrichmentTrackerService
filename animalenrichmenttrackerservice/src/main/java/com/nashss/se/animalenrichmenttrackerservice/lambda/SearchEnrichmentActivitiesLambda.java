package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.SearchEnrichmentActivitiesRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.SearchEnrichmentActivitiesResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class SearchEnrichmentActivitiesLambda
        extends LambdaActivityRunner<SearchEnrichmentActivitiesRequest, SearchEnrichmentActivitiesResult>
        implements RequestHandler<LambdaRequest<SearchEnrichmentActivitiesRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(LambdaRequest<SearchEnrichmentActivitiesRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromQuery(query ->
                    SearchEnrichmentActivitiesRequest.builder()
                            .withCriteria(query.get("q"))
                            .build()),
            (request, serviceComponent) ->
                serviceComponent.provideSearchEnrichmentActivitiesActivity().handleRequest(request)
        );
    }
}
