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
package net.frju.androidquery.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.provider.BaseColumns;

import net.frju.androidquery.operation.clause.Clause;
import net.frju.androidquery.operation.join.Join;
import net.frju.androidquery.operation.keyword.Limit;
import net.frju.androidquery.operation.keyword.OrderBy;

/**
 * @author Samuel Kirton [sam@memtrip.com]
 */
public abstract class BaseContentDatabaseProvider extends DatabaseProvider {

    protected ContentResolver mContentResolver;

    public BaseContentDatabaseProvider(ContentResolver contentResolver) {
        super();
        mContentResolver = contentResolver;
    }

    protected abstract String getAuthority();

    public Uri getUri(Class model) {
        String tableRealName = getResolver().getTableDescription(model).getTableRealName();
        return new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(getAuthority()).appendPath(firstToLowerCase(tableRealName)).build();
    }

    protected Uri getUri(String tableName) {
        return new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(getAuthority()).appendPath(firstToLowerCase(tableName)).build();
    }

    protected int bulkInsert(String tableName, ContentValues[] valuesArray) {
        return mContentResolver.bulkInsert(getUri(tableName), valuesArray);
    }

    protected int update(String tableName, ContentValues values, Clause[] clause) {
        return mContentResolver.update(getUri(tableName),
                values,
                mClauseHelper.getClause(clause),
                mClauseHelper.getClauseArgs(clause)
        );
    }

    protected Cursor query(String tableName, String[] columns, Clause[] clause, Join[] joins,
                           String groupBy, String having, OrderBy orderBy, Limit limit) {

        if (joins != null && joins.length > 0) {
            throw new SQLException("Join clause not supported by ContentProvider");
        } else if (groupBy != null) {
            throw new SQLException("GroupBy clause not supported by ContentProvider");
        } else if (having != null) {
            throw new SQLException("Having clause not supported by ContentProvider");
        } else if (limit != null) {
            throw new SQLException("Limit clause not supported by ContentProvider");
        } else {
            return mContentResolver.query(
                    getUri(tableName),
                    columns,
                    mClauseHelper.getClause(clause),
                    mClauseHelper.getClauseArgs(clause),
                    mClauseHelper.getOrderBy(orderBy)
            );
        }
    }

    protected int delete(String tableName, Clause[] clause) {
        return mContentResolver.delete(
                getUri(tableName),
                mClauseHelper.getClause(clause),
                mClauseHelper.getClauseArgs(clause)
        );
    }

    protected long count(String tableName, Clause[] clause) {
        Cursor c = null;

        // First try with the SQL method
        try {
            c = mContentResolver.query(
                    getUri(tableName),
                    new String[]{"COUNT(*)"},
                    mClauseHelper.getClause(clause),
                    mClauseHelper.getClauseArgs(clause),
                    null
            );

            c.moveToFirst();
            return c.getLong(0);
        } catch (Exception e) {
            // Maybe the content provider is not backed by an SQL database after all, let's try with the _count field
            try {
                c = mContentResolver.query(
                        getUri(tableName),
                        new String[]{BaseColumns._COUNT},
                        mClauseHelper.getClause(clause),
                        mClauseHelper.getClauseArgs(clause),
                        null
                );

                c.moveToFirst();
                return c.getLong(0);
            } catch (Exception e2) {
                throw new SQLException(e2.getMessage());
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    protected Cursor rawQuery(String sql) {
        throw new SQLException("Raw queries not supported by ContentProvider");
    }

    static String firstToUpperCase(String value) {
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }

    static String firstToLowerCase(String value) {
        return Character.toLowerCase(value.charAt(0)) + value.substring(1);
    }
}