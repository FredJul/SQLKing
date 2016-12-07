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
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.frju.androidquery.integration.utils;

import net.frju.androidquery.gen.Q;
import net.frju.androidquery.integration.models.User;

/**
 * The test data is a hard dependency of all the integration tests, changing
 * these constants will cause failures in the ReadTest suite. It is advised that
 * new constants are added if further test suites are required
 *
 * @author Samuel Kirton [sam@memtrip.com]
 */
public class SetupUser {
    public static final int ANGIE_ID = 1;
    public static final String ANGIE_USER_NAME = "angie";
    public static final long ANGIE_TIMESTAMP = 123456789;
    public static final boolean ANGIE_IS_REGISTERED = true;
    public static final double ANGIE_RATING = 100.2342;
    public static final int ANGIE_COUNT = 100;
    public static final int ANGIE_LOG_ID = 1;

    public static final int JOSH_ID = 2;
    public static final String JOSH_USER_NAME = "josh";
    public static final long JOSH_TIMESTAMP = 23456789;
    public static final boolean JOSH_IS_REGISTERED = true;
    public static final double JOSH_RATING = 10.2342;
    public static final int JOSH_COUNT = 100;

    public static final int GILL_ID = 3;
    public static final String GILL_USER_NAME = "gill";
    public static final long GILL_TIMESTAMP = 3456789;
    public static final boolean GILL_IS_REGISTERED = false;
    public static final double GILL_RATING = 3.22;
    public static final int GILL_COUNT = 7;

    public static final int CLYDE_ID = 4;
    public static final String CLYDE_USER_NAME = "clyde";
    public static final long CLYDE_TIMESTAMP = 456789;
    public static final boolean CLYDE_IS_REGISTERED = false;
    public static final double CLYDE_RATING = 90.2;
    public static final int CLYDE_COUNT = 100;

    public void setupFourTestUsers() {
        User[] users = {
                createUser(
                        ANGIE_ID,
                        ANGIE_USER_NAME,
                        ANGIE_TIMESTAMP,
                        ANGIE_IS_REGISTERED,
                        ANGIE_RATING,
                        ANGIE_COUNT,
                        ANGIE_LOG_ID
                ),
                createUser(
                        JOSH_ID,
                        JOSH_USER_NAME,
                        JOSH_TIMESTAMP,
                        JOSH_IS_REGISTERED,
                        JOSH_RATING,
                        JOSH_COUNT,
                        0
                ),
                createUser(
                        GILL_ID,
                        GILL_USER_NAME,
                        GILL_TIMESTAMP,
                        GILL_IS_REGISTERED,
                        GILL_RATING,
                        GILL_COUNT,
                        0
                ),
                createUser(
                        CLYDE_ID,
                        CLYDE_USER_NAME,
                        CLYDE_TIMESTAMP,
                        CLYDE_IS_REGISTERED,
                        CLYDE_RATING,
                        CLYDE_COUNT,
                        0
                ),
        };

        Q.User.insert(users).query();
    }

    public void tearDownFourTestUsers() {
        Q.User.delete().query();
    }

    public static User createUser(int id,
                                  String username,
                                  long timestamp,
                                  boolean isRegistered,
                                  double rating,
                                  int count,
                                  int logId) {

        User user = new User();
        user.id = id;
        user.username = username;
        user.timestamp = timestamp;
        user.isRegistered = isRegistered;
        user.rating = rating;
        user.count = count;
        user.logId = logId;

        return user;
    }
}