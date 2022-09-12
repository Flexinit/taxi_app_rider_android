package com.quawlebs.drupp.helpers;

import java.util.HashMap;

public interface IPaymentFlowObserver {
    void onResponse(HashMap<String, String> response);
}
