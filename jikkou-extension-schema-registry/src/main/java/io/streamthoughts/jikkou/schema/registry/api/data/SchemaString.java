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
package io.streamthoughts.jikkou.schema.registry.api.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.streamthoughts.jikkou.annotation.Reflectable;
import java.util.Objects;

@Reflectable
public final class SchemaString {

    private final String schema;

    /**
     * Creates a new {@link SchemaString} instance.
     *
     * @param schema a schema string.
     */
    @JsonCreator
    public SchemaString(@JsonProperty("schema") String schema) {
        this.schema = schema;
    }

    /**
     * Gets a schema string.
     *
     * @return a schema string.
     */
    public String schema() {
        return schema;
    }

    /** {@inheritDoc} **/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchemaString that = (SchemaString) o;
        return Objects.equals(schema, that.schema);
    }

    /** {@inheritDoc} **/
    @Override
    public int hashCode() {
        return Objects.hash(schema);
    }

    /** {@inheritDoc} **/
    @Override
    public String
    toString() {
        return "{schema=" + schema +  '}';
    }
}
