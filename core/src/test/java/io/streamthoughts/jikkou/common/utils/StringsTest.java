/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) The original authors
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.streamthoughts.jikkou.common.utils;

import java.util.Properties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringsTest {

    @Test
    void shouldReturnTrueForNullString() {
        Assertions.assertTrue(Strings.isBlank(null));
    }

    @Test
    void shouldReturnTrueForEmptyString() {
        Assertions.assertTrue(Strings.isBlank(""));
    }

    @Test
    void shouldReturnTrueForBlankString() {
        Assertions.assertTrue(Strings.isBlank("     "));
    }

    @Test
    void shouldReturnFalseForNonEmptyString() {
        Assertions.assertFalse(Strings.isBlank("dummy"));
    }

    @Test
    void shouldGetPropertiesWhenParsingValidString() {
        Properties properties = Strings.toProperties("a=1,b=2,c=3");
        Assertions.assertEquals(new Properties(){{
            setProperty("a", "1");
            setProperty("b", "2");
            setProperty("c", "3");
        }}, properties);
    }
    
    @Test
    void shouldPruneSuffixedString() {
        Assertions.assertEquals("string", Strings.pruneSuffix( "string-suffix", "-suffix"));
    }
}