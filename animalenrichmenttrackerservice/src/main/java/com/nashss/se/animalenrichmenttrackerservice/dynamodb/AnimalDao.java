package com.nashss.se.animalenrichmenttrackerservice.dynamodb;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Animal;
import com.nashss.se.animalenrichmenttrackerservice.metrics.MetricsPublisher;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Accesses data for an Animal using {@link Animal} to represent the model in DynamoDB.
 */
@Singleton
public class AnimalDao {
    private final DynamoDBMapper dynamoDBMapper;
    private final MetricsPublisher metricsPublisher;

    /**
     * nstantiates a new AnimalDao object.
     *
     * @param dynamoDBMapper the {@link DynamoDBMapper} used to interact with the Animals table.
     * @param metricsPublisher the {@link MetricsPublisher} used to record metrics.
     */
    @Inject
    public AnimalDao(DynamoDBMapper dynamoDBMapper, MetricsPublisher metricsPublisher) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.metricsPublisher = metricsPublisher;
    }
}
