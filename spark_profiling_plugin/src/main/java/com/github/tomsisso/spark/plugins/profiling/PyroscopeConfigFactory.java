package com.github.tomsisso.spark.plugins.profiling;

import com.google.common.base.Objects;
import io.pyroscope.http.Format;
import io.pyroscope.javaagent.EventType;
import io.pyroscope.javaagent.config.Config;
import org.apache.spark.api.plugin.PluginContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class PyroscopeConfigFactory {
    public static Config createConfig(PluginContext pluginContext,
                                      Map<String, String> extraConfMap) {
        Map<String, String> staticLabels = new HashMap<>();
        String executor = ("driver".equals(pluginContext.executorID())) ? "driver" : "executor-" + pluginContext.executorID();
        staticLabels.put("executor", executor);

        String appName = "spark-" + pluginContext.conf().get("spark.app.name");
        String serverAddress = extraConfMap.get("server.address");
        EventType eventType = EventType.fromId(Objects.firstNonNull(extraConfMap.get("events"), "itimer"));
        Duration profilingInterval = Duration.ofMillis(Integer.valueOf(Objects.firstNonNull(extraConfMap.get("profiling.interval.ms"), "99")));
        Duration uploadInterval = Duration.ofSeconds(Integer.valueOf(Objects.firstNonNull(extraConfMap.get("upload.interval.seconds"), "10")));
        String allocBytes = Objects.firstNonNull(extraConfMap.get("alloc.bytes"), "1000");
        String lockNanos = Objects.firstNonNull(extraConfMap.get("lock.ns"), "1000");

        return new Config.Builder()
                .setApplicationName(appName)
                .setServerAddress(serverAddress)
                .setFormat(Format.JFR)
                .setProfilingEvent(eventType)
                .setProfilingInterval(profilingInterval)
                .setUploadInterval(uploadInterval)
                .setProfilingAlloc(allocBytes)
                .setProfilingLock(lockNanos)
                .setLabels(staticLabels)
                .build();
    }
}
