# Pizza Online

## Overview

The implementation only covers the basic features to illustrate how to build RESTful API endpoints using Spring Boot and related libraries.

There are two modules, namely API (/api) and engine (/engine). The API module is an public interface of the system to accept requests which are then routed to the engine for further processing. Since the engine is sitting behind the API module, it is possible to running the engine module in a private network for better security.

The environment specific informaiton is stored in the application.properties file of the two modules. The application.properties file can be updated when deploying to different environments. In order to simplify the on-going maintanence, default values are specified in the POM file in the root folder which will be used to update the corresponding values in the application.properties files.

The system can be run as standalone Spring Boot application or deployed to Kubernetes with Istio. (Minibuke is used in the local environment for testing). Commands to run the application are provided in the section below.

## Java Version

Open JDK 11.0.5 is use for development and testing.

## Environment Setup

To start the system as standalone applications, it is only necessary to have Java JDK installed.

To deploy and run the system using Kubernetes, it is necessary to install the following tools

- [skaffold](https://skaffold.dev/)
- [Minikube](https://minikube.sigs.k8s.io/docs/)
- [Istio](https://istio.io/)

Docker containers are created and deployed to Minikube. (jib)[https://github.com/GoogleContainerTools/jib] is used to create the Docker container image for deployment.

## Steps to Test

Command line tool curl is used to test the endpoints.

### Spring Boot Standalone Mode

Run the following command in the module folders (i.e. api and engine) respectively

```bash
mvn spring-boot:run
```

To test the endpoint in the API module, run the following command with a sample input

```bash
curl -XPOST -H 'Authorization: Bearer xxx:xxx:yyy' http://localhost:9081/orders -d '{"items":[{"name":"testing again","quantity":10,"price":10}]}'
```

Data is stored in in the embedded H2 database which can be access [here](http://localhost:9081/h2-console)

### MiniKube Deployment

Make sure the required libraries and tools are properly installed. Run the following command in the module folders (i.e. api and engine) respectively

```bash
skaffold dev --no-prune=false --cache-artifacts=false --no-prune-children=false
```

Run the following command to check the IP address and port number to access the application
(Note: assuming node port in istio is used and no external load balancer )

```bash
export INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="http2")].nodePort}')
export SECURE_INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="https")].nodePort}')
export TCP_INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="tcp")].nodePort}')
export INGRESS_HOST=$(minikube ip)
```

To access the endpoint in the API module, run the following command

```bash
# Host HTTP header must be specified using the given value which is hardcoded in the Istio configuration file in deployment
curl -XPOST -H 'Host: api.ntunghoi.com' -H 'Authorization: Bearer xxx:xxx:yyy' "http://$INGRESS_HOST:$INGRESS_PORT/orders" -d '{"items":[{"name":"testing again","quantity":10,"price":10}]}'
```

## Limitations

1. Data validation is only implemented in the engine module before inserting into the database
2. Unit tests are implemented only in the engine module (since the implementation details are similar in the API module)
3. No documentation on the API endpoint is provided. One option is to build the API document using Swagger
4. Use JWT for the authorization HTTP header to verify against OAuth provider
5. Instead of using RESTful API to communicate between the two modules, it may be better to use message queue for asynchronious communication. This provides better system stability as the requests are stored in the message queue even if the engine module is not available for whatever reason. Once the engine is up and running again, it can process any pending requests in the message queue

## Commands

```bash
(cd api; mvn spring-boot:run)
```

## H2

```SQL
CREATE TABLE tmp (id uuid NOT NULL default random_uuid() PRIMARY KEY, message VARCHAR(255) NOT NULL)
```

## Skaffold

```bash
# Installation (https://skaffold.dev/docs/install/)
brew install skaffold
```

## Minikube

```bash
# https://minikube.sigs.k8s.io/docs/start/
# Install
curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-darwin-amd64
sudo install minikube-darwin-amd64 /usr/local/bin/minikube

# Start
/usr/local/bin/minibuke start

# Stop
minibuke stop
```

## Istio

```bash
# Download the binrary (https://istio.io/latest/docs/setup/getting-started/#download)
curl -L https://istio.io/downloadIstio | sh -
# Move folder to an appropriate location and add the subfolder bin to the environment variable PATH

# Install
istioctl install

# Ingress Gateway (https://istio.io/latest/docs/tasks/traffic-management/ingress/ingress-control/)
```

## Deployment

```bash
cd engine
skaffold dev --no-prune=false --cache-artifacts=false --no-prune-children=false

kubectl get service engine
minikube service engine --url

kubectl exec --stdin --tty <pod name> -- /bin/bash
```

## Testing

```bash
curl -H 'Authorization: Bearer someone:someone:debug' localhost:9081/orders

curl -XPOST -v -H 'Content-Type: application/json' -HHost:engine.ntunghoi.com -H 'Authorization: Bearer someone:someone:someone'  http://192.168.64.4:32387/orders -d '{"items":[{"name":"testing again","quantity":10,"price":10}]}'

curl -v -HHost:engine.ntunghoi.com -H 'Authorization: Bearer someone:someone:someone'  http://192.168.64.4:32387/orders
```
