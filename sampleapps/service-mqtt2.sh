docker service create --network custom_overlay --name mqtt2 --container-label sanalytics.slice="slice0" --label sanalytics.slice="slice0" -p 1884:1883 toke/mosquitto:release-1.4.10-2