docker service create --network=platform_overlay -p 8888:8080 --mode global --name iotcadvisor --mount type=bind,source=/,target=/rootfs,readonly=true --mount type=bind,source=/var/run,target=/var/run,readonly=false --mount type=bind,source=/sys,target=/sys,readonly=true --mount type=bind,source=/var/lib/docker/,target=/var/lib/docker,readonly=true solidnerd/rpi-cadvisor