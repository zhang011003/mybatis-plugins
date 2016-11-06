package com.study.mybatisplugins;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

/**
 * 该插件作用：将默认带example字符串的方法替换为指定的字符串
 */
public class RenameExampleMethodPlugin extends PluginAdapter {
	private String searchString;
    private String replaceString;
    private Pattern pattern;
    
    public boolean validate(List<String> warnings) {
        searchString = properties.getProperty("searchString"); 
        replaceString = properties.getProperty("replaceString");

        boolean valid = stringHasValue(searchString)
                && stringHasValue(replaceString);

        if (valid) {
            pattern = Pattern.compile(searchString);
        } else {
            if (!stringHasValue(searchString)) {
                warnings.add("searchString has no value!");
            }
            if (!stringHasValue(replaceString)) {
                warnings.add("replaceString has no value!");
            }
        }

        return valid;
    }

	private boolean stringHasValue(String s) {
		return s != null && s.length() > 0;
	}
	
	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		String oldType = introspectedTable.getCountByExampleStatementId();
		Matcher matcher = pattern.matcher(oldType);
        oldType = matcher.replaceAll(replaceString);
        introspectedTable.setCountByExampleStatementId(oldType);
        
        oldType = introspectedTable.getSelectByExampleStatementId();
        matcher = pattern.matcher(oldType);
        oldType = matcher.replaceAll(replaceString);
        introspectedTable.setSelectByExampleStatementId(oldType);
        
        oldType = introspectedTable.getSelectByExampleWithBLOBsStatementId();
        matcher = pattern.matcher(oldType);
        oldType = matcher.replaceAll(replaceString);
        introspectedTable.setSelectByExampleWithBLOBsStatementId(oldType);
        
        oldType = introspectedTable.getDeleteByExampleStatementId();
        matcher = pattern.matcher(oldType);
        oldType = matcher.replaceAll(replaceString);
        introspectedTable.setDeleteByExampleStatementId(oldType);
        
        oldType = introspectedTable.getUpdateByExampleSelectiveStatementId();
        matcher = pattern.matcher(oldType);
        oldType = matcher.replaceAll(replaceString);
        introspectedTable.setUpdateByExampleSelectiveStatementId(oldType);
        
        oldType = introspectedTable.getUpdateByExampleStatementId();
        matcher = pattern.matcher(oldType);
        oldType = matcher.replaceAll(replaceString);
        introspectedTable.setUpdateByExampleStatementId(oldType);
        
        oldType = introspectedTable.getUpdateByExampleWithBLOBsStatementId();
        matcher = pattern.matcher(oldType);
        oldType = matcher.replaceAll(replaceString);
        introspectedTable.setUpdateByExampleWithBLOBsStatementId(oldType);
        
	}
}
