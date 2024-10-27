pipeline {
    agent {
        kubernetes {
            yaml """
            apiVersion: v1
            kind: Pod
            spec:
              containers:
              - name: docker
                image: docker:latest
                command:
                - cat
                tty: true
                volumeMounts:
                - name: docker-socket
                  mountPath: /var/run/docker.sock
              volumes:
              - name: docker-socket
                hostPath:
                  path: /var/run/docker.sock
            """
        }
    }
    // triggers {
    //     pollSCM('H/2 * * * *') // Poll SCM every 2 minutes
    // }
    environment {
        DOCKER_HUB_CREDENTIALS = credentials('docker_hub_credentials_id')
        DOCKER_HUB_ACC = 'khueile'
        APP_IMAGE = 'app_image'
        MYSQL_IMAGE = 'mysql_image'
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/khueile/flask-mysql'
            }
        }
        stage('Build App Image') {
            steps {
                script {
                    appImage = docker.build("${DOCKER_HUB_ACC}/${APP_IMAGE}:latest", "-f dockerfile_app .")
                }
            }
        }
        stage('Push App Image') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', DOCKER_HUB_CREDENTIALS) {
                        appImage.push()
                    }
                }
            }
        }
        stage('Test') {
            steps {
                echo 'Testing...'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying...'
            }
        }
    }
}