package com.example.modulea;

import androidx.annotation.Keep;

import com.protocol.annotation.Protocol;

/**
 * Created by lianyagang on 2019/5/8
 */
@Protocol("ModuleBProtocol")
@Keep
public interface TestProtocol {

    String getData();
    String getTest();
}
