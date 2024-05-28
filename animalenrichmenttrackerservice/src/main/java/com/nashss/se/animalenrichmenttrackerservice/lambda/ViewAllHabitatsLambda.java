package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewAllHabitatsRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewAllHabitatsResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ViewAllHabitatsLambda
        extends LambdaActivityRunner<ViewAllHabitatsRequest, ViewAllHabitatsResult>
        implements RequestHandler<LambdaRequest<ViewAllHabitatsRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(LambdaRequest<ViewAllHabitatsRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromQuery(query ->
                    ViewAllHabitatsRequest.builder()
                            .withIsActive(query.get("isActive"))
                            .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideViewAllHabitatsActivity().handleRequest(request)
        );
    }
}
