/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) The original authors
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.streamthoughts.jikkou.kafka.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.streamthoughts.jikkou.core.annotation.ApiVersion;
import io.streamthoughts.jikkou.core.annotation.Description;
import io.streamthoughts.jikkou.core.annotation.Kind;
import io.streamthoughts.jikkou.core.annotation.Names;
import io.streamthoughts.jikkou.core.annotation.Reflectable;
import io.streamthoughts.jikkou.core.annotation.Verbs;
import io.streamthoughts.jikkou.core.models.HasMetadata;
import io.streamthoughts.jikkou.core.models.HasSpec;
import io.streamthoughts.jikkou.core.models.ObjectMeta;
import io.streamthoughts.jikkou.core.models.ObjectTemplate;
import io.streamthoughts.jikkou.core.models.Resource;
import io.streamthoughts.jikkou.core.models.Verb;
import java.beans.ConstructorProperties;
import javax.annotation.processing.Generated;
import lombok.Builder;
import lombok.With;
import lombok.extern.jackson.Jacksonized;


/**
 * V1KafkaTopic
 * <p>
 * KafkaTopic resources provide a way of managing topics in a Kafka cluster.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(builderMethodName = "builder", toBuilder = true, setterPrefix = "with")
@With
@Description("KafkaTopic resources provide a way of managing topics in a Kafka cluster.")
@Names(singular = "kafkatopic", plural = "kafkatopics", shortNames = {
    "kt"
})
@Verbs({
    Verb.LIST,
    Verb.CREATE,
    Verb.UPDATE,
    Verb.DELETE,
    Verb.GET,
    Verb.APPLY
})
@JsonPropertyOrder({
    "apiVersion",
    "kind",
    "metadata",
    "template",
    "spec",
    "status"
})
@ApiVersion("kafka.jikkou.io/v1beta2")
@Kind("KafkaTopic")
@Jacksonized
@Reflectable
@Generated("jsonschema2pojo")
public class V1KafkaTopic implements HasMetadata, HasSpec<V1KafkaTopicSpec> , Resource
{

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("apiVersion")
    @Builder.Default
    private String apiVersion = "kafka.jikkou.io/v1beta2";
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("kind")
    @Builder.Default
    private String kind = "KafkaTopic";
    /**
     * ObjectMeta
     * <p>
     * Metadata attached to the resources.
     * (Required)
     * 
     */
    @JsonProperty("metadata")
    @JsonPropertyDescription("Metadata attached to the resources.")
    private ObjectMeta metadata;
    /**
     * ObjectTemplate
     * <p>
     * Data values to be passed to the template engine.
     * 
     */
    @JsonProperty("template")
    @JsonPropertyDescription("Data values to be passed to the template engine.")
    private ObjectTemplate template;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("spec")
    private io.streamthoughts.jikkou.kafka.models.V1KafkaTopicSpec spec;
    @JsonProperty("status")
    private V1KafkaTopicStatus status;

    /**
     * No args constructor for use in serialization
     * 
     */
    public V1KafkaTopic() {
    }

    /**
     * 
     * @param template
     * @param metadata
     * @param apiVersion
     * @param kind
     * @param spec
     * @param status
     */
    @ConstructorProperties({
        "apiVersion",
        "kind",
        "metadata",
        "template",
        "spec",
        "status"
    })
    public V1KafkaTopic(String apiVersion, String kind, ObjectMeta metadata, ObjectTemplate template, io.streamthoughts.jikkou.kafka.models.V1KafkaTopicSpec spec, V1KafkaTopicStatus status) {
        super();
        this.apiVersion = apiVersion;
        this.kind = kind;
        this.metadata = metadata;
        this.template = template;
        this.spec = spec;
        this.status = status;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("apiVersion")
    public String getApiVersion() {
        return apiVersion;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("kind")
    public String getKind() {
        return kind;
    }

    /**
     * ObjectMeta
     * <p>
     * Metadata attached to the resources.
     * (Required)
     * 
     */
    @JsonProperty("metadata")
    public ObjectMeta getMetadata() {
        return metadata;
    }

    /**
     * ObjectTemplate
     * <p>
     * Data values to be passed to the template engine.
     * 
     */
    @JsonProperty("template")
    public ObjectTemplate getTemplate() {
        return template;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("spec")
    public io.streamthoughts.jikkou.kafka.models.V1KafkaTopicSpec getSpec() {
        return spec;
    }

    @JsonProperty("status")
    public V1KafkaTopicStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(V1KafkaTopic.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("apiVersion");
        sb.append('=');
        sb.append(((this.apiVersion == null)?"<null>":this.apiVersion));
        sb.append(',');
        sb.append("kind");
        sb.append('=');
        sb.append(((this.kind == null)?"<null>":this.kind));
        sb.append(',');
        sb.append("metadata");
        sb.append('=');
        sb.append(((this.metadata == null)?"<null>":this.metadata));
        sb.append(',');
        sb.append("template");
        sb.append('=');
        sb.append(((this.template == null)?"<null>":this.template));
        sb.append(',');
        sb.append("spec");
        sb.append('=');
        sb.append(((this.spec == null)?"<null>":this.spec));
        sb.append(',');
        sb.append("status");
        sb.append('=');
        sb.append(((this.status == null)?"<null>":this.status));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.template == null)? 0 :this.template.hashCode()));
        result = ((result* 31)+((this.metadata == null)? 0 :this.metadata.hashCode()));
        result = ((result* 31)+((this.apiVersion == null)? 0 :this.apiVersion.hashCode()));
        result = ((result* 31)+((this.kind == null)? 0 :this.kind.hashCode()));
        result = ((result* 31)+((this.spec == null)? 0 :this.spec.hashCode()));
        result = ((result* 31)+((this.status == null)? 0 :this.status.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof V1KafkaTopic) == false) {
            return false;
        }
        V1KafkaTopic rhs = ((V1KafkaTopic) other);
        return (((((((this.template == rhs.template)||((this.template!= null)&&this.template.equals(rhs.template)))&&((this.metadata == rhs.metadata)||((this.metadata!= null)&&this.metadata.equals(rhs.metadata))))&&((this.apiVersion == rhs.apiVersion)||((this.apiVersion!= null)&&this.apiVersion.equals(rhs.apiVersion))))&&((this.kind == rhs.kind)||((this.kind!= null)&&this.kind.equals(rhs.kind))))&&((this.spec == rhs.spec)||((this.spec!= null)&&this.spec.equals(rhs.spec))))&&((this.status == rhs.status)||((this.status!= null)&&this.status.equals(rhs.status))));
    }

}
