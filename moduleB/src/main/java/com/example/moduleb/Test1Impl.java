package com.example.moduleb;

import androidx.annotation.Keep;

import com.protocol.annotation.ProtocolImpl;

/**
 * Created by lianyagang on 2019/5/8
 */
@ProtocolImpl("ModuleBProtocol1")
@Keep
public class Test1Impl {

    public String getData1(String string) {
        return "来自模块B的数据整合一个文件的"+string;
    }

}
