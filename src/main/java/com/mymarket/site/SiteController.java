package com.mymarket.site;

import com.mymarket.membership.member.MemberService;
import com.mymarket.productcategory.ProductCategoryService;
import com.mymarket.web.ApplicationLocaleHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.SystemInfo;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/sites")
@RequiredArgsConstructor
public class SiteController {

    private final ProductCategoryService productCategoryService;
    private final MemberService memberService;

    @GetMapping("/metadata")
    public SiteMetadata getActiveProductCategories(HttpServletRequest request) {
        var siteMetadataBuilder = SiteMetadata.builder()
            .supportedLocales(ApplicationLocaleHolder.getSupportedLocales().stream()
                .map(it -> it.toString().replace("_", "-"))
                .toList()
            )
            .productCategories(productCategoryService.getNavBarProductCategories());

        var currentMember = memberService.getCurrentMember();
        if (currentMember.isPresent()) {
            return siteMetadataBuilder.loggedIn(true)
                .memberName(currentMember.get().getFirstName())
                .admin(currentMember.get().isAdmin())
                .storeOwner(currentMember.get().isStoreOwner())
                .build();
        } else {
            return siteMetadataBuilder.loggedIn(false).build();
        }
    }


    @GetMapping("/csrf")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @SneakyThrows
    @GetMapping("/status")
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
