package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.AddEnrichmentActivityToHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.AddEnrichmentActivityToHabitatResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class AddEnrichmentActivityToHabitatLambda extends LambdaActivityRunner<AddEnrichmentActivityToHabitatRequest,
        AddEnrichmentActivityToHabitatResult>
        implements RequestHandler<AuthenticatedLambdaRequest<AddEnrichmentActivityToHabitatRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<AddEnrichmentActivityToHabitatRequest> input,
                                        Context context) {
        return super.runActivity(
            () -> {
                AddEnrichmentActivityToHabitatRequest unauthReq =
                        input.fromBody(AddEnrichmentActivityToHabitatRequest.class);
                return input.fromUserClaims(claims ->
                        AddEnrichmentActivityToHabitatRequest.builder()
                                .withHabitatId(unauthReq.getHabitatId())
                                .withEnrichmentId(unauthReq.getEnrichmentId())
                                .withDateCompleted(unauthReq.getDateCompleted())
                                .withKeeperRating(unauthReq.getKeeperRating())
                                .withIsComplete(unauthReq.getIsComplete())
                                .withKeeperManagerId(claims.get("email"))
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideAddEnrichmentToHabitatActivity().handleRequest(request)
        );
    }
}
