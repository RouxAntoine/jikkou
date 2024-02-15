/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) The original authors
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.streamthoughts.jikkou.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.streamthoughts.jikkou.core.annotation.Reflectable;
import java.beans.ConstructorProperties;
import java.util.Collections;
import java.util.Map;

@JsonPropertyOrder({
        "name",
        "description",
        "metadata",
})
@Reflectable
public record ApiHealthIndicator(@JsonProperty("name") String name,
                                 @JsonProperty("description") String description,
                                 @JsonProperty("metadata") Map<String, Object> metadata) {

    @ConstructorProperties({
            "name",
            "description",
            "metadata",
    })
    public ApiHealthIndicator {

    }

    public ApiHealthIndicator(String name, String description) {
        this(name, description, Collections.emptyMap());
    }
}
