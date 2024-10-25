pipeline {
    agent any
    triggers {
        pollSCM('H/2 * * * *') // Poll SCM every 2 minutes
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
        stage('Build') {
            steps {
                echo 'Building...'
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