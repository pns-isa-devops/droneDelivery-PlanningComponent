pipeline {
    agent any
    tools {
        maven 'Maven-3.6.3'
        jdk 'JDK8'
    }
    stages {
        stage ('Build and Deploy') {
            steps {
                sh 'mvn clean package deploy'
            }
        }
    }
}