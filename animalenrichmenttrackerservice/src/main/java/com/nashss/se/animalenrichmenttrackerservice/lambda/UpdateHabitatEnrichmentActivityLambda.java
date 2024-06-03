package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.UpdateHabitatEnrichmentActivityRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.UpdateHabitatEnrichmentActivityResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class UpdateHabitatEnrichmentActivityLambda
        extends LambdaActivityRunner<UpdateHabitatEnrichmentActivityRequest, UpdateHabitatEnrichmentActivityResult>
        implements RequestHandler<AuthenticatedLambdaRequest<UpdateHabitatEnrichmentActivityRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<UpdateHabitatEnrichmentActivityRequest> input,
                                        Context context) {
        return super.runActivity(
            () -> {
                UpdateHabitatEnrichmentActivityRequest unauthReq =
                        input.fromBody(UpdateHabitatEnrichmentActivityRequest.class);
                return input.fromUserClaims(claims ->
                        UpdateHabitatEnrichmentActivityRequest.builder()
                                .withHabitatId(unauthReq.getHabitatId())
                                .withActivityId(unauthReq.getActivityId())
                                .withDateCompleted(unauthReq.getDateCompleted())
                                .withKeeperRating(unauthReq.getKeeperRating())
                                .withIsComplete(unauthReq.getIsComplete())
                                .withKeeperManagerId(claims.get("email"))
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideUpdateHabitatEnrichmentActivityActivity().handleRequest(request)
        );
    }
}
