# spark profiling-plugin demo 

1. make sure ./data contains: 
   1. org.csv
2. make sure ./jars contains: 
   1. spark_app-1.0-beta.jar
   2. spark_profiling_plugin-1.0-beta.jar
3. setup the environment (spark master, 2 workers, pyroscope & grafana): 
   ``` 
   docker-compose up --scale spark-worker=2
   ```
4. submit the demo spark app:
   1. login to spark master:
   ``` 
   docker exec -it spark-master /bin/bash
   ``` 
   2. submit the demo app job:
   ```
   /opt/bitnami/spark/bin/spark-submit --deploy-mode client \
   --master spark://spark-master:7077 \
   --name "my-spark-app" \
   --class com.tomsisso.spark.plugins.Demo \
   --jars /opt/spark-jars/spark_profiling_plugin-1.0-beta-jar-with-dependencies.jar \
   --conf spark.plugins='com.tomsisso.spark.plugins.profiling.pyroscope.SparkProfilingPlugin' \
   --conf spark.plugins.profiling.pyroscope.plugin.server.address='http://pyroscope:4040' \
   --conf spark.plugins.profiling.pyroscope.plugin.upload.interval.seconds=1 \
   /opt/spark-jars/spark_app-1.0-beta.jar
   
   /opt/bitnami/spark/bin/spark-submit --deploy-mode client \
   --master spark://spark-master:7077 \
   --name "my-spark-app" \
   --class com.tomsisso.spark.plugins.Demo \
   --jars /opt/spark-jars/spark_profiling_plugin-1.0-beta-jar-with-dependencies.jar \
   --conf spark.plugins='com.tomsisso.spark.plugins.profiling.pyroscope.SparkProfilingPlugin' \
   --conf spark.plugins.profiling.pyroscope.plugin.server.address='http://pyroscope:4040' \
   /opt/spark-jars/spark_app-1.0-beta.jar
   
   
   ```
5. go to grafana at http://grafana:3000/datasources (admin, admin), and add pyroscope plugin:
   ```
   Field	Value
   Name	Pyroscope
   URL	http://pyroscope:4040/
   ```