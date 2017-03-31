docker service create -p 9090:9090 --mount type=bind,source=/mastergit/sanalytics/sampleapps/prom/,target=/etc/prometheus/ --name=prom_platform --network="custom_monitoring" --network="platform_overlay" prom/prometheus -config.file=/etc/prometheus/prometheus-platform.yml -storage.local.path=/prometheus -web.console.libraries=/etc/prometheus/console_libraries -web.console.templates=/etc/prometheus/consoles