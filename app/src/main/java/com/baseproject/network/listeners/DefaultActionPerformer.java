package com.baseproject.network.listeners;

import java.util.HashMap;

/**
 * Created by Mushahid on 8/1/2017
 */

public interface DefaultActionPerformer {
    void onActionPerform(HashMap<String, String> headers, HashMap<String, String> params);
}
