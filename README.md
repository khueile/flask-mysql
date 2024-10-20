# flask-mysql

This repo can be used to prop up a 2-tier simple web application. The logic, as well as basic html interfacing, is handled by Flask. It interacts with a MySQL database (`student_database`) that has one table (`students`) in it to store student data.

Here are a few ways to go about propping this application up, with increasingly more complicated CICD wrangling, for educational purposes. 

## Method 0: Rawdogging it
This part is purely for record-keeping. The database has been replaced by mysql from the original sqlite, so this method no longer works.
Set up a virtual env with specific requirements
```
cd flask-mysql
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```
Start your Flask App in debug mode
``` 
flask --debug run
```
Check things out on your browser at
```
http://localhost:5000/
```

## Method 1: Pure docker
We rely on 2 containers here, one for the flask app and one for the mysql database.

Build the 2 images
```
cd flask-mysql
docker build -t mysql_image -f dockerfile_mysql .
docker build -t app_image -f dockerfile_app .
```

Create the network for the 2 containers to live on and interract with each other
```
docker network create appnw
```

Run the 2 containers:
```
docker run -p 3306:3306 -d --network appnw --name mysql_container -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=student_database mysql_image
docker run -p 5000:5000 -d --network appnw --name app_container app_image
```

Check out the application at `http://127.0.0.1:5000`
You can also use curl to test:
```
curl http://127.0.0.1:5000/list
curl -X POST -d "name=lmao&address=lmao&city=lmao&pin=lmao" http://127.0.0.1:5000/addrec
```

You can also checkout the content of mysql database container by doing
```
docker exec -it mysql_container mysql -uroot -proot
```

## Method 2: Add Kubernetes
The application will now consist of 2 deployments and 2 services:
- flask-deployment for the flask app
- mysql-deployment for the mysql database
- flask-service is a NodePort service that exposes the flask-deployment to allow us to interact with the Flask application from outside the cluster
- mysql-service is a ClusterIP service that allows the flask app side to read from and write to mysql side.

Create the cluster:
- Download Docker Desktop
- Open Docker Desktop > Preferences > Kubernetes
- Choose Enable Kubernetes
- Choose Apply and restart

Install kubectl: On my macbook it's `brew install kubectl`. After confirming it's working, check to use docker-desktop cluster:
```
kubectl config use-context docker-desktop
```

The 2 deployments are created using images from `dockerfile_app` and `dockerfile_mysql`.
We have set up 2 repo on docker hub to store these images: 
https://hub.docker.com/repository/docker/khueile/app_image/general
https://hub.docker.com/repository/docker/khueile/mysql_image/general
Here's how to build, tag, and push the 2 images to docker hub:

```
cd flask-mysql
docker build -t app_image -f dockerfile_app .
docker tag app_image khueile/app_image
docker push khueile/app_image

docker build -t mysql_image -f dockerfile_mysql .
docker tag mysql_image khueile/mysql_image
docker push khueile/mysql_image
```
To start the deployments and services:
```
cd flask-mysql/k8s
kubectl apply -f .
```
We should have something like this:
```
Minhs-MacBook-Pro:k8s minhkhuele$ kubectl get all
NAME                                    READY   STATUS    RESTARTS   AGE
pod/flask-deployment-67f9dddddf-jfgzk   1/1     Running   1          107m
pod/mysql-deployment-cf5f8c8fd-qxvr7    1/1     Running   1          147m

NAME                    TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
service/flask-service   NodePort    10.103.191.191   <none>        5000:30000/TCP   3h38m
service/kubernetes      ClusterIP   10.96.0.1        <none>        443/TCP          4d2h
service/mysql-service   ClusterIP   10.106.243.70    <none>        3306/TCP         4d1h

NAME                               READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/flask-deployment   1/1     1            1           107m
deployment.apps/mysql-deployment   1/1     1            1           4d1h

NAME                                          DESIRED   CURRENT   READY   AGE
replicaset.apps/flask-deployment-67f9dddddf   1         1         1       107m
replicaset.apps/mysql-deployment-cf5f8c8fd    1         1         1       147m
```

Now we can check out the app at `http://localhost:30000/`
