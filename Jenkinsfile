pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub-credentials-id')
        DOCKER_HUB_REPO = 'khueile'
        APP_IMAGE = 'app_image'
        MYSQL_IMAGE = 'mysql_image'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/your-repo/flask-mysql.git'
            }
        }
        // stage('Build App Image') {
        //     steps {
        //         script {
        //             sh 'docker build -t ${DOCKER_HUB_REPO}/${APP_IMAGE} -f dockerfile_app .'
        //             sh 'docker tag ${DOCKER_HUB_REPO}/${APP_IMAGE} ${DOCKER_HUB_REPO}/${APP_IMAGE}:latest'
        //         }
        //     }
        // }
        // stage('Build MySQL Image') {
        //     steps {
        //         script {
        //             sh 'docker build -t ${DOCKER_HUB_REPO}/${MYSQL_IMAGE} -f dockerfile_mysql .'
        //             sh 'docker tag ${DOCKER_HUB_REPO}/${MYSQL_IMAGE} ${DOCKER_HUB_REPO}/${MYSQL_IMAGE}:latest'
        //         }
        //     }
        // }
        // stage('Push Images to Docker Hub') {
        //     steps {
        //         script {
        //             docker.withRegistry('https://index.docker.io/v1/', 'DOCKER_HUB_CREDENTIALS') {
        //                 sh 'docker push ${DOCKER_HUB_REPO}/${APP_IMAGE}:latest'
        //                 sh 'docker push ${DOCKER_HUB_REPO}/${MYSQL_IMAGE}:latest'
        //             }
        //         }
        //     }
        // }
        // stage('Deploy to Kubernetes') {
        //     steps {
        //         script {
        //             sh 'kubectl apply -f flask-mysql/k8s'
        //         }
        //     }
        // }
    }
    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub-credentials-id')
        DOCKER_HUB_REPO = 'khueile'
        APP_IMAGE = 'app_image'
        MYSQL_IMAGE = 'mysql_image'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/your-repo/flask-mysql.git'
            }
        }
        stage('Build App Image') {
            steps {
                script {
                    sh 'docker build -t ${DOCKER_HUB_REPO}/${APP_IMAGE} -f dockerfile_app .'
                    sh 'docker tag ${DOCKER_HUB_REPO}/${APP_IMAGE} ${DOCKER_HUB_REPO}/${APP_IMAGE}:latest'
                }
            }
        }
        stage('Build MySQL Image') {
            steps {
                script {
                    sh 'docker build -t ${DOCKER_HUB_REPO}/${MYSQL_IMAGE} -f dockerfile_mysql .'
                    sh 'docker tag ${DOCKER_HUB_REPO}/${MYSQL_IMAGE} ${DOCKER_HUB_REPO}/${MYSQL_IMAGE}:latest'
                }
            }
        }
        stage('Push Images to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'DOCKER_HUB_CREDENTIALS') {
                        sh 'docker push ${DOCKER_HUB_REPO}/${APP_IMAGE}:latest'
                        sh 'docker push ${DOCKER_HUB_REPO}/${MYSQL_IMAGE}:latest'
                    }
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    sh 'kubectl apply -f flask-mysql/k8s'
                }
            }
        }
    }
    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}