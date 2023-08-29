# spark profiling-plugin demo 

1. make sure ./data contains: 
   1. org.csv
2. make sure ./jars contains: 
   1. spark_app-1.0-beta.jar
   2. spark_profiling_plugin-1.0-beta-jar-with-dependencies.jar
3. setup the environment (spark standalone cluster of master & worker, pyroscope & grafana): 
   ``` 
   docker-compose up 
   ```
4. submit the demo job using one of the nodes:
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

8. go to grafana at http://localhost:3000/ - (admin, admin). and use Spark Profiling dashboard