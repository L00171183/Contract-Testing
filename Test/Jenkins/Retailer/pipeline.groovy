pipeline {
    agent any
    stages {
        stage('checkout') {
            steps {
                bat "git clone https://github.com/L00171183/Contract-Testing.git"
            }
        }
        stage('build') {
            steps {
                dir('Test/retailer') {
                    bat "gradlew build"
                }
            }
        }
        stage('run provider verification test') {
            steps {
                dir('Test/retailer/src/test/java/com/example/retailer/pacts/provider') {
                    bat "gradlew run ProductPactTests"
                }
            }
        }
        
    }
}
