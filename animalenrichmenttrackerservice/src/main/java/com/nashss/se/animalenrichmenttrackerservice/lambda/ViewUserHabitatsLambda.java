package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewUserHabitatsRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewUserHabitatsResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ViewUserHabitatsLambda
        extends LambdaActivityRunner<ViewUserHabitatsRequest, ViewUserHabitatsResult>
        implements RequestHandler<AuthenticatedLambdaRequest<ViewUserHabitatsRequest>, LambdaResponse> {

    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<ViewUserHabitatsRequest> input, Context context) {
        log.info("handleRequest");

        return super.runActivity(
            () -> input.fromUserClaims(claims ->
                    ViewUserHabitatsRequest.builder()
                            .withKeeperManagerId(claims.get("email"))
                            .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideViewUserHabitatsActivity().handleRequest(request)
        );
    }
}
