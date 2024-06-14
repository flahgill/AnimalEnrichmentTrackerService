package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.AddSpeciesRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.AddSpeciesResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class AddSpeciesLambda
        extends LambdaActivityRunner<AddSpeciesRequest, AddSpeciesResult>
        implements RequestHandler<AuthenticatedLambdaRequest<AddSpeciesRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<AddSpeciesRequest> input, Context context) {
        return super.runActivity(
            () -> {
                AddSpeciesRequest unAuthReq = input.fromBody(AddSpeciesRequest.class);
                return input.fromUserClaims(claims ->
                        AddSpeciesRequest.builder()
                                .withSpeciesToAdd(unAuthReq.getSpeciesToAdd())
                                .withHabitatId(unAuthReq.getHabitatId())
                                .withKeeperManagerId(claims.get("email"))
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideAddSpeciesActivity().handleRequest(request)
        );
    }
}
