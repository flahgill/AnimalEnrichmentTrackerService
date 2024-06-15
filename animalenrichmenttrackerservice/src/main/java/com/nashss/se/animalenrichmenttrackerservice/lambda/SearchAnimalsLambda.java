package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.SearchAnimalsRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.SearchAnimalsResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class SearchAnimalsLambda
        extends LambdaActivityRunner<SearchAnimalsRequest, SearchAnimalsResult>
        implements RequestHandler<LambdaRequest<SearchAnimalsRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(LambdaRequest<SearchAnimalsRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromQuery(query ->
                    SearchAnimalsRequest.builder()
                            .withCriteria(query.get("q"))
                            .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideSearchAnimalsActivity().handleRequest(request)
        );
    }
}
