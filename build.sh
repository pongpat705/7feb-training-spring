mvn clean package
#eval $(minikube docker-env)
docker build -t producerapp:1.0 .