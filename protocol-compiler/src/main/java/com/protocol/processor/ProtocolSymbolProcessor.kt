package com.protocol.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo

class ProtocolSymbolProcessor(
    private val environment: SymbolProcessorEnvironment
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {

        // -------- @Protocol（接口） --------
        resolver.getSymbolsWithAnnotation(Constants.ANNOTATION_PROTOCOL)
            .filterIsInstance<KSClassDeclaration>()
            .filter { it.validate() }
            .forEach { generateProtocolGet(it) }

        // -------- @ProtocolImpl（实现） --------
        resolver.getSymbolsWithAnnotation(Constants.ANNOTATION_PROTOCOL_IMPL)
            .filterIsInstance<KSClassDeclaration>()
            .filter { it.validate() }
            .forEach { generateProtocolImplGet(it) }

        return emptyList()
    }

    /**
     * 生成：
     * InterfaceName$$Protocol$$Get
     */
    private fun generateProtocolGet(clazz: KSClassDeclaration) {
        val protocolValue = clazz.annotations
            .first { it.annotationType.resolve().declaration.qualifiedName?.asString() == Constants.ANNOTATION_PROTOCOL }
            .arguments[0].value?.toString()
            ?: return

        val className = clazz.simpleName.asString() + $$$"$$Protocol$$Get"

        val typeSpec = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.PUBLIC, KModifier.FINAL)
            .addSuperinterface(ClassName("com.protocol.annotation", "IProtocolProvider"))
            .addFunction(
                FunSpec.builder("getProtocol")
                    .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
                    .returns(String::class)
                    .addStatement("return %S", protocolValue)
                    .build()
            )
            .build()
        println(">>> Generating file: ${clazz.simpleName.asString()} in package ${Constants.GENERATED_PACKAGE}")

        FileSpec.builder(Constants.GENERATED_PACKAGE, className)
            .addType(typeSpec)
            .build()
            .writeTo(
                environment.codeGenerator,
                Dependencies(
                    aggregating = false,              // ✅ isolating
                    clazz.containingFile!!             // ✅ 只依赖当前文件
                )
            )
    }

    /**
     * 生成：
     * ProtocolValue$$ProtocolImpl$$Get
     */
    private fun generateProtocolImplGet(clazz: KSClassDeclaration) {
        val protocolValue = clazz.annotations
            .first { it.annotationType.resolve().declaration.qualifiedName?.asString() == Constants.ANNOTATION_PROTOCOL_IMPL }
            .arguments[0].value?.toString()
            ?: return

        val className = $$$"$$$protocolValue$$ProtocolImpl$$Get"

        val typeSpec = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.PUBLIC, KModifier.FINAL)
            .addSuperinterface(ClassName("com.protocol.annotation", "IProtocolImplProvider"))
            .addFunction(
                FunSpec.builder("getProtocolImpl")
                    .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
                    .returns(String::class)
                    .addStatement("return %S", clazz.toClassName().canonicalName)
                    .build()
            )
            .build()

        FileSpec.builder(Constants.GENERATED_PACKAGE, className)
            .addType(typeSpec)
            .build()
            .writeTo(
                environment.codeGenerator,
                Dependencies(
                    aggregating = false,              // ✅ isolating
                    clazz.containingFile!!
                )
            )
    }
}
