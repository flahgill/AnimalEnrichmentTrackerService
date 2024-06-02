package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.RemoveEnrichmentActivityFromHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.RemoveEnrichmentActivityFromHabitatResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class RemoveEnrichmentActivityFromHabitatLambda
        extends LambdaActivityRunner<RemoveEnrichmentActivityFromHabitatRequest,
        RemoveEnrichmentActivityFromHabitatResult>
        implements RequestHandler<AuthenticatedLambdaRequest<RemoveEnrichmentActivityFromHabitatRequest>,
        LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<RemoveEnrichmentActivityFromHabitatRequest> input,
                                        Context context) {
        return super.runActivity(
            () -> {
                RemoveEnrichmentActivityFromHabitatRequest unauthReq =
                        input.fromBody(RemoveEnrichmentActivityFromHabitatRequest.class);
                return input.fromUserClaims(claims ->
                        RemoveEnrichmentActivityFromHabitatRequest.builder()
                                .withActivityId(unauthReq.getActivityId())
                                .withHabitatId(unauthReq.getHabitatId())
                                .withKeeperManagerId(claims.get("email"))
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideRemoveEnrichmentActivityFromHabitatActivity().handleRequest(request)
        );
    }
}
