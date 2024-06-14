package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewSpeciesListRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewSpeciesListResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ViewSpeciesListLambda
        extends LambdaActivityRunner<ViewSpeciesListRequest, ViewSpeciesListResult>
        implements RequestHandler<LambdaRequest<ViewSpeciesListRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(LambdaRequest<ViewSpeciesListRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromPath(path ->
                    ViewSpeciesListRequest.builder()
                            .withHabitatId(path.get("habitatId"))
                            .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideViewSpeciesListActivity().handleRequest(request)
        );
    }
}
