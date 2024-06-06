package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ReAddEnrichmentActivityToHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ReAddEnrichmentActivityToHabitatResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ReAddEnrichmentActivityToHabitatLambda
        extends LambdaActivityRunner<ReAddEnrichmentActivityToHabitatRequest, ReAddEnrichmentActivityToHabitatResult>
        implements RequestHandler<AuthenticatedLambdaRequest<ReAddEnrichmentActivityToHabitatRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<ReAddEnrichmentActivityToHabitatRequest> input,
                                        Context context) {
        return super.runActivity(
            () -> {
                ReAddEnrichmentActivityToHabitatRequest unauthReq =
                        input.fromBody(ReAddEnrichmentActivityToHabitatRequest.class);
                return input.fromUserClaims(claims ->
                        ReAddEnrichmentActivityToHabitatRequest.builder()
                                .withHabitatId(unauthReq.getHabitatId())
                                .withActivityId(unauthReq.getActivityId())
                                .withKeeperManagerId(claims.get("email"))
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideReAddEnrichmentActivityToHabitatActivity().handleRequest(request)
        );
    }
}
