package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.RemoveEnrichmentActivityRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.RemoveEnrichmentActivityResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class RemoveEnrichmentActivityLambda
        extends LambdaActivityRunner<RemoveEnrichmentActivityRequest, RemoveEnrichmentActivityResult>
        implements RequestHandler<AuthenticatedLambdaRequest<RemoveEnrichmentActivityRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<RemoveEnrichmentActivityRequest> input,
                                        Context context) {
        return super.runActivity(
            () -> {
                RemoveEnrichmentActivityRequest authReq = input.fromUserClaims(claims ->
                        RemoveEnrichmentActivityRequest.builder()
                                .withKeeperManagerId(claims.get("email"))
                                .build());
                return input.fromPath(path ->
                        RemoveEnrichmentActivityRequest.builder()
                                .withActivityId(path.get("activityId"))
                                .withKeeperManagerId(authReq.getKeeperManagerId())
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideRemoveEnrichmentActivityActivity().handleRequest(request)
        );
    }
}
