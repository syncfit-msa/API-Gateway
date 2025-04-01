pipeline {
    agent any

    stages {
        stage('Git clone') {
            steps {
                git branch: 'develop', url: 'https://github.com/syncfit-msa/API-Gateway';
            }
        }

        stage('Project Build') {
            steps {
                sh '''
                    echo build start ~;
                    ./gradlew clean build -Dspring.profiles.active=prod -x test
                ''';
            }
        }

        stage ('AWS Credential') {
            steps {
                withCredentials([[
                $class: 'AmazonWebServicesCredentialsBinding',
                credentialsId: 'aws-access-key']]) {
                    sh 'aws ecr get-login-password --region ap-northeast-3 | docker login --username AWS --password-stdin 268104899906.dkr.ecr.ap-northeast-3.amazonaws.com'
                }
            }
        }

        stage('Docker Image Build') {
            steps {
                sh '''
                    docker build --platform linux/amd64 -t mini2/api-gateway .
                    docker tag mini2/api-gateway:latest 268104899906.dkr.ecr.ap-northeast-3.amazonaws.com/mini2/api-gateway:latest
                '''
            }
        }

        stage("Push to ECR") {
            steps {
                withCredentials([[
                $class: 'AmazonWebServicesCredentialsBinding',
                credentialsId: 'aws-access-key']]) {
                    sh 'docker push 268104899906.dkr.ecr.ap-northeast-3.amazonaws.com/mini2/api-gateway:latest'
                }
            }
        }

        stage('Deploy to EC2') {
            steps {
                sshagent(['ec2-ssh-key']) {
                    sh '''
                        ssh ec2-user@172.31.37.239 '
                            aws ecr get-login-password --region ap-northeast-3 | docker login --username AWS --password-stdin 268104899906.dkr.ecr.ap-northeast-3.amazonaws.com
                            docker pull 268104899906.dkr.ecr.ap-northeast-3.amazonaws.com/mini2/api-gateway:latest

                            docker-compose down
                            docker-compose up -d
                        '
                    '''
                }
            }
        }

        stage('Cleanup') {
            steps {
                sh 'docker rmi mini2/api-gateway:latest || true'
                sh 'docker rmi 268104899906.dkr.ecr.ap-northeast-3.amazonaws.com/mini2/api-gateway:latest || true'
            }
        }
    }
}