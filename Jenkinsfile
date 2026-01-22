pipeline {
    agent any

    environment {
        REGISTRY     = "gcr.io/YOUR_PROJECT_ID"
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

        stage('Build Docker Images') {
            steps {
                script {
                    sh """
                      docker build -t $REGISTRY/frontend:$IMAGE_TAG ./frontend
                      docker build -t $REGISTRY/order-service:$IMAGE_TAG ./order-service
                      docker build -t $REGISTRY/inventory-service:$IMAGE_TAG ./inventory-service
                    """
                }
            }
        }

        stage('Authenticate to GCP') {
            steps {
                withCredentials([file(credentialsId: 'gcp-service-account', variable: 'GCP_KEY')]) {
                    sh '''
                    gcloud auth activate-service-account --key-file=$GCP_KEY
                    gcloud config set project YOUR_PROJECT_ID
                    gcloud auth configure-docker --quiet
                    '''
                }
            }
        }


        stage('Push Images') {
            steps {
                script {
                    sh """
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
                    sh """
                      rm -rf k8s-manifests
                      git clone -b $K8S_BRANCH $K8S_REPO_URL
                      
                      cd k8s-manifests

                      sed -i 's|image:.*frontend.*|image: $REGISTRY/frontend:$IMAGE_TAG|' dev/frontend/frontend.yaml
                      sed -i 's|image:.*order-service.*|image: $REGISTRY/order-service:$IMAGE_TAG|' dev/order-service/order.yaml
                      sed -i 's|image:.*inventory-service.*|image: $REGISTRY/inventory-service:$IMAGE_TAG|' dev/inventory-service/inventory.yaml

                      git config user.email "jenkins@ci.com"
                      git config user.name "jenkins"

                      git add .
                      git commit -m "Update images to tag $IMAGE_TAG"
                      git push origin $K8S_BRANCH
                    """
                }
            }
        }
    }
}
