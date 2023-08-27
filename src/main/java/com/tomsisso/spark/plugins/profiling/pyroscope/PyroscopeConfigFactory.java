package com.tomsisso.spark.plugins.profiling.pyroscope;

import com.google.common.base.Objects;
import io.pyroscope.http.Format;
import io.pyroscope.javaagent.EventType;
import io.pyroscope.javaagent.config.Config;
import org.apache.spark.api.plugin.PluginContext;

import java.util.HashMap;
import java.util.Map;

public class PyroscopeConfigFactory {
    public static Config of(PluginContext pluginContext,
                            Map<String, String> extraConfMap) {
        Map<String, String> staticLabels = new HashMap<>();
        staticLabels.put("host", pluginContext.hostname());
        staticLabels.put("executor", pluginContext.executorID());

        return new Config.Builder()
                .setApplicationName(pluginContext.conf().getAppId())
                .setServerAddress(extraConfMap.get("server.address"))//"http://pyroscope-server:4040"
                .setFormat(Format.JFR)
                .setProfilingEvent(EventType.valueOf(Objects.firstNonNull(extraConfMap.get("events"), "itimer")))
//                .setProfilingInterval(extraConfMap.get("profiling.interval"))
//                .setSamplingDuration(extraConfMap.get("sampling.duration"))
//                .setUploadInterval(extraConfMap.get("upload.interval"))
//                .setAllocLive(Boolean.valueOf(extraConfMap.get("alloc.live"))
                .setProfilingAlloc(Objects.firstNonNull(extraConfMap.get("alloc.bytes"), ""))
                .setProfilingLock(Objects.firstNonNull(extraConfMap.get("lock.ns"), ""))
                .setLabels(staticLabels)
                .build();

    }
}
