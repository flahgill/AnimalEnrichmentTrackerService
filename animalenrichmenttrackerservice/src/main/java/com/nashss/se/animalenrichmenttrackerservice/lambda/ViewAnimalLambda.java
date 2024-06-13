package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewAnimalRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewAnimalResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ViewAnimalLambda
        extends LambdaActivityRunner<ViewAnimalRequest, ViewAnimalResult>
        implements RequestHandler<LambdaRequest<ViewAnimalRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(LambdaRequest<ViewAnimalRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromPath(path ->
                    ViewAnimalRequest.builder()
                            .withAnimalId(path.get("animalId"))
                            .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideViewAnimalActivity().handleRequest(request)
        );
    }
}
