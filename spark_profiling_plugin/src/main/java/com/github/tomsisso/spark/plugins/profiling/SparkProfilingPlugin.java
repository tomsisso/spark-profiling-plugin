package com.github.tomsisso.spark.plugins.profiling;

import org.apache.spark.api.plugin.DriverPlugin;
import org.apache.spark.api.plugin.ExecutorPlugin;
import org.apache.spark.api.plugin.SparkPlugin;

public class SparkProfilingPlugin implements SparkPlugin {

    @Override
    public DriverPlugin driverPlugin() {
        return new DriverProfilingPlugin();
    }

    @Override
    public ExecutorPlugin executorPlugin() {
        return new ExecutorProfilingPlugin();
    }
}
