package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.RemoveSpeciesRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.RemoveSpeciesResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class RemoveSpeciesLambda
        extends LambdaActivityRunner<RemoveSpeciesRequest, RemoveSpeciesResult>
        implements RequestHandler<AuthenticatedLambdaRequest<RemoveSpeciesRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<RemoveSpeciesRequest> input, Context context) {
        return super.runActivity(
            () -> {
                RemoveSpeciesRequest unauthReq = input.fromBody(RemoveSpeciesRequest.class);
                return input.fromUserClaims(claims ->
                        RemoveSpeciesRequest.builder()
                                .withSpeciesToRemove(unauthReq.getSpeciesToRemove())
                                .withHabitatId(unauthReq.getHabitatId())
                                .withKeeperManagerId(claims.get("email"))
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideRemoveSpeciesActivity().handleRequest(request)
        );
    }
}
