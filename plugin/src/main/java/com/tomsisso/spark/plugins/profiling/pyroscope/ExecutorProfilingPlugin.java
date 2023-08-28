package com.tomsisso.spark.plugins.profiling.pyroscope;

import io.pyroscope.javaagent.PyroscopeAgent;
import io.pyroscope.javaagent.config.Config;
import io.pyroscope.labels.LabelsSet;
import io.pyroscope.labels.ScopedContext;
import org.apache.spark.TaskContext;
import org.apache.spark.TaskFailedReason;
import org.apache.spark.api.plugin.ExecutorPlugin;
import org.apache.spark.api.plugin.PluginContext;

import java.util.HashMap;
import java.util.Map;

public class ExecutorProfilingPlugin implements ExecutorPlugin {
    private final ThreadLocal<ScopedContext> threadLocalScopedContext = new ThreadLocal<>();

    @Override
    public void init(PluginContext ctx, Map<String, String> extraConf) {
        Config config = PyroscopeConfigFactory.of(ctx, extraConf);
        PyroscopeAgent.start(config);
    }

    @Override
    public void onTaskStart() {
        setCurrentContext();
    }

    @Override
    public void onTaskSucceeded() {
        clearCurrentContext();
    }

    @Override
    public void onTaskFailed(TaskFailedReason failureReason) {
        clearCurrentContext();
    }

    private void setCurrentContext() {
        Map<String, String> labels = new HashMap<>();
        labels.put("stage", "stage-" + TaskContext.get().stageId());
        labels.put("partition", "partition-" + TaskContext.get().partitionId());

        //This sets the contextId of the current thread for the profiler
        ScopedContext scopedContext = new ScopedContext(new LabelsSet(labels));
        //we keep the scopedContext locally as well, in order to close the relevant instance when a task completes.
        //when task completes our callback will be called from the same thread
        threadLocalScopedContext.set(scopedContext);
    }

    private void clearCurrentContext() {
        ScopedContext currentContext = threadLocalScopedContext.get();
        if (currentContext != null) {
            currentContext.close();
        }
    }
}
