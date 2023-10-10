package com.protocol.processor

import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import com.protocol.annotation.IProtocolImplProvider
import com.protocol.annotation.IProtocolProvider
import com.protocol.annotation.Protocol
import com.protocol.annotation.ProtocolImpl
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo

/**
 * ksp注解处理器
 *
 * @author HB.zhuanghngzhan
 * @date 2023-04-18
 */
class ProtocolSymbolProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val dependencies = Dependencies(false, *resolver.getAllFiles().toList().toTypedArray())

        resolver.getSymbolsWithAnnotation(Protocol::class.qualifiedName.orEmpty())
            .filter { it is KSClassDeclaration && it.validate() }
            .forEach {
                createProtocol(it as KSClassDeclaration, dependencies)
            }

        resolver.getSymbolsWithAnnotation(ProtocolImpl::class.qualifiedName.orEmpty())
            .filter { it is KSClassDeclaration && it.validate() }
            .forEach {
                createProtocolImpl(it as KSClassDeclaration, dependencies)
            }

        return emptyList()
    }

    /**
     * 生成接口的Protocol协议的映射类
     * 创建类，类名为：接口名 + $$Protocol$$Get
     * 实现接口IProtocolProvider，实现方法getProtocol并返回Protocol注解的值
     * @author HB.zhuanghngzhan
     * @date 2023-04-19
     * @param element
     * @param dependencies
     */
    private fun createProtocol(element: KSClassDeclaration, dependencies: Dependencies) {
        val value = element.annotations.firstOrNull { it.shortName.asString() == "Protocol" }?.arguments?.get(0)?.value?.toString()
        val clazz = element.closestClassDeclaration()
        if (!value.isNullOrBlank() && clazz != null) {
            val className = clazz.toClassName()
            val name = "${clazz.simpleName.asString()}$\$Protocol$\$Get"

            val builder = TypeSpec.classBuilder(className.simpleName + "$\$Protocol$\$Get")
                .addModifiers(KModifier.PUBLIC, KModifier.FINAL)
                .addSuperinterface(IProtocolProvider::class)
            val methodBuild = FunSpec.builder("getProtocol")
                .returns(String::class)
                .addModifiers(KModifier.PUBLIC, KModifier.FINAL, KModifier.OVERRIDE)
                .addStatement("return %S", value)
            builder.addFunction(methodBuild.build())
            val file = FileSpec.builder(Constants.PROTOCOL_PROVIDER_PACKAGE, name)
                .addType(builder.build())
                .build()
            file.writeTo(environment.codeGenerator, dependencies)
        }
    }

    /**
     * 生成实现类的Protocol协议的映射类
     * 创建类，类名为：Protocol注解值 + $$ProtocolImpl$$Get
     * 实现接口IProtocolImplProvider，实现方法getProtocolImpl并返回Protocol实现类的全路径
     * @author HB.zhuanghngzhan
     * @date 2023-04-19
     * @param element
     * @param dependencies
     */
    private fun createProtocolImpl(element: KSClassDeclaration, dependencies: Dependencies) {
        val value = element.annotations.firstOrNull { it.shortName.asString() == "ProtocolImpl" }?.arguments?.get(0)?.value?.toString()
        val clazz = element.closestClassDeclaration()
        if (!value.isNullOrBlank() && clazz != null) {
            val name = "$value$\$ProtocolImpl$\$Get"
            val builder = TypeSpec.classBuilder(name)
                .addModifiers(KModifier.PUBLIC, KModifier.FINAL)
                .addSuperinterface(IProtocolImplProvider::class)
            val methodBuild = FunSpec.builder("getProtocolImpl")
                .returns(String::class)
                .addModifiers(KModifier.PUBLIC, KModifier.FINAL, KModifier.OVERRIDE)
                .addStatement("return %S", clazz.toClassName().canonicalName)
            builder.addFunction(methodBuild.build())
            val file = FileSpec.builder(Constants.PROTOCOL_PROVIDER_PACKAGE, name)
                .addType(builder.build())
                .build()
            file.writeTo(environment.codeGenerator, dependencies)
        }
    }

}