package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewAnimalsForHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewAnimalsForHabitatResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ViewAnimalsForHabitatLambda extends LambdaActivityRunner<ViewAnimalsForHabitatRequest,
        ViewAnimalsForHabitatResult>
        implements RequestHandler<LambdaRequest<ViewAnimalsForHabitatRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(LambdaRequest<ViewAnimalsForHabitatRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromPath(path ->
                    ViewAnimalsForHabitatRequest.builder()
                            .withHabitatId(path.get("habitatId"))
                            .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideViewAnimalsForHabitatActivity().handleRequest(request)
        );
    }
}
