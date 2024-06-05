package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.SearchEnrichmentsRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.SearchEnrichmentsResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class SearchEnrichmentsLambda
        extends LambdaActivityRunner<SearchEnrichmentsRequest, SearchEnrichmentsResult>
        implements RequestHandler<LambdaRequest<SearchEnrichmentsRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(LambdaRequest<SearchEnrichmentsRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromQuery(query ->
                    SearchEnrichmentsRequest.builder()
                            .withCriteria(query.get("q"))
                            .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideSearchEnrichmentsActivity().handleRequest(request)
        );
    }
}
