package com.mymarket.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mymarket.membership.member.MemberNotLoggedInException;
import com.mymarket.membership.member.MemberService;
import com.mymarket.store.StoreValidator;
import com.mymarket.web.error.ApplicationValidator;
import jakarta.validation.Validator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductValidator implements ApplicationValidator<Product> {

    @Getter
    private final Validator validator;
    private final ObjectMapper objectMapper;
    private final MemberService memberService;
    private final StoreValidator storeValidator;

    @Override
    public void addCustomErrors(Product product, BindingResult bindingResult) {
        var price = product.getPrice();
        if (price != null) {
            if (price.getAmount() != null && price.getCurrency() == null) {
                bindingResult.rejectValue("price.currency", "", "{error.price.currency}");
            }
            if (price.getAmount() == null && price.getCurrency() != null) {
                bindingResult.rejectValue("price.amount", "", "{error.price.amount}");
            }
        }
        if (StringUtils.hasText(product.getSpecs())) {
            try {
                objectMapper.readValue(product.getSpecs(), Map.class);
            } catch (Exception ex) {
                bindingResult.rejectValue("specs", "", "{error.specs}");
            }
        }
        var currentMember = memberService.getCurrentMember()
            .orElseThrow(MemberNotLoggedInException::new);
        if (product.getStore() != null && storeValidator.isOthersStore(currentMember.getId(), product.getStore().getId())) {
            bindingResult.rejectValue("store", "", "{store-violation}");
        }
    }
}
