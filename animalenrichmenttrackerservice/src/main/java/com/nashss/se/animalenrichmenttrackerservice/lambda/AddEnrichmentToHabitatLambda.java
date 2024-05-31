package com.nashss.se.animalenrichmenttrackerservice.lambda;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.AddEnrichmentToHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.AddEnrichmentToHabitatResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class AddEnrichmentToHabitatLambda extends LambdaActivityRunner<AddEnrichmentToHabitatRequest,
        AddEnrichmentToHabitatResult>
        implements RequestHandler<AuthenticatedLambdaRequest<AddEnrichmentToHabitatRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<AddEnrichmentToHabitatRequest> input,
                                        Context context) {
        return super.runActivity(
            () -> {
                AddEnrichmentToHabitatRequest unauthReq = input.fromBody(AddEnrichmentToHabitatRequest.class);
                return input.fromUserClaims(claims ->
                        AddEnrichmentToHabitatRequest.builder()
                                .withHabitatId(unauthReq.getHabitatId())
                                .withEnrichmentId(unauthReq.getEnrichmentId())
                                .withDateCompleted(unauthReq.getDateCompleted())
                                .withKeeperRating(unauthReq.getKeeperRating())
                                .withKeeperManagerId(claims.get("email"))
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideAddEnrichmentToHabitatActivity().handleRequest(request)
        );
    }
}
