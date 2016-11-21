package com.memtrip.sqlking.operation.function;

import android.database.Cursor;

import com.memtrip.sqlking.database.DatabaseProvider;
import com.memtrip.sqlking.database.Query;

import java.util.concurrent.Callable;

import io.reactivex.Observable;

public class Raw extends Query {

    private Raw() {
    }

    public static Builder getBuilder(DatabaseProvider databaseProvider) {
        return new Builder(databaseProvider);
    }

    public static class Builder {
        private String mQuery;
        private DatabaseProvider mDatabaseProvider;

        private Builder(DatabaseProvider databaseProvider) {
            mDatabaseProvider = databaseProvider;
        }

        public Builder query(String query) {
            mQuery = query;
            return this;
        }

        public Cursor execute() {
            return rawQuery(mQuery, mDatabaseProvider);
        }

        /**
         * Executes a Row query
         *
         * @return An RxJava Observable
         */
        public Observable<Cursor> rx() {
            return wrapRx(new Callable<Cursor>() {
                @Override
                public Cursor call() throws Exception {
                    return execute();
                }
            });
        }
    }
}
