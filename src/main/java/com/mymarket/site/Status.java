package com.mymarket.site;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Status {
    boolean healthy;
    long systemUptime;
    int processorCount;
    double cpuLoad;
    String memoryTotal;
    String memoryAvailable;
}
