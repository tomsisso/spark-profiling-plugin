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
   /opt/spark/bin/spark-submit --deploy-mode cluster
   --master spark://spark-master:7077 \
   --total-executor-cores 1 \
   --class com.tomsisso.spark.plugins.Demo \
   --driver-memory 1G \
   --executor-memory 1G \
   --jars /opt/spark-jars/spark_profiling_plugin-1.0-beta.jar \
   --conf spark.plugins='com.tomsisso.spark.plugins.profiling.pyroscope.SparkProfilingPlugin' \
   /opt/spark-jars/spark_app-1.0-beta.jar
   ```
5. go to grafana at http://grafana:3000/datasources (admin, admin), and add pyroscope plugin:
   ```
   Field	Value
   Name	Pyroscope
   URL	http://pyroscope:4040/
   ```