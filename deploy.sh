mvn clean package
scp -i ~/mzoamci.pem target/pipeline-monitor.hpi root@10.183.40.203:~/jenkins/plugins
ssh -i ~/mzoamci.pem root@10.183.40.203 "docker restart b4fe370ffcab"
until $(curl --output /dev/null --silent --head --fail http://10.183.40.203:49001); do printf '.'; sleep 1; done
ssh -i ~/mzoamci.pem root@10.183.40.203 "docker logs b4fe370ffcab | tail -50"