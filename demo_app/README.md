# Spark Profiling Plugin - Demo

1. Build the jars (plugin & demo app). From the project root, run:
   ```
   mvn clean package
   ```
   1. You should get:
      1. ./demo_app/target/demo_app-1.0.0.jar
      2. ./spark_profiling_plugin/target/spark_profiling_plugin-1.0.0-jar-with-dependencies.jar
3. Setup the environment, from the demo_app dir (pyroscope server, grafana server, and spark standalone cluster of master & worker): 
   ``` 
   docker-compose up 
   ```
4. Submit the demo spark job to the cluster, from the host:
   ``` 
   docker exec demo_app_spark-master_1 \
   /opt/bitnami/spark/bin/spark-submit \
   --deploy-mode cluster \
   --master spark://spark-master:7077 \
   --name "my-demo-app" \
   --conf spark.driver.cores=1 \
   --conf spark.driver.memory=1g \
   --conf spark.executor.cores=1 \
   --conf spark.executor.memory=1g \
   --class com.github.tomsisso.spark.plugins.DemoSparkApp \
   --jars /opt/spark-jars/plugin/spark_profiling_plugin-1.0.0-jar-with-dependencies.jar \
   --conf spark.plugins='com.github.tomsisso.spark.plugins.profiling.SparkProfilingPlugin' \
   --conf spark.plugins.profiling.plugin.server.address='http://pyroscope:4040' \
   --conf spark.plugins.profiling.plugin.upload.interval.seconds=5 \
   /opt/spark-jars/app/demo_app-1.0.0.jar
   ```

8. Services are accessible from the host:
   1. Grafana at http://localhost:3000/. User=admin, password=admin. Check "Spark Profiling" dashboard.
   2. Spark cluster master: http://localhost:8080/
   3. Spark UI: http://localhost:4040/