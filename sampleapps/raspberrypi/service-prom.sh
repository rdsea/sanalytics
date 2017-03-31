docker service create -p 9090:9090 --mount type=bind,source=/home/pi/cproinger/configs/prom/,target=/etc/prometheus/ --mount type=bind,source=/home/pi/cproinger/data/prom-platform-data,target=/prometheus --name=prom_platform --network="platform_overlay" --constraint 'node.hostname == c1n1' cproinger/rpi-prometheus -config.file=/etc/prometheus/prometheus-platform.yml -storage.local.path=/prometheus -web.console.libraries=/etc/prometheus/console_libraries -web.console.templates=/etc/prometheus/consoles