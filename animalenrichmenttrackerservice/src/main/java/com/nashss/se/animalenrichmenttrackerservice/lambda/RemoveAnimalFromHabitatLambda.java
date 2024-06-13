package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.RemoveAnimalFromHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.RemoveAnimalFromHabitatResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class RemoveAnimalFromHabitatLambda
        extends LambdaActivityRunner<RemoveAnimalFromHabitatRequest, RemoveAnimalFromHabitatResult>
        implements RequestHandler<AuthenticatedLambdaRequest<RemoveAnimalFromHabitatRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<RemoveAnimalFromHabitatRequest> input,
                                        Context context) {
        return super.runActivity(
            () -> {
                RemoveAnimalFromHabitatRequest unauthReq = input.fromBody(RemoveAnimalFromHabitatRequest.class);
                return input.fromUserClaims(claims ->
                        RemoveAnimalFromHabitatRequest.builder()
                                .withAnimalId(unauthReq.getAnimalId())
                                .withHabitatId(unauthReq.getHabitatId())
                                .withKeeperManagerId(claims.get("email"))
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideRemoveAnimalFromHabitatActivity().handleRequest(request)
        );
    }
}
