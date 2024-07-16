/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) The original authors
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.streamthoughts.jikkou.kafka;

import io.streamthoughts.jikkou.core.annotation.Provider;
import io.streamthoughts.jikkou.core.config.ConfigProperty;
import io.streamthoughts.jikkou.core.extension.ExtensionRegistry;
import io.streamthoughts.jikkou.core.models.change.GenericResourceChange;
import io.streamthoughts.jikkou.core.models.change.ResourceChange;
import io.streamthoughts.jikkou.core.resource.ResourceRegistry;
import io.streamthoughts.jikkou.kafka.action.KafkaConsumerGroupsResetOffsets;
import io.streamthoughts.jikkou.kafka.collections.V1KafkaBrokerList;
import io.streamthoughts.jikkou.kafka.collections.V1KafkaClientQuotaList;
import io.streamthoughts.jikkou.kafka.collections.V1KafkaPrincipalAuthorizationList;
import io.streamthoughts.jikkou.kafka.collections.V1KafkaTopicList;
import io.streamthoughts.jikkou.kafka.health.KafkaBrokerHealthIndicator;
import io.streamthoughts.jikkou.kafka.internals.KafkaUtils;
import io.streamthoughts.jikkou.kafka.internals.admin.AdminClientContextFactory;
import io.streamthoughts.jikkou.kafka.internals.admin.AdminClientFactory;
import io.streamthoughts.jikkou.kafka.internals.admin.DefaultAdminClientFactory;
import io.streamthoughts.jikkou.kafka.internals.consumer.ConsumerFactory;
import io.streamthoughts.jikkou.kafka.internals.consumer.DefaultConsumerFactory;
import io.streamthoughts.jikkou.kafka.internals.producer.DefaultProducerFactory;
import io.streamthoughts.jikkou.kafka.internals.producer.ProducerFactory;
import io.streamthoughts.jikkou.kafka.model.user.V1KafkaUser;
import io.streamthoughts.jikkou.kafka.models.V1KafkaBroker;
import io.streamthoughts.jikkou.kafka.models.V1KafkaClientQuota;
import io.streamthoughts.jikkou.kafka.models.V1KafkaConsumerGroup;
import io.streamthoughts.jikkou.kafka.models.V1KafkaPrincipalAuthorization;
import io.streamthoughts.jikkou.kafka.models.V1KafkaPrincipalRole;
import io.streamthoughts.jikkou.kafka.models.V1KafkaTableRecord;
import io.streamthoughts.jikkou.kafka.models.V1KafkaTopic;
import io.streamthoughts.jikkou.kafka.reconciler.AdminClientConsumerGroupCollector;
import io.streamthoughts.jikkou.kafka.reconciler.AdminClientConsumerGroupController;
import io.streamthoughts.jikkou.kafka.reconciler.AdminClientKafkaAclCollector;
import io.streamthoughts.jikkou.kafka.reconciler.AdminClientKafkaAclController;
import io.streamthoughts.jikkou.kafka.reconciler.AdminClientKafkaBrokerCollector;
import io.streamthoughts.jikkou.kafka.reconciler.AdminClientKafkaQuotaCollector;
import io.streamthoughts.jikkou.kafka.reconciler.AdminClientKafkaQuotaController;
import io.streamthoughts.jikkou.kafka.reconciler.AdminClientKafkaTableCollector;
import io.streamthoughts.jikkou.kafka.reconciler.AdminClientKafkaTableController;
import io.streamthoughts.jikkou.kafka.reconciler.AdminClientKafkaTopicCollector;
import io.streamthoughts.jikkou.kafka.reconciler.AdminClientKafkaTopicController;
import io.streamthoughts.jikkou.kafka.reconciler.AdminClientKafkaUserCollector;
import io.streamthoughts.jikkou.kafka.reconciler.AdminClientKafkaUserController;
import io.streamthoughts.jikkou.kafka.reporter.KafkaChangeReporter;
import io.streamthoughts.jikkou.kafka.transform.KafkaPrincipalAuthorizationTransformation;
import io.streamthoughts.jikkou.kafka.transform.KafkaTopicMaxNumPartitionsTransformation;
import io.streamthoughts.jikkou.kafka.transform.KafkaTopicMaxReplicasTransformation;
import io.streamthoughts.jikkou.kafka.transform.KafkaTopicMaxRetentionMsTransformation;
import io.streamthoughts.jikkou.kafka.transform.KafkaTopicMinInSyncReplicasTransformation;
import io.streamthoughts.jikkou.kafka.transform.KafkaTopicMinReplicasTransformation;
import io.streamthoughts.jikkou.kafka.transform.KafkaTopicMinRetentionMsTransformation;
import io.streamthoughts.jikkou.kafka.validation.ClientQuotaValidation;
import io.streamthoughts.jikkou.kafka.validation.NoDuplicatePrincipalAllowedValidation;
import io.streamthoughts.jikkou.kafka.validation.NoDuplicatePrincipalRoleValidation;
import io.streamthoughts.jikkou.kafka.validation.NoDuplicateTopicsAllowedValidation;
import io.streamthoughts.jikkou.kafka.validation.TopicConfigKeysValidation;
import io.streamthoughts.jikkou.kafka.validation.TopicMaxNumPartitionsValidation;
import io.streamthoughts.jikkou.kafka.validation.TopicMaxReplicationFactorValidation;
import io.streamthoughts.jikkou.kafka.validation.TopicMinNumPartitionsValidation;
import io.streamthoughts.jikkou.kafka.validation.TopicMinReplicationFactorValidation;
import io.streamthoughts.jikkou.kafka.validation.TopicNamePrefixValidation;
import io.streamthoughts.jikkou.kafka.validation.TopicNameRegexValidation;
import io.streamthoughts.jikkou.kafka.validation.TopicNameSuffixValidation;
import io.streamthoughts.jikkou.spi.BaseExtensionProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.jetbrains.annotations.NotNull;

/**
 * Extension provider for Apache Kafka.
 */
@Provider(
    name = "kafka",
    description = "Extension provider for Apache Kafka",
    tags = {"Apache Kafka"}
)
public final class KafkaExtensionProvider extends BaseExtensionProvider {

    private final ConfigProperty<Map<String, Object>> clientConfig = ConfigProperty
        .ofMap("client")
        .description("The kafka client configuration properties.")
        .orElse(HashMap::new);

    public AdminClientContextFactory newAdminClientContextFactory() {
        return new AdminClientContextFactory(configuration, newAdminClientFactory());
    }

    public AdminClientFactory newAdminClientFactory() {
        return new DefaultAdminClientFactory(
            clientConfig.map(KafkaUtils::getAdminClientConfigs).get(configuration)
        );
    }

    public ConsumerFactory<byte[], byte[]> newConsumerFactory() {
        return newConsumerFactory(new ByteArrayDeserializer(), new ByteArrayDeserializer());
    }

    public <K, V> ConsumerFactory<K, V> newConsumerFactory(final Deserializer<K> keyDeserializer,
                                                           final Deserializer<V> valueDeserializer) {
        return new DefaultConsumerFactory<>(
            clientConfig.map(KafkaUtils::getConsumerClientConfigs).get(configuration),
            keyDeserializer,
            valueDeserializer
        );
    }

    public <K, V> ProducerFactory<K, V> newProducerFactory(final Serializer<K> keySerializer,
                                                           final Serializer<V> valueSerializer) {
        return new DefaultProducerFactory<>(
            clientConfig.map(KafkaUtils::getProducerClientConfigs).get(configuration),
            keySerializer,
            valueSerializer
        );
    }

    /**
     * {@inheritDoc}
     **/
    @Override
    public void registerExtensions(@NotNull ExtensionRegistry registry) {

        // controllers
        registry.register(KafkaBrokerHealthIndicator.class, KafkaBrokerHealthIndicator::new);
        registry.register(AdminClientKafkaAclController.class, AdminClientKafkaAclController::new);
        registry.register(AdminClientKafkaTopicController.class, AdminClientKafkaTopicController::new);
        registry.register(AdminClientKafkaQuotaController.class, AdminClientKafkaQuotaController::new);
        registry.register(AdminClientConsumerGroupController.class, AdminClientConsumerGroupController::new);
        registry.register(AdminClientKafkaUserController.class, AdminClientKafkaUserController::new);

        // collectors
        registry.register(AdminClientKafkaBrokerCollector.class, AdminClientKafkaBrokerCollector::new);
        registry.register(AdminClientKafkaQuotaCollector.class, AdminClientKafkaQuotaCollector::new);
        registry.register(AdminClientKafkaTopicCollector.class, AdminClientKafkaTopicCollector::new);
        registry.register(AdminClientKafkaAclCollector.class, AdminClientKafkaAclCollector::new);
        registry.register(AdminClientKafkaTableCollector.class, AdminClientKafkaTableCollector::new);
        registry.register(AdminClientKafkaTableController.class, AdminClientKafkaTableController::new);
        registry.register(AdminClientConsumerGroupCollector.class, AdminClientConsumerGroupCollector::new);
        registry.register(AdminClientKafkaUserCollector.class, AdminClientKafkaUserCollector::new);

        // transformations
        registry.register(KafkaPrincipalAuthorizationTransformation.class, KafkaPrincipalAuthorizationTransformation::new);
        registry.register(KafkaTopicMaxRetentionMsTransformation.class, KafkaTopicMaxRetentionMsTransformation::new);
        registry.register(KafkaTopicMaxReplicasTransformation.class, KafkaTopicMaxReplicasTransformation::new);
        registry.register(KafkaTopicMaxNumPartitionsTransformation.class, KafkaTopicMaxNumPartitionsTransformation::new);
        registry.register(KafkaTopicMinRetentionMsTransformation.class, KafkaTopicMinRetentionMsTransformation::new);
        registry.register(KafkaTopicMinReplicasTransformation.class, KafkaTopicMinReplicasTransformation::new);
        registry.register(KafkaTopicMinInSyncReplicasTransformation.class, KafkaTopicMinInSyncReplicasTransformation::new);

        // validations
        registry.register(ClientQuotaValidation.class, ClientQuotaValidation::new);
        registry.register(NoDuplicatePrincipalRoleValidation.class, NoDuplicatePrincipalRoleValidation::new);
        registry.register(NoDuplicateTopicsAllowedValidation.class, NoDuplicateTopicsAllowedValidation::new);
        registry.register(NoDuplicatePrincipalAllowedValidation.class, NoDuplicatePrincipalAllowedValidation::new);
        registry.register(TopicConfigKeysValidation.class, TopicConfigKeysValidation::new);
        registry.register(TopicMinReplicationFactorValidation.class, TopicMinReplicationFactorValidation::new);
        registry.register(TopicMaxReplicationFactorValidation.class, TopicMaxReplicationFactorValidation::new);
        registry.register(TopicMinNumPartitionsValidation.class, TopicMinNumPartitionsValidation::new);
        registry.register(TopicMaxNumPartitionsValidation.class, TopicMaxNumPartitionsValidation::new);
        registry.register(TopicNameRegexValidation.class, TopicNameRegexValidation::new);
        registry.register(TopicNamePrefixValidation.class, TopicNamePrefixValidation::new);
        registry.register(TopicNameSuffixValidation.class, TopicNameSuffixValidation::new);

        // reporters
        registry.register(KafkaChangeReporter.class, KafkaChangeReporter::new);

        // actions
        registry.register(KafkaConsumerGroupsResetOffsets.class, KafkaConsumerGroupsResetOffsets::new);
    }

    /**
     * {@inheritDoc}
     **/
    @Override
    public void registerResources(@NotNull ResourceRegistry registry) {
        Stream.of(
            V1KafkaBroker.class,
            V1KafkaClientQuota.class,
            V1KafkaTopic.class,
            V1KafkaPrincipalAuthorization.class,
            V1KafkaPrincipalAuthorizationList.class,
            V1KafkaPrincipalRole.class,
            V1KafkaTableRecord.class,
            V1KafkaConsumerGroup.class,
            V1KafkaUser.class
        ).forEach(resource -> {
            registry.register(resource);
            registry.register(GenericResourceChange.class, ResourceChange.fromResource(resource));
        });

        // register collections
        Stream.of(
            V1KafkaBrokerList.class,
            V1KafkaClientQuotaList.class,
            V1KafkaTopicList.class
        ).forEach(registry::register);
    }
}
