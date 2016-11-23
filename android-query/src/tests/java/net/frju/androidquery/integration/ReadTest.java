/**
 * Copyright 2013-present memtrip LTD.
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
package net.frju.androidquery.integration;

import net.frju.androidquery.gen.Q;
import net.frju.androidquery.integration.models.User;
import net.frju.androidquery.integration.utils.SetupUser;
import net.frju.androidquery.operation.clause.Where;
import net.frju.androidquery.operation.function.Select;
import net.frju.androidquery.operation.keyword.OrderBy;

import org.junit.Before;

import static net.frju.androidquery.operation.clause.And.and;
import static net.frju.androidquery.operation.clause.In.in;
import static net.frju.androidquery.operation.clause.Or.or;
import static net.frju.androidquery.operation.clause.Where.where;

/**
 * @author Samuel Kirton [sam@memtrip.com]
 */
public class ReadTest extends IntegrationTest {

    @Before
    public void setUp() {
        super.setUp();
        getSetupUser().tearDownFourTestUsers(getSQLProvider());
        getSetupUser().setupFourTestUsers(getSQLProvider());
    }

    @org.junit.Test
    public void testAllUsersAreSelected() {
        User[] users = Select.getBuilder().query(User.class, getSQLProvider());

        // 4 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 4 rows will be selected
        assertEquals(4, users.length);
    }

    @org.junit.Test
    public void testEqualToSingleSelection() {
        User user = Select.getBuilder()
                .where(where(Q.User.USERNAME, Where.Op.EQUAL_TO, SetupUser.CLYDE_USER_NAME))
                .querySingle(User.class, getSQLProvider());

        assertEquals(SetupUser.CLYDE_USER_NAME, user.getUsername());
    }

    @org.junit.Test
    public void testEqualToBooleanSelection() {
        User[] users = Select.getBuilder()
                .where(where(Q.User.IS_REGISTERED, Where.Op.EQUAL_TO, true))
                .query(User.class, getSQLProvider());

        // 2 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 2 rows will be selected
        assertEquals(2, users.length);
    }

    @org.junit.Test
    public void testEqualToLongSelection() {
        User user = Select.getBuilder()
                .where(where(Q.User.TIMESTAMP, Where.Op.EQUAL_TO, SetupUser.CLYDE_TIMESTAMP))
                .querySingle(User.class, getSQLProvider());

        assertEquals(SetupUser.CLYDE_USER_NAME, user.getUsername());
        assertEquals(SetupUser.CLYDE_TIMESTAMP, user.getTimestamp());
        assertEquals(SetupUser.CLYDE_IS_REGISTERED, user.getIsRegistered());
    }

    @org.junit.Test
    public void testMoreThanSelection() {
        User[] users = Select.getBuilder()
                .where(where(Q.User.TIMESTAMP, Where.Op.MORE_THAN, SetupUser.CLYDE_TIMESTAMP))
                .query(User.class, getSQLProvider());

        // 3 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 3 rows will be selected
        assertEquals(3, users.length);
    }

    @org.junit.Test
    public void testMoreThanOrEqualToSelection() {
        User[] users = Select.getBuilder()
                .where(where(Q.User.TIMESTAMP, Where.Op.MORE_THAN_OR_EQUAL_TO, SetupUser.CLYDE_TIMESTAMP))
                .query(User.class, getSQLProvider());

        // All 4 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 4 rows will be selected
        assertEquals(4, users.length);
    }

    @org.junit.Test
    public void testLessThanSelection() {
        User[] users = Select.getBuilder()
                .where(where(Q.User.TIMESTAMP, Where.Op.LESS_THAN, SetupUser.ANGIE_TIMESTAMP))
                .query(User.class, getSQLProvider());

        // 3 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 3 rows will be selected
        assertEquals(3, users.length);
    }

    @org.junit.Test
    public void testLessThanOrEqualToSelection() {
        User[] users = Select.getBuilder()
                .where(where(Q.User.TIMESTAMP, Where.Op.LESS_THAN_OR_EQUAL_TO, SetupUser.ANGIE_TIMESTAMP))
                .query(User.class, getSQLProvider());

        // 4 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 4 rows will be selected
        assertEquals(4, users.length);
    }

    @org.junit.Test
    public void testLikeStartingWithSelection() {
        User[] users = Select.getBuilder()
                .where(where(Q.User.USERNAME, Where.Op.LIKE, "jo%"))
                .query(User.class, getSQLProvider());

        // 1 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 1 rows will be selected
        assertEquals(1, users.length);
    }

    @org.junit.Test
    public void testLikeEndingWithSelection() {
        User[] users = Select.getBuilder()
                .where(where(Q.User.USERNAME, Where.Op.LIKE, "%e"))
                .query(User.class, getSQLProvider());

        // 2 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 2 rows will be selected
        assertEquals(2, users.length);
    }

    @org.junit.Test
    public void testLikeContainingSelection() {
        User[] users = Select.getBuilder()
                .where(where(Q.User.USERNAME, Where.Op.LIKE, "%lyd%"))
                .query(User.class, getSQLProvider());

        // 1 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 1 rows will be selected
        assertEquals(1, users.length);
    }

    @org.junit.Test
    public void testInStringSelection() {
        User[] users = Select.getBuilder()
                .where(in(Q.User.USERNAME, SetupUser.CLYDE_USER_NAME, SetupUser.ANGIE_USER_NAME))
                .query(User.class, getSQLProvider());

        // 2 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 2 rows will be selected
        assertEquals(2, users.length);
    }

    @org.junit.Test
    public void testInLongSelection() {
        User[] users = Select.getBuilder()
                .where(in(Q.User.TIMESTAMP, SetupUser.CLYDE_TIMESTAMP, SetupUser.ANGIE_TIMESTAMP, SetupUser.GILL_TIMESTAMP))
                .query(User.class, getSQLProvider());

        // 3 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 3 rows will be selected
        assertEquals(3, users.length);
    }

    @org.junit.Test
    public void testOrWhereInQueryIsBuiltFromClause() {
        User[] users = Select.getBuilder()
                .where(or(
                        where(Q.User.USERNAME, Where.Op.EQUAL_TO, SetupUser.CLYDE_USER_NAME),
                        in(Q.User.TIMESTAMP, SetupUser.GILL_TIMESTAMP, SetupUser.ANGIE_TIMESTAMP)
                ))
                .query(User.class, getSQLProvider());


        // 3 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 3 rows will be selected
        assertEquals(3, users.length);
    }

    @org.junit.Test
    public void testAndEqualOperationsSelection() {
        User[] users = Select.getBuilder()
                .where(and(
                        where(Q.User.USERNAME, Where.Op.EQUAL_TO, SetupUser.CLYDE_USER_NAME),
                        where(Q.User.IS_REGISTERED, Where.Op.EQUAL_TO, SetupUser.CLYDE_IS_REGISTERED),
                        where(Q.User.TIMESTAMP, Where.Op.EQUAL_TO, SetupUser.CLYDE_TIMESTAMP)
                ))
                .query(User.class, getSQLProvider());

        // 1 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 1 rows will be selected
        assertEquals(1, users.length);
    }

    @org.junit.Test
    public void testOrEqualOperationsSelection() {
        User[] users = Select.getBuilder()
                .where(or(
                        where(Q.User.USERNAME, Where.Op.EQUAL_TO, SetupUser.CLYDE_USER_NAME),
                        where(Q.User.USERNAME, Where.Op.EQUAL_TO, SetupUser.ANGIE_USER_NAME)
                ))
                .query(User.class, getSQLProvider());

        // 2 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 2 rows will be selected
        assertEquals(2, users.length);
    }

    @org.junit.Test
    public void testAndOrEqualsOperationsSelection() {
        User[] users = Select.getBuilder()
                .where(
                        and(
                                or(
                                        where(Q.User.USERNAME, Where.Op.EQUAL_TO, SetupUser.CLYDE_USER_NAME),
                                        where(Q.User.USERNAME, Where.Op.EQUAL_TO, SetupUser.ANGIE_USER_NAME)
                                ),
                                and(
                                        where(Q.User.TIMESTAMP, Where.Op.MORE_THAN_OR_EQUAL_TO, SetupUser.ANGIE_TIMESTAMP)
                                )
                        )
                )
                .query(User.class, getSQLProvider());

        // 1 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 1 rows will be selected
        assertEquals(1, users.length);
    }

    @org.junit.Test
    public void testNumericOrderByAscSelection() {
        User[] users = Select.getBuilder()
                .orderBy(Q.User.TIMESTAMP, OrderBy.Order.ASC)
                .query(User.class, getSQLProvider());

        // clyde, gill, josh, angie is the timestamp ascending order of the users created
        // by #setupFourTestUsers, therefore, we assert that the rows will be
        // selected in this order
        assertEquals(4, users.length);
        assertEquals(SetupUser.CLYDE_USER_NAME, users[0].getUsername());
        assertEquals(SetupUser.GILL_USER_NAME, users[1].getUsername());
        assertEquals(SetupUser.JOSH_USER_NAME, users[2].getUsername());
        assertEquals(SetupUser.ANGIE_USER_NAME, users[3].getUsername());
    }

    @org.junit.Test
    public void testNumericOrderByDescSelection() {
        User[] users = Select.getBuilder()
                .orderBy(Q.User.TIMESTAMP, OrderBy.Order.DESC)
                .query(User.class, getSQLProvider());

        // angie, josh, gill, clyde is the timestamp descending order of the users created
        // by #setupFourTestUsers, therefore, we assert that the rows will be
        // selected in this order
        assertEquals(4, users.length);
        assertEquals(SetupUser.ANGIE_USER_NAME, users[0].getUsername());
        assertEquals(SetupUser.JOSH_USER_NAME, users[1].getUsername());
        assertEquals(SetupUser.GILL_USER_NAME, users[2].getUsername());
        assertEquals(SetupUser.CLYDE_USER_NAME, users[3].getUsername());
    }

    @org.junit.Test
    public void testAlphaOrderByAscSelection() {
        User[] users = Select.getBuilder()
                .orderBy(Q.User.USERNAME, OrderBy.Order.ASC)
                .query(User.class, getSQLProvider());

        // angie, clyde, gill, josh is the username ascending order of the users created
        // by #setupFourTestUsers, therefore, we assert that the rows will be
        // selected in this order
        assertEquals(4, users.length);
        assertEquals(SetupUser.ANGIE_USER_NAME, users[0].getUsername());
        assertEquals(SetupUser.CLYDE_USER_NAME, users[1].getUsername());
        assertEquals(SetupUser.GILL_USER_NAME, users[2].getUsername());
        assertEquals(SetupUser.JOSH_USER_NAME, users[3].getUsername());
    }

    @org.junit.Test
    public void testAlphaOrderByDescSelection() {
        User[] users = Select.getBuilder()
                .orderBy(Q.User.USERNAME, OrderBy.Order.DESC)
                .query(User.class, getSQLProvider());

        // josh, gill, clyde, angie is the username descending order of the users created
        // by #setupFourTestUsers, therefore, we assert that the rows will be
        // selected in this order
        assertEquals(4, users.length);
        assertEquals(SetupUser.JOSH_USER_NAME, users[0].getUsername());
        assertEquals(SetupUser.GILL_USER_NAME, users[1].getUsername());
        assertEquals(SetupUser.CLYDE_USER_NAME, users[2].getUsername());
        assertEquals(SetupUser.ANGIE_USER_NAME, users[3].getUsername());
    }

    @org.junit.Test
    public void testOrderByRandom() {
        User[] users = Select.getBuilder()
                .orderBy(Q.User.USERNAME, OrderBy.Order.RANDOM)
                .query(User.class, getSQLProvider());

        // just check that the results are returned and no error is thrown
        // TODO: do 100 random queries and ensure that at least one of the ordering is different
        assertEquals(4, users.length);
    }

    @org.junit.Test
    public void testLimitLowerBoundSelection() {
        User[] users = Select.getBuilder()
                .limit(0, 2)
                .orderBy(Q.User.USERNAME, OrderBy.Order.DESC)
                .query(User.class, getSQLProvider());

        assertEquals(2, users.length);
        assertEquals(SetupUser.JOSH_USER_NAME, users[0].getUsername());
        assertEquals(SetupUser.GILL_USER_NAME, users[1].getUsername());
    }

    @org.junit.Test
    public void testLimitUpperBoundSelection() {
        User[] users = Select.getBuilder()
                .limit(2,4)
                .orderBy(Q.User.USERNAME, OrderBy.Order.DESC)
                .query(User.class, getSQLProvider());

        assertEquals(2, users.length);
        assertEquals(SetupUser.CLYDE_USER_NAME, users[0].getUsername());
        assertEquals(SetupUser.ANGIE_USER_NAME, users[1].getUsername());
    }
}