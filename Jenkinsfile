pipeline {
    agent any

    environment {
        REGISTRY = "us-east1-docker.pkg.dev/raviteja-demo/demo-app"
        IMAGE_TAG    = "${BUILD_NUMBER}"
        K8S_REPO_URL = "https://github.com/Ravitejaganji1234/microservices-k8s-manifests.git"
        K8S_BRANCH   = "main"
    }

    stages {

        stage('Checkout Application Repo') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Ravitejaganji1234/devops-microservices-demo.git'
            }
        }

        stage('Login to Docker Hub') {
    steps {
        withCredentials([usernamePassword(
            credentialsId: 'Docker-hub',
            usernameVariable: 'DOCKER_USER',
            passwordVariable: 'DOCKER_PASS'
        )]) {
            bat """
            docker login -u %DOCKER_USER% -p %DOCKER_PASS%
            """
        }
    }
}


        stage('Build Docker Images') {
            steps {
                script {
                    bat """
                      docker build -t $REGISTRY/frontend:$IMAGE_TAG frontend
                      docker build -t $REGISTRY/order-service:$IMAGE_TAG order-service
                      docker build -t $REGISTRY/inventory-service:$IMAGE_TAG inventory-service
                    """
                }
            }
        }

        stage('Authenticate to GCP') {
            steps {
                withCredentials([file(credentialsId: 'gcp-service-account', variable: 'GCP_KEY')]) {
                    bat '''
                    gcloud auth activate-service-account --key-file=%GCP_KEY%
                    gcloud config set project raviteja-demo
                    gcloud auth configure-docker us-east1-docker.pkg.dev --quiet
                    '''
                }
            }
        }


        stage('Push Images') {
            steps {
                script {
                    bat """
                      docker push $REGISTRY/frontend:$IMAGE_TAG
                      docker push $REGISTRY/order-service:$IMAGE_TAG
                      docker push $REGISTRY/inventory-service:$IMAGE_TAG
                    """
                }
            }
        }

        stage('Update Kubernetes Manifests Repo') {
            steps {
                script {
                    bat """
                rmdir /s /q k8s-manifests
                git clone -b %K8S_BRANCH% %K8S_REPO_URL%

                cd k8s-manifests

                powershell -Command "(Get-Content dev/frontend/frontend.yaml) -replace 'image:.*', 'image: %REGISTRY%/frontend:%IMAGE_TAG%' | Set-Content dev/frontend/frontend.yaml"
                powershell -Command "(Get-Content dev/order-service/order.yaml) -replace 'image:.*', 'image: %REGISTRY%/order-service:%IMAGE_TAG%' | Set-Content dev/order-service/order.yaml"
                powershell -Command "(Get-Content dev/inventory-service/inventory.yaml) -replace 'image:.*', 'image: %REGISTRY%/inventory-service:%IMAGE_TAG%' | Set-Content dev/inventory-service/inventory.yaml"

                git config user.email "jenkins@ci.com"
                git config user.name "jenkins"

                git add .
                git commit -m "Update images to tag %IMAGE_TAG%"
                git push origin %K8S_BRANCH%
                """
                }
            }
        }
    }
}
