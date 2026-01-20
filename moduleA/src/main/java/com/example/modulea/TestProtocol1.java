package com.example.modulea;

import androidx.annotation.Keep;

import com.protocol.annotation.Protocol;

/**
 * Created by lianyagang on 2019/5/8
 */
@Protocol("ModuleBProtocol1")
@Keep
public interface TestProtocol1 {

    String getData1(String string);
    String getTest();
    String getTest2();
    String getTest3();
    String getTest4();
}
