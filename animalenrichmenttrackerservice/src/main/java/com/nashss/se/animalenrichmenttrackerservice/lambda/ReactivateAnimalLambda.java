package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ReactivateAnimalRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ReactivateAnimalResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ReactivateAnimalLambda
        extends LambdaActivityRunner<ReactivateAnimalRequest, ReactivateAnimalResult>
        implements RequestHandler<AuthenticatedLambdaRequest<ReactivateAnimalRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<ReactivateAnimalRequest> input, Context context) {
        return super.runActivity(
            () -> {
                ReactivateAnimalRequest unAuthReq = input.fromBody(ReactivateAnimalRequest.class);
                return input.fromUserClaims(claims ->
                        ReactivateAnimalRequest.builder()
                                .withAnimalId(unAuthReq.getAnimalId())
                                .withHabitatId(unAuthReq.getHabitatId())
                                .withKeeperManagerId(claims.get("email"))
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideReactivateAnimalActivity().handleRequest(request)
        );
    }
}
