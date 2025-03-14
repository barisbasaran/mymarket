package com.mymarket.membership.member;

import com.mymarket.web.error.ApplicationException;

public class MemberNotLoggedInException extends ApplicationException {

    public MemberNotLoggedInException() {
        super("member-not-logged-in");
    }
}
