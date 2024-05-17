package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewHabitatResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ViewHabitatLambda
        extends LambdaActivityRunner<ViewHabitatRequest, ViewHabitatResult>
        implements RequestHandler<AuthenticatedLambdaRequest<ViewHabitatRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<ViewHabitatRequest> input, Context context) {
        System.out.println("INPUT: " + input.getBody());
        return super.runActivity(
            () -> {
                ViewHabitatRequest unauthRequest = input.fromUserClaims(claims ->
                        ViewHabitatRequest.builder()
                                .withKeeperManagerId(claims.get("email"))
                                .build());

                return input.fromPath(path ->
                        ViewHabitatRequest.builder()
                                .withKeeperManagerId(unauthRequest.getKeeperManagerId())
                                .withHabitatId(path.get("habitatId"))
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideViewHabitatActivity().handleRequest(request)
        );
    }
}
