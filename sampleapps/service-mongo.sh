docker service create --network custom_overlay --name mymongodb --publish 27017:27017 --container-label sanalytics.slice="slice0" --label sanalytics.service.slice="slice0" cproinger/mongo-with-prom-exporter