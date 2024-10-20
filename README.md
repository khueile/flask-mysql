# flask-with-sqlite

Clone this Repo

```
git clone git@github.com:kshyam/flask-with-sqlite.git
cd flask-with-sqlite
```

Or Download form Zip

```
https://github.com/kshyam/flask-with-sqlite/archive/refs/heads/main.zip

```

cd flask-with-sqlite

```
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```

Start you Flask App in debug mode

``` 
flask --debug run
```

Open all at 

```
http://localhost:5000/

```


```
cd flask-with-sqlite

docker build -t mysql_image -f dockerfile_mysql .
docker network create appnw

docker run -p 3306:3306 -d --network appnw --name mysql_container -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=student_database mysql_image

docker exec -it mysql_container mysql -uroot -proot

docker build -t app_image -f dockerfile_app .
docker run -p 5000:5000 -d --network appnw --name app_container app_image
```
curl http://127.0.0.1:5000/list
curl -X POST -d "name=lmao&address=lmao&city=lmao&pin=lmao" http://127.0.0.1:5000/addrec

'''
docker build -t app_image -f dockerfile_app .
docker tag app_image khueile/app_image
docker push khueile/app_image
kubectl rollout restart deployment/flask-deployment

docker build -t mysql_image -f dockerfile_mysql .
docker tag mysql_image khueile/mysql_image
docker push khueile/mysql_image

'''
kubectl delete deployment flask-deployment
kubectl apply -f flask-deployment.yaml
kubectl get pods
kubectl port-forward pod/flask-deployment-67dddddf-jjdwq 5000:5000
'''