package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.UpdateHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.UpdateHabitatResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UpdateHabitatLambda
        extends LambdaActivityRunner<UpdateHabitatRequest, UpdateHabitatResult>
        implements RequestHandler<AuthenticatedLambdaRequest<UpdateHabitatRequest>, LambdaResponse> {

    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<UpdateHabitatRequest> input, Context context) {
        log.info("handleRequest");
        return super.runActivity(
            () -> {
                UpdateHabitatRequest unauthRequest = input.fromBody(UpdateHabitatRequest.class);
                return input.fromUserClaims(claims ->
                        UpdateHabitatRequest.builder()
                                .withHabitatId(unauthRequest.getHabitatId())
                                .withHabitatName(unauthRequest.getHabitatName())
                                .withSpecies(unauthRequest.getSpecies())
                                .withIsActive(unauthRequest.getIsActive())
                                .withKeeperManagerId(claims.get("email"))
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideUpdateHabitatActivity().handleRequest(request)
        );
    }
}
