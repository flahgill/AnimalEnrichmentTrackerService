package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewHabitatEnrichmentsRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewHabitatEnrichmentsResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;


public class ViewHabitatEnrichmentsLambda
        extends LambdaActivityRunner<ViewHabitatEnrichmentsRequest, ViewHabitatEnrichmentsResult>
        implements RequestHandler<LambdaRequest<ViewHabitatEnrichmentsRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(LambdaRequest<ViewHabitatEnrichmentsRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromPath(path ->
                    ViewHabitatEnrichmentsRequest.builder()
                            .withHabitatId(path.get("habitatId"))
                            .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideViewHabitatEnrichmentsActivity().handleRequest(request)
        );
    }
}
