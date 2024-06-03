package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewEnrichmentActivityRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewEnrichmentActivityResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ViewEnrichmentActivityLambda
        extends LambdaActivityRunner<ViewEnrichmentActivityRequest, ViewEnrichmentActivityResult>
        implements RequestHandler<LambdaRequest<ViewEnrichmentActivityRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(LambdaRequest<ViewEnrichmentActivityRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromPath(path ->
                    ViewEnrichmentActivityRequest.builder()
                            .withActivityId(path.get("activityId"))
                            .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideViewEnrichmentActivityActivity().handleRequest(request)
        );
    }
}
