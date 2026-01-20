package com.protocol.processor

import com.google.devtools.ksp.processing.*

/**
 *
 * KSP 通过它实例化真正的 SymbolProcessor
 */


class ProtocolSymbolProcessorProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return ProtocolSymbolProcessor(environment)
    }
}

