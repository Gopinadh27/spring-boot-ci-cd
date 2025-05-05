pipeline {
    agent any

    environment {
        IMAGE_NAME = 'spring-boot-ci-cd'
        DOCKER_REGISTRY = 'docker.io/ctli'
        CONTAINER_NAME='user_service'
        TAG = "${env.BUILD_NUMBER}"
        SONAR_URL = 'http://sonarqube:9000'
    }

    stages {
        stage('Clean Workspace') {
            steps {
                echo "Cleaning workspace..."
                deleteDir()
            }
        }

        stage('Clone Repository') {
            steps {
                echo "Cloning repository..."
                sh "git clone https://github.com/Gopinadh27/spring-boot-ci-cd.git"
            }
        }


        stage('Build') {
            steps {
                echo 'Building Spring Boot application...'
                sh '''
                    cd spring-boot-ci-cd
                    mvn clean package -DskipTests
                '''
            }
        }

        stage('Static Code Analysis') {
            steps {
                withCredentials([string(credentialsId:'sonarqube', variable: 'SONAR_AUTH_TOKEN')]) {
                    echo "Present Working Directory"
                    sh 'pwd'
                    sh 'cd spring-boot-ci-cd && mvn sonar:sonar -Dsonar.login=${SONAR_AUTH_TOKEN} -Dsonar.host.url=${SONAR_URL}'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "Building Docker image: ${IMAGE_NAME} with tag ${TAG}"
                sh "ls"
                sh "pwd"
                sh "cd spring-boot-ci-cd && docker build -t ${DOCKER_REGISTRY}/${IMAGE_NAME}:${TAG} ."
            }
        }

        stage('Push Docker Image') {
            steps {
                echo 'Pushing Docker image to registry...'
                withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                sh '''echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'''
                sh '''echo Successfully logged into docker registry...'''
                sh "docker push ${DOCKER_REGISTRY}/${IMAGE_NAME}:${TAG}"
                sh '''echo image pushed to docker registry successfully...'''
                }
            }
        }

        stage ('Update Deployment file') {
            environment {
                GIT_REPO_NAME = 'spring-boot-ci-cd'
                GIT_USER_NAME='Gopinadh27'
            }
            steps {
                withCredentials([string(credentialsId:'github', variable:'GIT_HUB_TOKEN')]) {
                    sh '''
                       pwd
                       cd spring-boot-ci-cd
                       git config user.email "gopinadh@git.com"
                       git config user.name  "gopinadh"
                       sed -i "s|replaceImageTag|${DOCKER_REGISTRY}/${IMAGE_NAME}:${TAG}|g" deployment.yml
                       git add deployment.yml
                       git commit -m "Update deployment image to version ${BUILD_NUMBER}"
                       git push https://${GIT_HUB_TOKEN}@github.com/${GIT_USER_NAME}/${GIT_REPO_NAME} HEAD:main
                       echo "deployment file updated successfully"
                    '''
                }
            }
        }

        stage('Cleanup') {
            steps {
                echo 'Cleaning up Docker image...'
                sh "docker rmi ${DOCKER_REGISTRY}/${IMAGE_NAME}:${TAG} || true"
            }
        }

        stage {
            steps {
                withCredentials(file(credentialsId: 'ec2_pem', variable:'EC2_PEM'),
                                string(credentialsId: 'ec2_user', variable: 'EC2_USER'),
                                string(credentialsId: 'ec2_host', variable: 'EC2_HOST'),
                                usernamePassword(credentialsId: 'docker-hub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')
                                ) {
                sh '''echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'''
                sh '''echo Successfully logged into docker registry...'''
                    sh """
                    ssh -o StrictHostKeyChecking=no -i ${EC2_PEM} ${EC2_USER}@${EC2_HOST} << 'EOF'
                        docker stop ${CONTAINER_NAME} || true
                        docker rm ${CONTAINER_NAME} || true
                        docker pull ${DOCKER_REGISTRY}/${IMAGE_NAME}:${TAG}
                        docker run -dit --name ${CONTAINER_NAME} -p 2000:2000 ${DOCKER_REGISTRY}/${IMAGE_NAME}:${TAG}
                    EOF
                    """
            }
        }
    }

    post {
        stages {
            steps {

            }
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
