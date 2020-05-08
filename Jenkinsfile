pipeline {
    agent any
    tools {
        maven 'Maven-3.6.3'
        jdk 'JDK8'
    }
    stages {
        stage ('Build and Deploy') {
            steps {
            configFileProvider([configFile(fileId: 'Maven_settings.xml', variable: 'MAVEN_GLOBAL_SETTINGS')]){
                            sh 'mvn -gs $MAVEN_GLOBAL_SETTINGS clean package deploy'
                            }
            }
        }
    }
}