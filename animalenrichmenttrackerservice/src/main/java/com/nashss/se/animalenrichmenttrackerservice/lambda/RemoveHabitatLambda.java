package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.RemoveHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.RemoveHabitatResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RemoveHabitatLambda
        extends LambdaActivityRunner<RemoveHabitatRequest, RemoveHabitatResult>
        implements RequestHandler<AuthenticatedLambdaRequest<RemoveHabitatRequest>, LambdaResponse> {

    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<RemoveHabitatRequest> input, Context context) {
        log.info("handleRequest");

        return super.runActivity(
            () -> {
                RemoveHabitatRequest unAuthRequest = input.fromUserClaims(claims ->
                        RemoveHabitatRequest.builder()
                                .withKeeperManagerId(claims.get("email"))
                                .build());

                return input.fromPath(path ->
                        RemoveHabitatRequest.builder()
                                .withHabitatId(path.get("habitatId"))
                                .withKeeperManagerId(unAuthRequest.getKeeperManagerId())
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideRemoveHabitatActivity().handleRequest(request)
        );
    }

}
