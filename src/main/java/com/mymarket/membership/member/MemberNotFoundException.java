package com.mymarket.membership.member;

import com.mymarket.web.error.ApplicationException;

public class MemberNotFoundException extends ApplicationException {

    public MemberNotFoundException() {
        super("member-not-found");
    }
}
