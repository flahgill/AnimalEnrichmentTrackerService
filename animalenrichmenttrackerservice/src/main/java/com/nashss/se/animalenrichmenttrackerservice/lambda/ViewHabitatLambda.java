package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewHabitatResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ViewHabitatLambda
        extends LambdaActivityRunner<ViewHabitatRequest, ViewHabitatResult>
        implements RequestHandler<LambdaRequest<ViewHabitatRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(LambdaRequest<ViewHabitatRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromPath(path ->
                        ViewHabitatRequest.builder()
                                .withHabitatId(path.get("habitatId"))
                                .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideViewHabitatActivity().handleRequest(request)
        );
    }
}
