package com.memtrip.sqlking.preprocessor.processor.templates;

import com.memtrip.sqlking.preprocessor.processor.model.Data;
import com.memtrip.sqlking.preprocessor.processor.templates.method.AssembleCreateTableMethod;
import com.memtrip.sqlking.preprocessor.processor.templates.method.FormatConstantMethod;
import com.memtrip.sqlking.preprocessor.processor.templates.method.GetCursorGetterMethod;
import com.memtrip.sqlking.preprocessor.processor.templates.method.GetInsertValueMethod;

import java.util.HashMap;
import java.util.Map;

public class DataModel {

    private static final String TABLES = "tables";

    public static Map<String, Object> create(Data data) {
        Map<String, Object> map = new HashMap<>();

        map.put(TABLES, data.getTables());
        map.putAll(GetCursorGetterMethod.getMethodMap());
        map.putAll(GetInsertValueMethod.getMethodMap());
        map.putAll(AssembleCreateTableMethod.getMethodMap());
        map.putAll(FormatConstantMethod.getMethodMap());

        return map;
    }
}
