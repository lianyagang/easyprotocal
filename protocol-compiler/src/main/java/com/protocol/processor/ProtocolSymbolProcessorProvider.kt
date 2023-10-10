package com.protocol.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

/**
 * 注解处理器提供者
 *
 * @author HB.zhuanghngzhan
 * @date 2023-04-18
 */
class ProtocolSymbolProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return ProtocolSymbolProcessor(environment)
    }
}