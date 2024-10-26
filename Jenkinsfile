pipeline {
    agent any
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
            when {
                branch 'main'
            }
            steps {
                git branch: 'main', url: 'https://github.com/khueile/flask-mysql'
            }
        }
        stage('Build App Image') {
            steps {
                script {
                    sh 'docker build -t ${APP_IMAGE} -f dockerfile_app .'
                    sh 'docker tag ${APP_IMAGE} ${DOCKER_HUB_ACC}/${APP_IMAGE}:latest'
                }
            }
        }
        stage('Push App Image') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', DOCKER_HUB_CREDENTIALS) {
                        sh 'docker push ${DOCKER_HUB_ACC}/${APP_IMAGE}:latest'
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