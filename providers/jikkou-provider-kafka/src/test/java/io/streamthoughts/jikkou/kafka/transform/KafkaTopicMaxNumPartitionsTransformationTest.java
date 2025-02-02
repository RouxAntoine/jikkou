/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) The original authors
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.streamthoughts.jikkou.kafka.transform;

import static io.streamthoughts.jikkou.kafka.transform.KafkaTopicMaxNumPartitionsTransformation.MAX_NUM_PARTITIONS_CONFIG;

import io.streamthoughts.jikkou.core.ReconciliationContext;
import io.streamthoughts.jikkou.core.extension.ExtensionContext;
import io.streamthoughts.jikkou.core.models.ResourceList;
import io.streamthoughts.jikkou.kafka.models.V1KafkaTopic;
import io.streamthoughts.jikkou.kafka.models.V1KafkaTopicSpec;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class KafkaTopicMaxNumPartitionsTransformationTest {

    KafkaTopicMaxNumPartitionsTransformation transformation;

    @BeforeEach
    void beforeEach() {
        ExtensionContext context = Mockito.mock(ExtensionContext.class);
        Mockito.when(context.configuration()).thenReturn(MAX_NUM_PARTITIONS_CONFIG.asConfiguration(10));
        transformation = new KafkaTopicMaxNumPartitionsTransformation();
        transformation.init(context);
    }

    @Test
    void shouldEnforceConstraintForInvalidValue() {
        // Given
        V1KafkaTopic resource = V1KafkaTopic.builder()
                .withSpec(V1KafkaTopicSpec
                        .builder()
                        .withPartitions(100)
                        .build())
                .build();
        // When
        Optional<V1KafkaTopic> result = transformation
                .transform(resource, ResourceList.empty(), ReconciliationContext.Default.EMPTY);

        // Then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPresent());

        V1KafkaTopic transformed = result.get();
        Assertions.assertEquals(10, transformed.getSpec().getPartitions());
    }

    @Test
    void shouldNotEnforceConstraintForValidValue() {
        // Given
        V1KafkaTopic resource = V1KafkaTopic.builder()
                .withSpec(V1KafkaTopicSpec
                        .builder()
                        .withPartitions(4)
                        .build())
                .build();
        // When
        Optional<V1KafkaTopic> result = transformation
                .transform(resource, ResourceList.empty(), ReconciliationContext.Default.EMPTY);

        // Then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPresent());

        V1KafkaTopic transformed = result.get();
        Assertions.assertEquals(4, transformed.getSpec().getPartitions());
    }
}