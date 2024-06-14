package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.UpdateAnimalRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.UpdateAnimalResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class UpdateAnimalLambda
        extends LambdaActivityRunner<UpdateAnimalRequest, UpdateAnimalResult>
        implements RequestHandler<AuthenticatedLambdaRequest<UpdateAnimalRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<UpdateAnimalRequest> input, Context context) {
        return super.runActivity(
            () -> {
                UpdateAnimalRequest unAuthReq = input.fromBody(UpdateAnimalRequest.class);
                return input.fromUserClaims(claims ->
                        UpdateAnimalRequest.builder()
                                .withAnimalId(unAuthReq.getAnimalId())
                                .withAnimalName(unAuthReq.getAnimalName())
                                .withAge(unAuthReq.getAge())
                                .withSex(unAuthReq.getSex())
                                .withSpecies(unAuthReq.getSpecies())
                                .withKeeperManagerId(claims.get("email"))
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideUpdateAnimalActivity().handleRequest(request)
        );
    }
}
