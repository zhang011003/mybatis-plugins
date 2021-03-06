package com.study.mybatisplugins;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;

/**
 * 该插件作用：将client接口中的方法提取到公共接口中
 */
public class ExtractClientMethodToInterfacePlugin extends PluginAdapter {
	private static final FullyQualifiedJavaType MODEL = new FullyQualifiedJavaType("Model");
	private static final FullyQualifiedJavaType MODEL_CRITERIA = new FullyQualifiedJavaType("ModelCriteria");
	private List<Method> interfaceMethod = new ArrayList<>();
	private JavaClientGeneratorConfiguration clientConfig;
	private FullyQualifiedJavaType genericInterfaceType;
	private String modelName = "";
	private String modelExampleName = "";
	public static Interface genericInterface;
	@Override
	public void setContext(Context context) {
		super.setContext(context);
		clientConfig = getContext().getJavaClientGeneratorConfiguration();
		genericInterfaceType = new FullyQualifiedJavaType(clientConfig.getTargetPackage() + ".GenericMapper");
		genericInterfaceType.addTypeArgument(MODEL);
		genericInterfaceType.addTypeArgument(MODEL_CRITERIA);
		genericInterface = new Interface(genericInterfaceType);
	}
	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}
	
	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		interfaceMethod.clear();
	}
	
	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
		
		String targetProject = clientConfig.getTargetProject();
		
		updateMethodsSignature(interfaceMethod, modelName, modelExampleName);
	
		for (Method method : interfaceMethod) {
			genericInterface.addMethod(method);
		}
		genericInterface.addImportedType(new FullyQualifiedJavaType(
			"org.apache.ibatis.annotations.Param"));
		genericInterface.addImportedType(new FullyQualifiedJavaType("java.util.List"));
		JavaFormatter javaFormatter = new DefaultJavaFormatter();
		GeneratedJavaFile javaFile = new GeneratedJavaFile(genericInterface, targetProject, javaFormatter);
		List<GeneratedJavaFile> javaFiles = new ArrayList<>();
		javaFiles.add(javaFile);
		return javaFiles;
	}
	public static void updateMethodsSignature(List<Method> methods, String modelName, 
		String modelExampleName) {
		for (Method method : interfaceMethod) {
			for (int i = 0; i < method.getParameters().size(); i++) {
				Parameter param = method.getParameters().get(i);
				if (modelName.equals(param.getType().getShortName())) {
					Parameter tmpParam = new Parameter(MODEL, "model");
					for (String annotation : param.getAnnotations()) {
						tmpParam.addAnnotation(annotation);
					}
					method.getParameters().set(i, param);
				}
				if (modelExampleName.equals(param.getType().getShortName())) {
					Parameter tmpParam = new Parameter(MODEL_CRITERIA, "modelCriteria");
					for (String annotation : param.getAnnotations()) {
						tmpParam.addAnnotation(annotation);
					}
					method.getParameters().set(i, param);
				}
			}
			FullyQualifiedJavaType returnType = method.getReturnType();
			if (returnType.getTypeArguments().size() <= 0) {
				if (modelName.equals(returnType.getShortName())) {
					method.setReturnType(MODEL);
				}
				else if (modelExampleName.equals(returnType.getShortName())) {
					method.setReturnType(MODEL_CRITERIA);
				}
			} else {
				for (int i = 0; i < returnType.getTypeArguments().size(); i++) {
					FullyQualifiedJavaType typeArg = returnType.getTypeArguments().get(i);
					if (modelName.equals(typeArg.getShortName())) {
						returnType.getTypeArguments().set(i, MODEL);
					}
					else if (modelExampleName.equals(typeArg.getShortName())) {
						returnType.getTypeArguments().set(i, MODEL_CRITERIA);
					}
				}
			}
		}
	}

	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		FullyQualifiedJavaType superInterface = new FullyQualifiedJavaType(genericInterfaceType.getShortName());
		FullyQualifiedJavaType importedType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
		interfaze.addImportedType(importedType);
		superInterface.getTypeArguments().set(0, importedType);
		
		importedType = new FullyQualifiedJavaType(introspectedTable.getExampleType());
		interfaze.addImportedType(importedType);
		superInterface.getTypeArguments().set(1, importedType);
		
		interfaze.addSuperInterface(superInterface);
		
		interfaze.getMethods().removeAll(interfaceMethod);
		
		return true;
	}
	@Override
	public boolean clientCountByExampleMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		interfaceMethod.add(method);
		return false;
	}
	@Override
	public boolean clientDeleteByExampleMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		interfaceMethod.add(method);
		return false;
	}
	@Override
	public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		interfaceMethod.add(method);
		return false;
	}
	@Override
	public boolean clientInsertMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		interfaceMethod.add(method);
		return false;
	}
	
	@Override
	public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		interfaceMethod.add(method);
		return false;
	}
	@Override
	public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		interfaceMethod.add(method);
		return false;
	}
	@Override
	public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		interfaceMethod.add(method);
		return false;
	}
	@Override
	public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		interfaceMethod.add(method);
		return false;
	}
	@Override
	public boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		interfaceMethod.add(method);
		return false;
	}
	@Override
	public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		interfaceMethod.add(method);
		return false;
	}
	@Override
	public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		interfaceMethod.add(method);
		return false;
	}
	@Override
	public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		interfaceMethod.add(method);
		return false;
	}
	@Override
	public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		interfaceMethod.add(method);
		return false;
	}
	@Override
	public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		interfaceMethod.add(method);
		return false;
	}
	@Override
	public boolean clientSelectAllMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		interfaceMethod.add(method);
		return false;
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
