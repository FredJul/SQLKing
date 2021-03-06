package net.frju.androidquery.preprocessor.processor.freemarker;

import net.frju.androidquery.preprocessor.processor.data.Data;
import net.frju.androidquery.preprocessor.processor.data.DbModel;
import net.frju.androidquery.preprocessor.processor.freemarker.method.AssembleCreateTableMethod;
import net.frju.androidquery.preprocessor.processor.freemarker.method.FormatConstantMethod;
import net.frju.androidquery.preprocessor.processor.freemarker.method.GetColumnSetterMethod;
import net.frju.androidquery.preprocessor.processor.freemarker.method.GetColumnsSqlArrayMethod;
import net.frju.androidquery.preprocessor.processor.freemarker.method.GetContentValueMethod;
import net.frju.androidquery.preprocessor.processor.freemarker.method.GetCursorGetterMethod;
import net.frju.androidquery.preprocessor.processor.freemarker.method.GetPrimaryKeySetterMethod;
import net.frju.androidquery.preprocessor.processor.freemarker.method.GetPrimaryKeyValueMethod;
import net.frju.androidquery.preprocessor.processor.freemarker.method.IsPrimaryKeyAutoIncrementMethod;
import net.frju.androidquery.preprocessor.processor.freemarker.method.JoinReferencesMethod;
import net.frju.androidquery.preprocessor.processor.freemarker.method.JoinSettersMethod;

import java.util.HashMap;
import java.util.Map;

public class DataModel {

    private static final String PACKAGE_NAME = "package_name";
    private static final String DATABASE_PROVIDERS = "providers";
    private static final String TABLES = "tables";
    private static final String TABLE = "table";

    public static Map<String, Object> createQMap(String packageName, Data data) {
        Map<String, Object> map = new HashMap<>();

        map.put(PACKAGE_NAME, packageName);
        map.put(DATABASE_PROVIDERS, data.getDatabaseProviders());
        map.put(TABLES, data.getTables());
        map.putAll(GetCursorGetterMethod.getMethodMap(data));
        map.putAll(GetContentValueMethod.getMethodMap(data));
        map.putAll(GetPrimaryKeyValueMethod.getMethodMap(data));
        map.putAll(GetPrimaryKeySetterMethod.getMethodMap());
        map.putAll(GetColumnSetterMethod.getMethodMap());
        map.putAll(IsPrimaryKeyAutoIncrementMethod.getMethodMap());
        map.putAll(AssembleCreateTableMethod.getMethodMap(data));
        map.putAll(GetColumnsSqlArrayMethod.getMethodMap(data));
        map.putAll(FormatConstantMethod.getMethodMap());
        map.putAll(JoinSettersMethod.getMethodMap(data));
        map.putAll(JoinReferencesMethod.getMethodMap());

        return map;
    }

    public static Map<String, Object> createModelDescriptorMap(String packageName, DbModel table, Data data) {
        Map<String, Object> map = createQMap(packageName, data);
        map.put(TABLE, table);
        return map;
    }
}
