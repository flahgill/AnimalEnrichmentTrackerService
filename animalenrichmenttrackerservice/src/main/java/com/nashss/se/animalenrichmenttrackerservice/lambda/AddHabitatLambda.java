package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.AddHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.AddHabitatResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class AddHabitatLambda
        extends LambdaActivityRunner<AddHabitatRequest, AddHabitatResult>
        implements RequestHandler<AuthenticatedLambdaRequest<AddHabitatRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<AddHabitatRequest> input, Context context) {
        return super.runActivity(
            () -> {
                AddHabitatRequest unauthRequest = input.fromBody(AddHabitatRequest.class);
                return input.fromUserClaims(claims ->
                        AddHabitatRequest.builder()
                                .withHabitatName(unauthRequest.getHabitatName())
                                .withSpecies(unauthRequest.getSpecies())
                                .withKeeperManagerId(claims.get("email"))
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideAddHabitatActivity().handleRequest(request)
        );
    }
}
