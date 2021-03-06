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
package net.frju.androidquery.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Samuel Kirton [sam@memtrip.com]
 */
public interface Resolver {
    @NonNull
    Class<?> getModelClassFromName(@NonNull String modelDbName);

    @NonNull
    DbModelDescriptor getDbModelDescriptor(@NonNull Class<?> classDef);

    @NonNull
    Class<?>[] getModelsForProvider(@Nullable Class<? extends DatabaseProvider> providerClass);

    @Nullable
    DatabaseProvider getDatabaseProviderForModel(@Nullable Class<?> model);

    void initModelWithInitMethods(@NonNull Object model);
}