package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewAcceptableEnrichmentIdsRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewAcceptableEnrichmentIdsResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ViewAcceptableEnrichmentIdsLambda
        extends LambdaActivityRunner<ViewAcceptableEnrichmentIdsRequest, ViewAcceptableEnrichmentIdsResult>
        implements RequestHandler<LambdaRequest<ViewAcceptableEnrichmentIdsRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(LambdaRequest<ViewAcceptableEnrichmentIdsRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromPath(path ->
                    ViewAcceptableEnrichmentIdsRequest.builder()
                            .withHabitatId(path.get("habitatId"))
                            .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideViewAcceptableEnrichmentIdsActivity().handleRequest(request)
        );
    }
}
