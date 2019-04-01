package com.mavsoft.label.Helpers;

import java.util.Hashtable;

/**
 * Created by Anthony Gordon on 11/09/2017.
 */

//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

public class IntentHelper {

    private static IntentHelper _instance;
    private Hashtable<String, Object> _hash;

    private IntentHelper() {
        _hash = new Hashtable<String, Object>();
    }

    private static IntentHelper getInstance() {
        if(_instance==null) {
            _instance = new IntentHelper();
        }
        return _instance;
    }

    public static void addObjectForKey(Object object, String key) {
        getInstance()._hash.put(key, object);
    }

    public static Object getObjectForKey(String key) {
        IntentHelper helper = getInstance();
        Object data = helper._hash.get(key);
        helper._hash.remove(key);
        return data;
    }
}
