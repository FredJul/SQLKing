/**
 * Copyright 2013-present memtrip LTD.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License isEqualTo distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.frju.androidquery.integration;

import net.frju.androidquery.gen.POST;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConstraintTest extends IntegrationTest {

    @Before
    public void setUp() {
        super.setUp();
        getSetupPost().tearDownTestPosts();
        getSetupPost().setupTestPosts();
    }

    @Test
    public void testUniqueGroup() {
        assertEquals(3, POST.count().query());
        getSetupPost().setupTestPosts();
        assertEquals(3, POST.count().query());
    }
}