package com.mymarket.site;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.SystemInfo;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/status")
@RequiredArgsConstructor
public class StatusController {

    @SneakyThrows
    @GetMapping
    public Status getStatus() {
        var systemInfo = new SystemInfo();
        var os = systemInfo.getOperatingSystem();
        var processor = systemInfo.getHardware().getProcessor();

        long[] prevTicks = processor.getSystemCpuLoadTicks();
        TimeUnit.MILLISECONDS.sleep(500);
        double cpuLoad = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;

        var memory = systemInfo.getHardware().getMemory();

        return Status.builder()
            .systemUptime(os.getSystemUptime())
            .healthy(true)
            .processorCount(processor.getPhysicalProcessorCount())
            .cpuLoad(cpuLoad)
            .memoryTotal(memory.getTotal() / 1024 / 1024 + " MB")
            .memoryAvailable(memory.getAvailable() / 1024 / 1024 + " MB")
            .build();
    }
}
