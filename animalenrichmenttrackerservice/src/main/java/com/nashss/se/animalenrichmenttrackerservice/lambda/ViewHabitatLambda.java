package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewHabitatResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ViewHabitatLambda
        extends LambdaActivityRunner<ViewHabitatRequest, ViewHabitatResult>
        implements RequestHandler<LambdaRequest<ViewHabitatRequest>, LambdaResponse> {

    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(LambdaRequest<ViewHabitatRequest> input, Context context) {
        log.info("handleRequest");
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
