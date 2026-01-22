pipeline {
    agent any

    environment {
        REGISTRY     = "us-east1-docker.pkg.dev/raviteja-demo/demo-app"
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

        stage('Login to Docker Hub (base images only)') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'Docker-hub',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    bat 'docker login -u %DOCKER_USER% -p %DOCKER_PASS%'
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                bat '''
                docker build -t %REGISTRY%/frontend:%IMAGE_TAG% frontend
                docker build -t %REGISTRY%/order-service:%IMAGE_TAG% order-service
                docker build -t %REGISTRY%/inventory-service:%IMAGE_TAG% inventory-service
                '''
            }
        }

        stage('Authenticate to GCP') {
            steps {
                withCredentials([file(credentialsId: 'gcp-service-account', variable: 'GCP_KEY')]) {
                    bat '''
                    gcloud auth activate-service-account --key-file=%GCP_KEY%
                    gcloud config set project raviteja-demo
                    '''
                }
            }
        }

        stage('Configure Docker for Artifact Registry') {
            steps {
                bat '''
                gcloud auth configure-docker us-east1-docker.pkg.dev --quiet
                '''
            }
        }

        stage('Force Docker Credential Helper (SYSTEM SAFE)') {
            steps {
                bat '''
                mkdir C:\\Windows\\System32\\config\\systemprofile\\.docker 2>NUL

                (
                echo {
                echo   "credHelpers": {
                echo     "us-east1-docker.pkg.dev": "gcloud"
                echo   }
                echo }
                ) > C:\\Windows\\System32\\config\\systemprofile\\.docker\\config.json

                exit /b 0
                '''
            }
        }

        stage('Verify Docker Auth') {
            steps {
                bat 'type C:\\Windows\\System32\\config\\systemprofile\\.docker\\config.json'
            }
        }

        stage('Push Images to Artifact Registry') {
            steps {
                bat '''
                docker push %REGISTRY%/frontend:%IMAGE_TAG%
                docker push %REGISTRY%/order-service:%IMAGE_TAG%
                docker push %REGISTRY%/inventory-service:%IMAGE_TAG%
                '''
            }
        }

        stage('Update Kubernetes Manifests Repo') {
            steps {
                bat '''
                rmdir /s /q microservices-k8s-manifests
                git clone -b %K8S_BRANCH% %K8S_REPO_URL%

                cd microservices-k8s-manifests

                powershell -Command "(Get-Content dev/frontend/frontend.yaml) -replace 'image:.*', 'image: %REGISTRY%/frontend:%IMAGE_TAG%' | Set-Content dev/frontend/frontend.yaml"
                powershell -Command "(Get-Content dev/order-service/order.yaml) -replace 'image:.*', 'image: %REGISTRY%/order-service:%IMAGE_TAG%' | Set-Content dev/order-service/order.yaml"
                powershell -Command "(Get-Content dev/inventory-service/inventory.yaml) -replace 'image:.*', 'image: %REGISTRY%/inventory-service:%IMAGE_TAG%' | Set-Content dev/inventory-service/inventory.yaml"

                git config user.email "jenkins@ci.com"
                git config user.name "jenkins"

                git add .
                git commit -m "Update images to tag %IMAGE_TAG%"
                git push -u origin %K8S_BRANCH%
                '''
            }
        }
    }
}
