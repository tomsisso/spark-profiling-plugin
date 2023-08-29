# Spark Profiling Plugin 

### Intro
This plugin allows profiling a spark application. It collects profiling data both from the driver and the executors, to get a deep view of the application's cpu, memory allocations, locks, etc.
It combines 2 components:
1. [Pyroscope Java agent](https://grafana.com/docs/pyroscope/next/configure-client/language-sdks/java/) - to collect the profiling samples from the java processes and report them to a Pyroscope server.
2. [Apache Spark plugin](https://spark.apache.org/docs/latest/api/java/index.html?org/apache/spark/api/plugin/SparkPlugin.html) - custom plugin implementation for starting the agent on every node, while attaching a context to the profiling data in runtime. 
It's using the plugin's onTaskStart/onTaskSucceeded/onTaskFailed callbacks to mark the execution flow with the relevant context labels: executor, stage and partition.
The result allows investigating the profiling data in multiple ways - either as a whole, using an aggregated view of the entire app, or as some zoom-in view by breaking it down by executor, stage or partition.

### Using it
1. Download or build the plugin jar. For building it on your own - run the following command from the spark_profiling_plugin directory:
   ```
   mvn clean package
   ```
    1. You should get:
       1. /spark_profiling_plugin/target/spark_profiling_plugin-1.0.0-jar-with-dependencies.jar
2. Submit your spark job including the plugin configurations. e.g:
   ``` 
   /spark-submit \
   ... your arguments ... \
   --jars <YOUR_PATH_TO_THE_PLUGIN_JAR_LOCATION>/spark_profiling_plugin-1.0.0-jar-with-dependencies.jar \
   --conf spark.plugins='com.github.tomsisso.spark.plugins.profiling.SparkProfilingPlugin' \
   --conf spark.plugins.profiling.plugin.server.address='http://<YOUR_PYROSCOPE_SERVER>:4040' \
   --conf spark.plugins.profiling.plugin.upload.interval.seconds=10 \
   /<YOUR_APP>.jar
   ```
   
### Demo
This repo includes an E2E demo environment including docker-compose.yml with the relevant Spark, Pyroscope and Grafana dockers (& with a predefined grafana dashboard).

Start [here](https://github.com/tomsisso/spark-profiling-plugin/blob/main/demo_app/README.md)

![Demo Dashboard](https://github.com/tomsisso/spark-profiling-plugin/assets/25052755/cc36dd67-9c1c-488f-9e4b-e6ced03912c3)