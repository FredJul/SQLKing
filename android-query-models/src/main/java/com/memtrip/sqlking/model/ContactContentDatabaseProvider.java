package com.memtrip.sqlking.model;

import android.content.ContentResolver;
import android.provider.ContactsContract;

import com.memtrip.sqlking.database.BaseContentDatabaseProvider;
import com.memtrip.sqlking.database.Resolver;

import net.frju.androidquery.models.gen.Q;


public class ContactContentDatabaseProvider extends BaseContentDatabaseProvider {

    public ContactContentDatabaseProvider(ContentResolver contentResolver) {
        super(contentResolver);
    }

    @Override
    protected String getAuthority() {
        return ContactsContract.AUTHORITY;
    }

    @Override
    protected Resolver getResolver() {
        return Q.getResolver();
    }
}
