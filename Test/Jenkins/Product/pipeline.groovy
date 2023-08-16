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
                dir('Test/product_service') {
                    bat "gradlew build"
                }
            }
        }
        stage('run consumer test') {
            steps {
                dir('Test/product_service/src/test/java/com/example/product/pact/consumer') {
                    bat "gradlew run RetailerContractTests"
                }
            }
        }
        stage('Publish Consumer Pact to Pact Broker') {
            steps {
                dir('Test/product_service/target/pacts') {
                    script {
                def brokerUrl = 'http://localhost:9292/' 
                def pactFile = 'target/pacts/product-retailer.json'
                   
     
                    bat "gradlew pactPublish -PpactBrokerUrl=$brokerUrl -PpactFile=$pactFile"

                }
            }
        }
        }
        stage('Deploy') {
            steps {
                bat 'pact-broker can-i-deploy --pacticipant=Product --broker-base-url=http://localhost:9292/ --latest'
            }
        }
    }
}
