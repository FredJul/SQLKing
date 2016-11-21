package com.memtrip.sqlking.sample.model;

import com.memtrip.sqlking.common.Column;
import com.memtrip.sqlking.common.Table;
import com.memtrip.sqlking.sample.provider.ContentDatabaseProvider;
import com.memtrip.sqlking.sample.provider.LocalDatabaseProvider;

@Table(localDatabaseProvider = LocalDatabaseProvider.class, contentDatabaseProvider = ContentDatabaseProvider.class)
public class User {
    @Column(index = true, realName = "_id")
    public int id;
    @Column
    public String username;
}
