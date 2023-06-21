package com.dunpju.stubs;

import lombok.Setter;

@Setter
public class MapperXmlStub {
    private String namespace;

    public String stub() {
        String tpl = """
                <?xml version="1.0" encoding="UTF-8"?>
                <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
                <mapper namespace="%NAMESPACE%">
                </mapper>""";
        tpl = tpl.replaceAll("%NAMESPACE%", this.namespace);
        return tpl;
    }
}
