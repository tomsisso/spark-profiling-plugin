package com.github.tomsisso.spark.plugins.profiling;

import io.pyroscope.javaagent.PyroscopeAgent;
import io.pyroscope.javaagent.config.Config;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.plugin.DriverPlugin;
import org.apache.spark.api.plugin.PluginContext;
import scala.Tuple2;

import java.util.HashMap;
import java.util.Map;

public class DriverProfilingPlugin implements DriverPlugin {

    @Override
    public Map<String, String> init(SparkContext sc, PluginContext pluginContext) {
        Map<String, String> pluginConfigsMap = buildConfigsMap(sc);

        Config config = PyroscopeConfigFactory.createConfig(pluginContext, pluginConfigsMap);
        PyroscopeAgent.start(config);

        return pluginConfigsMap;
    }

    private static Map<String, String> buildConfigsMap(SparkContext sc) {
        Tuple2<String, String>[] conf = JavaSparkContext.fromSparkContext(sc)
                .getConf()
                .getAllWithPrefix("spark.plugins.profiling.plugin.");

        Map<String, String> map = new HashMap<>(conf.length);
        for (Tuple2<String, String> tuple : conf) {
            map.put(tuple._1(), tuple._2());
        }
        return map;
    }
}
