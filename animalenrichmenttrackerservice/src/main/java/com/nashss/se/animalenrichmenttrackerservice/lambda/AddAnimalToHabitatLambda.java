package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.AddAnimalToHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.AddAnimalToHabitatResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class AddAnimalToHabitatLambda
        extends LambdaActivityRunner<AddAnimalToHabitatRequest, AddAnimalToHabitatResult>
        implements RequestHandler<AuthenticatedLambdaRequest<AddAnimalToHabitatRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<AddAnimalToHabitatRequest> input, Context context) {
        return super.runActivity(
            () -> {
                AddAnimalToHabitatRequest unauthRequest = input.fromBody(AddAnimalToHabitatRequest.class);
                return input.fromUserClaims(claims ->
                        AddAnimalToHabitatRequest.builder()
                                .withAnimalName(unauthRequest.getAnimalName())
                                .withHabitatId(unauthRequest.getHabitatId())
                                .withAge(unauthRequest.getAge())
                                .withSex(unauthRequest.getSex())
                                .withSpecies(unauthRequest.getSpecies())
                                .withKeeperManagerId(claims.get("email"))
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideAddAnimalToHabitatActivity().handleRequest(request)
        );
    }
}
