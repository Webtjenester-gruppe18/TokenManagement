pipeline {
  agent any
  stages {
    stage('Test') {
      steps {
        sh 'mvn clean install'
      }
    }
     stage('Build') {
          steps {
            sh 'docker build --tag tokenmanagerimage:latest . '
          }
        }
     stage('Deploy') {
          steps {
            sh '''docker stop tokenmanager || true && docker rm tokenmanager || true;
            docker run -d -p 7373:8080 --name tokenmanager tokenmanagerimage:latest
            '''
          }
     }
  }
}