package com.mymarket.store;

import com.mymarket.web.error.ApplicationException;

public class StoreNotFoundException extends ApplicationException {

    public StoreNotFoundException() {
        super("store-not-found");
    }
}
