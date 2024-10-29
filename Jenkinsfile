pipeline {
    agent any
    // triggers {
    //     pollSCM('H/2 * * * *') // Poll SCM every 2 minutes
    // }
    environment {
        // DOCKER_HUB_CREDENTIALS = credentials('docker_hub_credentials_id')
        GITHUB_CREDENTIALS = credentials('id_rsa_github_personal')
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
                    withDockerRegistry([credentialsId: 'docker_hub_credentials_id', url: 'https://index.docker.io/v1/']) {
                        sh 'docker push khueile/app_image:latest'
                    }
                }
            }
        }
    }
}