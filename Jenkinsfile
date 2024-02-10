pipeline {
    agent any

    parameters {
        string(name: 'CHEMIN', defaultValue: '/var/lib/jenkins/workspace/FirstTask', description: 'Chemin du répertoire à créer')
    }

    environment {
        GIT_REPO_URL = 'git@github.com:Ferielkraiem2000/Dataset-Generator.git'
        REMOTE_PATH = '/home/vagrant/Dataset-Generator'
    }

    stages {
        stage('Create repository') {
            steps {
                sh "mkdir -p ${params.CHEMIN}"
            }
        }
        stage('Clone or Update Git Repository on Remote Server') {
            steps {
                script {
                    sshagent(credentials: ['SSH_Agent'], keyFileVariable: '/var/lib/jenkins/workspace/FirstTask/id_rsa') {
                        withCredentials([usernamePassword(credentialsId: 'git_token', passwordVariable: 'git_token', usernameVariable: 'Ferielkraiem2000')]) {                      
                            sh """
                                if ssh -o StrictHostKeyChecking=no -i /var/lib/jenkins/workspace/FirstTask/id_rsa vagrant@192.168.1.13 "[ ! -d '${REMOTE_PATH}' ]"; then
                                    ssh -o StrictHostKeyChecking=no -i /var/lib/jenkins/workspace/FirstTask/id_rsa vagrant@192.168.1.13 "git clone -b develop --single-branch ${GIT_REPO_URL}"
                                else
                                    ssh -o StrictHostKeyChecking=no -i /var/lib/jenkins/workspace/FirstTask/id_rsa vagrant@192.168.1.13 "cd ${REMOTE_PATH} && git pull origin develop"
                                fi
                            """
                        }
                    }
                }
            }
        }
    }
}
