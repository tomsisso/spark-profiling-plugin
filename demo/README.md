# Spark Profiling Plugin Demo

1. Build the project jars. From the project root, run:
   ```
   mvn clean package
   ```
   1. You should get:
      1. ./demo/app/target/spark_app-1.0-beta.jar
      2. ./plugin/target/spark_profiling_plugin-1.0-beta-jar-with-dependencies.jar
3. Setup the environment (pyroscope server, grafana server, and spark standalone cluster of master & worker): 
   ``` 
   docker-compose up 
   ```
4. Submit the demo spark job to the cluster:
   ``` 
   docker exec demo_spark-master_1 \
   /opt/bitnami/spark/bin/spark-submit \
   --deploy-mode cluster \
   --master spark://spark-master:7077 \
   --name "my-demo-app" \
   --conf spark.driver.cores=1 \
   --conf spark.driver.memory=1g \
   --conf spark.executor.cores=1 \
   --conf spark.executor.memory=1g \
   --class com.tomsisso.spark.plugins.Demo \
   --jars /opt/spark-jars/plugin/spark_profiling_plugin-1.0-beta-jar-with-dependencies.jar \
   --conf spark.plugins='com.tomsisso.spark.plugins.profiling.pyroscope.SparkProfilingPlugin' \
   --conf spark.plugins.profiling.pyroscope.plugin.server.address='http://pyroscope:4040' \
   --conf spark.plugins.profiling.pyroscope.plugin.upload.interval.seconds=1 \
   /opt/spark-jars/app/spark_app-1.0-beta.jar
   ```

8. Services are accessible from the host:
   1. Grafana at http://localhost:3000/. User & pass: admin, admin. Check "Spark Profiling" dashboard.
   2. Spark cluster master: http://localhost:8080/
   3. Spark UI: http://localhost:4040/