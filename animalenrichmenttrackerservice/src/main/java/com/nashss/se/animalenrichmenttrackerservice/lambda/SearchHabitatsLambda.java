package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.SearchHabitatsRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.SearchHabitatsResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class SearchHabitatsLambda
        extends LambdaActivityRunner<SearchHabitatsRequest, SearchHabitatsResult>
        implements RequestHandler<LambdaRequest<SearchHabitatsRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(LambdaRequest<SearchHabitatsRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromQuery(query ->
                    SearchHabitatsRequest.builder()
                            .withCriteria(query.get("q"))
                            .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideSearchHabitatsActivity().handleRequest(request)
        );
    }
}
