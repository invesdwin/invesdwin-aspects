pipeline {
  agent any
  stages {
    stage('Build and test') {
      withMaven {
           sh 'mvn clean install -f invesdwin-aspects/pom.xml -T4'
      }
    }
  }
}