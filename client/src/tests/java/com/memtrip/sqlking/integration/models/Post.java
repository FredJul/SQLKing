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
package com.memtrip.sqlking.integration.models;

import com.memtrip.sqlking.common.Column;
import com.memtrip.sqlking.common.Table;

/**
 * @author Samuel Kirton [sam@memtrip.com]
 */
@Table
public class Post {
    @Column private String id;
    @Column private String title;
    @Column private byte[] blob;
    @Column private long timestamp;

    public String getId() {
        return id;
    }

    public void setId(String newVal) {
        id = newVal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String newVal) {
        title = newVal;
    }

    public byte[] getBlob() {
        return blob;
    }

    public void setBlob(byte[] newVal) {
        blob = newVal;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long newVal) {
        timestamp = newVal;
    }
}