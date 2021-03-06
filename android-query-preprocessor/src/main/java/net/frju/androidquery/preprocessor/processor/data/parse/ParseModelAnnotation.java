package net.frju.androidquery.preprocessor.processor.data.parse;

import net.frju.androidquery.preprocessor.processor.Context;
import net.frju.androidquery.preprocessor.processor.data.DbField;
import net.frju.androidquery.preprocessor.processor.data.DbModel;
import net.frju.androidquery.preprocessor.processor.data.ForeignKey;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

import static net.frju.androidquery.preprocessor.processor.data.parse.ParseFieldAnnotation.parseField;

class ParseModelAnnotation {

    static DbModel parseModel(Element element) {

        String name = assembleName(element);
        String tablePackage = assemblePackage(element);
        TypeMirror mirror = assembleDatabaseProvider(element);

        DbModel dbModel = new DbModel();
        dbModel.setElement(element);
        dbModel.setName(name);
        dbModel.setDbName(assembleRealName(element));
        dbModel.setPackage(tablePackage);
        dbModel.setType(tablePackage + "." + name);
        dbModel.setFields(assembleColumns(element));
        dbModel.setForeignKeys(assembleForeignKeys(element));
        dbModel.setDatabaseProvider(mirror);
        dbModel.setHasLocalDatabaseProvider(assembleHasLocalDatabaseProvider(mirror));
        dbModel.setInitMethodNames(assembleInitMethods(element));

        return dbModel;
    }

    private static String assembleName(Element element) {
        Name name = element.getSimpleName();
        return name.toString();
    }

    static TypeMirror assembleDatabaseProvider(Element element) {
        net.frju.androidquery.annotation.DbModel dbModelAnnotation = element.getAnnotation(net.frju.androidquery.annotation.DbModel.class);
        TypeMirror type = null;
        try {
            dbModelAnnotation.databaseProvider();
        } catch (MirroredTypeException mte) {
            type = mte.getTypeMirror();
        }
        return type;
    }

    static boolean assembleHasLocalDatabaseProvider(TypeMirror mirror) {
        TypeElement providerElement = Context.getInstance().getElementUtils().getTypeElement(mirror.toString());

        if (providerElement != null && providerElement.getSuperclass() != null &&
                providerElement.getSuperclass().toString().equals("net.frju.androidquery.database.BaseLocalDatabaseProvider")) {
            return true;
        }

        return false;
    }

    private static String assembleRealName(Element element) {
        net.frju.androidquery.annotation.DbModel dbModelAnnotation = element.getAnnotation(net.frju.androidquery.annotation.DbModel.class);
        return dbModelAnnotation.dbName();
    }

    private static String assemblePackage(Element element) {
        PackageElement packageElement = Context.getInstance().getElementUtils().getPackageOf(element);
        Name name = packageElement.getQualifiedName();
        return name.toString();
    }

    private static List<DbField> assembleColumns(Element element) {
        List<DbField> dbFields = new ArrayList<>();

        for (Element childElement : Context.getInstance().getElementUtils().getAllMembers((TypeElement) element)) {
            if (childElement.getKind().isField() && childElement.getAnnotation(net.frju.androidquery.annotation.DbField.class) != null) {
                dbFields.add(parseField(childElement));
            }
        }

        return dbFields;
    }

    private static List<String> assembleInitMethods(Element element) {
        List<String> initMethods = new ArrayList<>();

        for (Element childElement : Context.getInstance().getElementUtils().getAllMembers((TypeElement) element)) {
            if (childElement instanceof ExecutableElement && childElement.getAnnotation(net.frju.androidquery.annotation.InitMethod.class) != null) {
                initMethods.add(childElement.getSimpleName().toString());
            }
        }

        return initMethods;
    }

    private static List<ForeignKey> assembleForeignKeys(Element element) {
        net.frju.androidquery.annotation.DbModel dbModelAnnotation = element.getAnnotation(net.frju.androidquery.annotation.DbModel.class);
        net.frju.androidquery.annotation.ForeignKey[] foreignKeysAnnotation = dbModelAnnotation.foreignKeys();

        List<ForeignKey> foreignKeys = new ArrayList<>();

        for (net.frju.androidquery.annotation.ForeignKey annotation : foreignKeysAnnotation) {
            ForeignKey foreignKey = new ForeignKey();
            foreignKey.setTable(annotation.targetTable());
            foreignKey.setTargetColumn(annotation.targetColumn());
            foreignKey.setLocalColumn(annotation.localColumn());

            foreignKeys.add(foreignKey);
        }

        return foreignKeys;
    }
}