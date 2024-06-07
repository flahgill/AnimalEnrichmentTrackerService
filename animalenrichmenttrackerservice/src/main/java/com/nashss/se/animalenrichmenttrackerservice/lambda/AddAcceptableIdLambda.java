package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.AddAcceptableIdRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.AddAcceptableIdResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class AddAcceptableIdLambda
        extends LambdaActivityRunner<AddAcceptableIdRequest, AddAcceptableIdResult>
        implements RequestHandler<AuthenticatedLambdaRequest<AddAcceptableIdRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<AddAcceptableIdRequest> input, Context context) {
        return super.runActivity(
            () -> {
                AddAcceptableIdRequest unauthReq = input.fromBody(AddAcceptableIdRequest.class);
                return input.fromUserClaims(claims ->
                        AddAcceptableIdRequest.builder()
                                .withIdToAdd(unauthReq.getIdToAdd())
                                .withHabitatId(unauthReq.getHabitatId())
                                .withKeeperManagerId(claims.get("email"))
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideAddAcceptableIdActivity().handleRequest(request)
        );
    }
}
