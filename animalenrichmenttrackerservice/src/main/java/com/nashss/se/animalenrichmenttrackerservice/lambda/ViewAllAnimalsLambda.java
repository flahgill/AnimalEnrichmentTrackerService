package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewAllAnimalsRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewAllAnimalsResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ViewAllAnimalsLambda
        extends LambdaActivityRunner<ViewAllAnimalsRequest, ViewAllAnimalsResult>
        implements RequestHandler<LambdaRequest<ViewAllAnimalsRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(LambdaRequest<ViewAllAnimalsRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromQuery(query ->
                    ViewAllAnimalsRequest.builder()
                            .withIsActive(query.get("isActive"))
                            .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideViewAllAnimalsActivity().handleRequest(request)
        );
    }
}
