package com.protocol.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import com.protocol.annotation.IProtocolImplProvider
import com.protocol.annotation.IProtocolProvider
import com.protocol.annotation.Protocol
import com.protocol.annotation.ProtocolImpl
import com.squareup.kotlinpoet.*
import java.io.IOException

class BuilderProcessor(
    val codeGenerator: CodeGenerator,
    val logger: KSPLogger
) : SymbolProcessor {


    override fun process(resolver: Resolver): List<KSAnnotated> {

        //获取所有带Protocol注解的类
        val protocolImplSet = resolver.getSymbolsWithAnnotation(ProtocolImpl::class.java.name)
        val protocolSet = resolver.getSymbolsWithAnnotation(Protocol::class.java.name)

        protocolImplSet
            .filter { (it is KSClassDeclaration) && it.validate() }
            .forEach {
                it as KSClassDeclaration
                //遍历该类所有的注解
                it.annotations.forEach { data ->
                    //找到ProtocolImpl注解的参数value
                    val resValue =
                        data.arguments.find { argument -> argument.name?.asString() == "value" }?.value?.toString()
                    if (resValue != null) {
                        logger.error("protocolImplSet找到了====$resValue")
                        createProtocolImpl(resValue, it.simpleName.asString())
                    }

                }
            }
        protocolSet
            .filter { it is KSClassDeclaration && it.validate() }
            .forEach {
                it as KSClassDeclaration
                //遍历该类所有的注解
                it.annotations.forEach { data ->
                    //找到ProtocolImpl注解的参数value
                    val resValue =
                        data.arguments.find { argument -> argument.name?.asString() == "value" }?.value?.toString()
                    if (resValue != null) {
                        logger.error("protocolSet找到了====$resValue")
                        createProtocol(resValue, it.simpleName.asString())
                    }

                }
            }

        return emptyList()
    }

    /**
     * 生成接口的Protocol协议的映射类
     * 创建类，类名为：接口名 + $$Protocol$$Get
     * 实现接口IProtocolProvider，实现方法getProtocol并返回Protocol注解的值
     */
    @OptIn(DelicateKotlinPoetApi::class)
    private fun createProtocol(route: String, className: String) {
        //生成类名
        val builder = TypeSpec.classBuilder("$className$\$Protocol$\$Get")
            .addModifiers(KModifier.PUBLIC)
            .addFunction(generateProtocolValue(route, "getProtocol"))
            .addSuperinterface(IProtocolProvider::class.java).build()
        try {
            val kotlinFile =
                FileSpec.builder(Constants.PROTOCOL_PROVIDER_PACKAGE, "$className$\$Protocol$\$Get")
                    .addType(builder)
                    .build()
            kotlinFile.writeTo(System.out)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //    https://blog.csdn.net/lyabc123456/article/details/128697123
    private fun generateProtocolValue(name: String, method: String): FunSpec {
        return FunSpec.builder(method)
            .addModifiers(KModifier.PUBLIC, KModifier.FINAL)
            .addAnnotation(
                Override::class
            )
            .returns(String::class)
            .addStatement("return %S", name)
            .build()
    }

    /**
     * 生成实现类的Protocol协议的映射类
     * 创建类，类名为：Protocol注解值 + $$ProtocolImpl$$Get
     * 实现接口IProtocolImplProvider，实现方法getProtocolImpl并返回Protocol实现类的全路径
     */
    private fun createProtocolImpl(
        route: String,
        className: String
    ) {
        //生成类名
        val builder = TypeSpec.classBuilder("$className$\$ProtocolImpl$\$Get")
            .addModifiers(KModifier.PUBLIC)
            .addFunction(generateProtocolValue(route, "getProtocolImpl"))
            .addSuperinterface(IProtocolImplProvider::class.java).build()
        try {
            val kotlinFile =
                FileSpec.builder(
                    Constants.PROTOCOL_PROVIDER_PACKAGE,
                    "$className$\$ProtocolImpl$\$Get"
                )
                    .addType(builder)
                    .build()
            kotlinFile.writeTo(System.out)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

}
