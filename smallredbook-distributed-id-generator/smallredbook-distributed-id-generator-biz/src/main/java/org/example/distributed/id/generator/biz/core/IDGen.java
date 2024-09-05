package org.example.distributed.id.generator.biz.core;


import org.example.distributed.id.generator.biz.core.common.Result;

public interface IDGen {
    Result get(String key);
    boolean init();
}
