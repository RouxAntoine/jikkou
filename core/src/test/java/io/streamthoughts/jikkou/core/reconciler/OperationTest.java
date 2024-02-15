/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) The original authors
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.streamthoughts.jikkou.core.reconciler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OperationTest {

    @Test
    void testHumanize() {
        Assertions.assertEquals("Create", Operation.CREATE.humanize());
        Assertions.assertEquals("Delete", Operation.DELETE.humanize());
        Assertions.assertEquals("Update", Operation.UPDATE.humanize());
        Assertions.assertEquals("Unchanged", Operation.NONE.humanize());
    }
}