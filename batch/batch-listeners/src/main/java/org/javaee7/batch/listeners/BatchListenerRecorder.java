package org.javaee7.batch.listeners;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Roberto Cortez
 */
@Singleton
public class BatchListenerRecorder {
    private Map<String, Integer> recordedMethodExecutions = new HashMap<>();

    public void addListenerMethodExecution(Class klass, String method) {
        String key = getKey(klass, method);
        if (recordedMethodExecutions.containsKey(key)) {
            recordedMethodExecutions.put(key, recordedMethodExecutions.get(key) + 1);
        } else {
            recordedMethodExecutions.put(key, 1);
        }
    }

    public boolean isListenerMethodExecuted(Class klass, String method) {
        return recordedMethodExecutions.containsKey(getKey(klass, method));
    }

    public Integer totalListenerMethodExecutions(Class klass, String method) {
        Integer total = recordedMethodExecutions.get(getKey(klass, method));
        return total != null ? total : Integer.valueOf(0);
    }

    private String getKey(Class klass, String method) {
        return klass.getName() + "." + method;
    }
}
