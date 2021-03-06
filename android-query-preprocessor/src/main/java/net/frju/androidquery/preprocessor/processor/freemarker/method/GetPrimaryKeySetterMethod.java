package net.frju.androidquery.preprocessor.processor.freemarker.method;

import net.frju.androidquery.preprocessor.processor.data.DbField;
import net.frju.androidquery.preprocessor.processor.data.DbModel;
import net.frju.androidquery.preprocessor.processor.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.ext.beans.StringModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class GetPrimaryKeySetterMethod implements TemplateMethodModelEx {

    private static final String GET_PRIMARY_KEY_SETTER = "getPrimaryKeySetter";

    public static Map<String, Object> getMethodMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(GET_PRIMARY_KEY_SETTER, new GetPrimaryKeySetterMethod());
        return map;
    }

    private GetPrimaryKeySetterMethod() {
    }

    private String assemblePrimaryKeySetter(String varName, String valueVarName, DbModel dbModel) {
        for (DbField dbField : dbModel.getFields()) {
            if (dbField.hasPrimaryKey()) {
                return StringUtils.getSetter(varName, valueVarName, dbField);
            }
        }

        return "";
    }

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        Object var = arguments.get(0);
        String varName = var instanceof SimpleScalar ?
                var.toString() :
                String.valueOf(var);

        var = arguments.get(1);
        String valueVarName = var instanceof SimpleScalar ?
                var.toString() :
                String.valueOf(var);

        Object tableValue = arguments.get(2);

        DbModel dbModel;
        if (tableValue instanceof StringModel) {
            StringModel stringModel = (StringModel) tableValue;
            dbModel = (DbModel) stringModel.getAdaptedObject(DbModel.class);
        } else {
            throw new IllegalStateException("The assemblePrimaryKeySetter argument must be type of " +
                    "net.frju.androidquery.preprocessor.processor.data.DbModel");
        }

        return assemblePrimaryKeySetter(varName, valueVarName, dbModel);
    }
}
