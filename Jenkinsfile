pipeline {
  agent any
  stages {
    stage('Build and test') {
      steps {
        sh 'mvn clean install -f invesdwin-aspects/pom.xml surefire-report:report -T4'
      }
    }
  }
  post {
    always {
      junit '**/invesdwin-aspects/target/surefire-reports/*.xml'
    }
  }
}