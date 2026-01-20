package com.protocol.processor



/**
 * 常量定义
 * 这里只放“编译期用”的字符串，避免硬编码散落各处
 * Created by lianyagang on 2026年01月20日17:09:04
 */
object Constants {

    /** 注解所在包名 */
    private const val ANNOTATION_PACKAGE = "com.protocol.annotation"

    /** @Protocol */
    const val ANNOTATION_PROTOCOL = "$ANNOTATION_PACKAGE.Protocol"

    /** @ProtocolImpl */
    const val ANNOTATION_PROTOCOL_IMPL = "$ANNOTATION_PACKAGE.ProtocolImpl"

    /** 生成代码统一放到这个包 */
    const val GENERATED_PACKAGE = "com.protocol.provider"
}


