package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.RemoveAcceptableIdRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.RemoveAcceptableIdResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class RemoveAcceptableIdLambda
        extends LambdaActivityRunner<RemoveAcceptableIdRequest, RemoveAcceptableIdResult>
        implements RequestHandler<AuthenticatedLambdaRequest<RemoveAcceptableIdRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<RemoveAcceptableIdRequest> input, Context context) {
        return super.runActivity(
            () -> {
                RemoveAcceptableIdRequest unauthReq = input.fromBody(RemoveAcceptableIdRequest.class);
                return input.fromUserClaims(claims ->
                        RemoveAcceptableIdRequest.builder()
                                .withIdToRemove(unauthReq.getIdToRemove())
                                .withHabitatId(unauthReq.getHabitatId())
                                .withKeeperManagerId(claims.get("email"))
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideRemoveAcceptableIdActivity().handleRequest(request)
        );
    }
}
