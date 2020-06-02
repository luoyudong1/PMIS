package com.kthw.pmis.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SystemConfig {
    @Value("${js.version}")//jsVersion
    public String jsVersion;

    @Value("${js.date}")
    public String jsDate;

    @Value("${js.time}")
    public String jsTime;
}
