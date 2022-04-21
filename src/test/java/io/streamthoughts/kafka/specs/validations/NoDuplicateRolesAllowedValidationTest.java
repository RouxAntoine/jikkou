/*
 * Copyright 2021 StreamThoughts.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.streamthoughts.kafka.specs.validations;

import io.streamthoughts.kafka.specs.model.V1AccessRoleObject;
import io.streamthoughts.kafka.specs.model.V1SecurityObject;
import io.streamthoughts.kafka.specs.model.V1SpecObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

public class NoDuplicateRolesAllowedValidationTest {

    private static final V1AccessRoleObject TEST_ROLE = new V1AccessRoleObject("role", Collections.emptyList());

    private final NoDuplicateRolesAllowedValidation validation = new NoDuplicateRolesAllowedValidation();

    @Test
    public void should_throw_validation_exception_given_duplicate() {
        Assertions.assertThrows(ValidationException.class, () -> {
            final V1SpecObject object = V1SpecObject.withSecurity(
                    V1SecurityObject.withRoles(List.of(TEST_ROLE, TEST_ROLE))
            );
            validation.validate(object);
        });
    }

    @Test
    public void should_not_throw_validation_exception_given_duplicate() {
        Assertions.assertThrows(ValidationException.class, () -> {
            final V1SpecObject object = V1SpecObject.withSecurity(
                    V1SecurityObject.withRoles(List.of(TEST_ROLE, TEST_ROLE))
            );
            validation.validate(object);
        });
    }
}