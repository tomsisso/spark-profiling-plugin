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

    public static final String SERVER_ADDRESS_CONF = "server.address";
    public static final String EVENTS_CONF = "events";
    public static final String PROFILING_INTERVAL_MS_CONF = "profiling.interval.ms";
    public static final String UPLOAD_INTERVAL_SECONDS_CONF = "upload.interval.seconds";
    public static final String ALLOC_BYTES_CONF = "alloc.bytes";
    public static final String LOCK_NS_CONF = "lock.ns";

    public static Config createConfig(PluginContext pluginContext,
                                      Map<String, String> extraConfMap) {
        Map<String, String> staticLabels = new HashMap<>();
        String executor = ("driver".equals(pluginContext.executorID())) ? "driver" : "executor-" + pluginContext.executorID();
        staticLabels.put("executor", executor);

        String appName = "spark-" + pluginContext.conf().get("spark.app.name");
        String serverAddress = extraConfMap.get(SERVER_ADDRESS_CONF);
        EventType eventType = EventType.fromId(Objects.firstNonNull(extraConfMap.get(EVENTS_CONF), "itimer"));
        Duration profilingInterval = Duration.ofMillis(Integer.parseInt(Objects.firstNonNull(extraConfMap.get(PROFILING_INTERVAL_MS_CONF), "99")));
        Duration uploadInterval = Duration.ofSeconds(Integer.parseInt(Objects.firstNonNull(extraConfMap.get(UPLOAD_INTERVAL_SECONDS_CONF), "5")));
        String allocBytes = Objects.firstNonNull(extraConfMap.get(ALLOC_BYTES_CONF), "1000");
        String lockNanos = Objects.firstNonNull(extraConfMap.get(LOCK_NS_CONF), "1000");

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
