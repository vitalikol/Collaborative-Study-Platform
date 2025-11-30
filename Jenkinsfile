pipeline {
    agent any

    tools {
        maven 'maven-3.9.11'
    }

    environment {
        DOCKER_IMAGE = "csp:latest"
    }

    stages {
        stage('Debug') {
            steps {
                echo "Jenkinsfile loaded from repo"
                sh 'pwd'
                sh 'ls -R'
            }
        }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/vitalikol/Collaborative-Study-Platform.git',
                    branch: 'main',
                    credentialsId: 'github-token'
            }
        }

        stage('Build Maven') {
            steps {
                dir('server') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Docker Build') {
            steps {
                dir('server') {
                    sh 'docker build -t $DOCKER_IMAGE .'
                }
            }
        }

        stage('Restart Container') {
            steps {
                sh '''
                docker stop csp || true
                docker rm csp || true

                docker run -d \
                    --name csp \
                    -p 8080:8080 \
                    -v csp_data:/app/data \
                    csp:latest
                '''
            }
        }
    }
}
