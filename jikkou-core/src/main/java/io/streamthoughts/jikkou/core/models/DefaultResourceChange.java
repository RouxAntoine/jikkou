/*
 * Copyright 2023 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.streamthoughts.jikkou.core.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.streamthoughts.jikkou.core.annotation.ApiVersion;
import io.streamthoughts.jikkou.core.annotation.Description;
import io.streamthoughts.jikkou.core.annotation.Kind;
import io.streamthoughts.jikkou.core.reconcilier.Change;
import java.beans.ConstructorProperties;
import java.util.Objects;
import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Description("")
@JsonPropertyOrder({
        "apiVersion",
        "kind",
        "metadata",
        "change"
})
@ApiVersion("core.jikkou.io/v1beta2")
@Kind("GenericResourceChange")
public class DefaultResourceChange<T extends Change> implements HasMetadataChange<T> {

    private final String kind;
    private final String apiVersion;
    private final ObjectMeta metadata;
    private final T change;

    public DefaultResourceChange(final T change) {
        this(null, null, null, change);
    }

    @ConstructorProperties({
            "apiVersion",
            "kind",
            "metadata",
            "change"
    })
    public DefaultResourceChange(final String kind,
                                 final String apiVersion,
                                 final ObjectMeta metadata,
                                 final T change) {
        this.kind = kind;
        this.apiVersion = apiVersion;
        this.metadata = metadata;
        this.change = change;
    }

    public static <T extends Change> DefaultResourceChangeBuilder<T> builder() {
        return new DefaultResourceChangeBuilder<T>();
    }

    /**
     * {@inheritDoc}
     **/
    @JsonProperty("kind")
    @Override
    public String getKind() {
        return Optional.ofNullable(kind).orElse(Resource.getKind(this.getClass()));
    }

    /**
     * {@inheritDoc}
     **/
    @JsonProperty("apiVersion")
    @Override
    public String getApiVersion() {
        return Optional.ofNullable(apiVersion).orElse(Resource.getApiVersion(this.getClass()));
    }

    /**
     * {@inheritDoc}
     **/
    @JsonProperty("metadata")
    @Override
    public ObjectMeta getMetadata() {
        return Optional.ofNullable(metadata).orElse(new ObjectMeta());
    }

    /**
     * {@inheritDoc}
     **/
    @JsonProperty("change")
    @Override
    public T getChange() {
        return change;
    }

    /**
     * {@inheritDoc}
     **/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultResourceChange<?> that = (DefaultResourceChange<?>) o;
        return Objects.equals(kind, that.kind) &&
                Objects.equals(apiVersion, that.apiVersion) &&
                Objects.equals(metadata, that.metadata) &&
                Objects.equals(change, that.change);
    }

    /**
     * {@inheritDoc}
     **/
    @Override
    public int hashCode() {
        return Objects.hash(kind, apiVersion, metadata, change);
    }

    @Override
    public String toString() {
        return "DefaultResourceChange[" +
                "kind=" + kind +
                ", apiVersion=" + apiVersion +
                ", metadata=" + metadata +
                ", change=" + change +
                ']';
    }

    public DefaultResourceChange<T> withKind(String kind) {
        return this.kind == kind ? this : new DefaultResourceChange<T>(kind, this.apiVersion, this.metadata, this.change);
    }

    public DefaultResourceChange<T> withApiVersion(String apiVersion) {
        return this.apiVersion == apiVersion ? this : new DefaultResourceChange<T>(this.kind, apiVersion, this.metadata, this.change);
    }

    public DefaultResourceChange<T> withMetadata(ObjectMeta metadata) {
        return this.metadata == metadata ? this : new DefaultResourceChange<T>(this.kind, this.apiVersion, metadata, this.change);
    }

    public DefaultResourceChange<T> withChange(T change) {
        return this.change == change ? this : new DefaultResourceChange<T>(this.kind, this.apiVersion, this.metadata, change);
    }

    public DefaultResourceChangeBuilder<T> toBuilder() {
        return new DefaultResourceChangeBuilder<T>().withKind(this.kind).withApiVersion(this.apiVersion).withMetadata(this.metadata).withChange(this.change);
    }

    public static class DefaultResourceChangeBuilder<T extends Change> {
        private String kind;
        private String apiVersion;
        private ObjectMeta metadata;
        private T change;

        DefaultResourceChangeBuilder() {
        }

        public DefaultResourceChangeBuilder<T> withKind(String kind) {
            this.kind = kind;
            return this;
        }

        public DefaultResourceChangeBuilder<T> withApiVersion(String apiVersion) {
            this.apiVersion = apiVersion;
            return this;
        }

        public DefaultResourceChangeBuilder<T> withMetadata(ObjectMeta metadata) {
            this.metadata = metadata;
            return this;
        }

        public DefaultResourceChangeBuilder<T> withChange(T change) {
            this.change = change;
            return this;
        }

        public DefaultResourceChange<T> build() {
            return new DefaultResourceChange<T>(this.kind, this.apiVersion, this.metadata, this.change);
        }
    }
}
