package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.RemoveAnimalRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.RemoveAnimalResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class RemoveAnimalLambda
        extends LambdaActivityRunner<RemoveAnimalRequest, RemoveAnimalResult>
        implements RequestHandler<AuthenticatedLambdaRequest<RemoveAnimalRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<RemoveAnimalRequest> input, Context context) {
        return super.runActivity(
            () -> {
                RemoveAnimalRequest authReq = input.fromUserClaims(claims ->
                        RemoveAnimalRequest.builder()
                                .withKeeperManagerId(claims.get("email"))
                                .build());
                return input.fromPath(path ->
                        RemoveAnimalRequest.builder()
                                .withAnimalId(path.get("animalId"))
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideRemoveAnimalActivity().handleRequest(request)
        );
    }

}
