/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) The original authors
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.streamthoughts.jikkou.kafka.transform;

import static io.streamthoughts.jikkou.kafka.transform.KafkaTopicMinInSyncReplicasTransformation.MIN_INSYNC_REPLICAS_CONFIG;

import io.streamthoughts.jikkou.core.ReconciliationContext;
import io.streamthoughts.jikkou.core.extension.ExtensionContext;
import io.streamthoughts.jikkou.core.models.Configs;
import io.streamthoughts.jikkou.core.models.ResourceListObject;
import io.streamthoughts.jikkou.kafka.models.V1KafkaTopic;
import io.streamthoughts.jikkou.kafka.models.V1KafkaTopicSpec;
import java.util.Optional;
import org.apache.kafka.common.config.TopicConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class KafkaTopicMinInSyncReplicasTransformationTest {

    KafkaTopicMinInSyncReplicasTransformation transformation;

    @BeforeEach
    void beforeEach() {
        ExtensionContext context = Mockito.mock(ExtensionContext.class);
        Mockito.when(context.appConfiguration()).thenReturn(MIN_INSYNC_REPLICAS_CONFIG.asConfiguration(2));
        transformation = new KafkaTopicMinInSyncReplicasTransformation();
        transformation.init(context);
    }


    @Test
    void shouldEnforceConstraintForInvalidValue() {
        // Given
        V1KafkaTopic resource = V1KafkaTopic.builder()
                .withSpec(V1KafkaTopicSpec
                        .builder()
                        .withConfigs(Configs.of(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, 1))
                        .build())
                .build();
        // When
        Optional<V1KafkaTopic> result = transformation
                .transform(resource, ResourceListObject.empty(), ReconciliationContext.Default.EMPTY);

        // Then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPresent());

        V1KafkaTopic transformed = result.get();
        Assertions.assertEquals(
                2,
                transformed.getSpec().getConfigs().get(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG).value()
        );
    }

    @Test
    void shouldEnforceConstraintForMissingValue() {
        // Given
        V1KafkaTopic resource = V1KafkaTopic.builder()
                .withSpec(V1KafkaTopicSpec
                        .builder()
                        .build())
                .build();
        // When
        Optional<V1KafkaTopic> result = transformation
                .transform(resource, ResourceListObject.empty(), ReconciliationContext.Default.EMPTY);

        // Then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPresent());

        V1KafkaTopic transformed = result.get();
        Assertions.assertEquals(
                2,
                transformed.getSpec().getConfigs().get(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG).value()
        );
    }

    @Test
    void shouldNotEnforceConstraintForValidResource() {
        // Given
        V1KafkaTopic resource = V1KafkaTopic.builder()
                .withSpec(V1KafkaTopicSpec
                        .builder()
                        .withConfigs(Configs.of(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, 3))
                        .build())
                .build();
        // When
        Optional<V1KafkaTopic> result = transformation
                .transform(resource, ResourceListObject.empty(), ReconciliationContext.Default.EMPTY);

        // Then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPresent());

        V1KafkaTopic transformed = result.get();
        Assertions.assertEquals(
                3,
                transformed.getSpec().getConfigs().get(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG).value()
        );
    }
}