package com.study.mybatisplugins;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.IntrospectedTable.TargetRunTime;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.plugins.RowBoundsPlugin;

public class RowBoundsPluginEx extends RowBoundsPlugin {
	private FullyQualifiedJavaType rowBounds;
	private List<Methods> = new ArrayList<>();
	private String modelName;
	private String modelExampleName;
	
	public RowBoundsPluginEx() {
		rowBounds = new FullyQualifiedJavaType("org.apache.ibatis.session.RowBounds");
	}
	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		if (hasGenericInterface(interfaze)) {
			interfaze = ExtractClientMethodToInterfacePlugin.genericInterface;
			ExtractClientMethodToInterfacePlugin.updateMethodsSignature(methods,
				modelName, modelExampleName);
		}
		for (Method method : methods) {
			if (!interfaceHasMethod(interfaze, method)) {
				interfaze.addMethod(method);
				interfaze.addImportedType(rowBounds);
			}
		}
		
		return true;
	}
	private boolean interfaceHasMethod(Interface interfaze, Method method2) {
		for (Method interfaceMethod : interfaze.getMethods()) {
			if (isMethodEqual(method, interfaceMethod)) {
				return true;
			}
		}	
		return false;
	}
	private boolean isMethodEqual(Method method1, Method method2) {
		if (method1 == method2) {
			return true;
		}
		if (method1.getName().equals(method2.getName()) {
			if (method1.getParameters().size() == method2.getParameters().size()) {
				boolean allParamTypeEqual = true;
				for (int i = 0; i < method1.getParameters().size(); i++) {
					if (!method1.getParameters().get(i).getType().equals(
							method2.getParameters().get(i).getType())) {
								allParamTypeEqual = false;
								break;
					}
				}
				if (allParamTypeEqual) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method,
			Interface interfaze, IntrospectedTable introspectedTable) {
		if (introspectedTable.getTargetRuntime() == TargetRuntime.MYBATIS3) {
			copyAndAddMethod(method, interfaze);
		}
		return true;
	}
	
	@Override
	public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(
			Method method, Interface interfaze, 
			IntrospectedTable introspectedTable) {
		if (introspectedTable.getTargetRuntime() == TargetRuntime.MYBATIS3) {
			copyAndAddMethod(method, interfaze);
		}
		return true;
	}
	
	private boolean hasGenericInterface(Interface interfaze) {
		boolean found = false;
		for (FullyQualifiedJavaType superInterface : interface.getSuperInterfaceTypes()) {
			if (superInterface.getShortNameWithoutTypeArguments().equals(
				ExtractClientMethodToInterfacePlugin.genericInterface.getType().getShortNameWithoutTypeArguments())) {
				found = true;
				break;
			}
		}
		return found;
	}
	
	private void copyAndAddMethod(Method method, Interface interfaze) {
		Method newMethod = new Method(method);
		newMethod.setName(method.getName() + "WithRowbounds");
		newMethod.addParameter(new Parameter(rowBounds, "rowBounds"));
		methods.add(newMethod);
	}
	
	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		modelExampleName = topLevelClass.getType().getShortName();
		return true;
	}
	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		modelName = topLevelClass.getType().getShortName();
		return true;
	}
}
