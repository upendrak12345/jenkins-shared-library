// vars/myCustomPipeline.groovy
def call(Map config = [:]) {
    // Fallback default if the user doesn't specify a directory
    def targetDir = config.directory ?: '.'

    pipeline {
        agent any // Runs directly on your Jenkins master pod

        stages {
            stage('System Audit') {
                steps {
                    script {
                        // We call the functions from our other file!
                        linuxUtils.printHeader("Running System Information")
                        
                        sh "uname -a"
                        sh "whoami"
                    }
                }
            }

            stage('Workspace Analysis') {
                steps {
                    script {
                        linuxUtils.printHeader("Analyzing Directory: ${targetDir}")
                        
                        sh "ls -lah ${targetDir}"
                        sh "du -sh ${targetDir}"
                    }
                }
            }

            stage('Tool Validation') {
                steps {
                    script {
                        linuxUtils.printHeader("Checking Required Binaries")
                        
                        // Let's check for standard Linux tools
                        linuxUtils.checkTool('git')
                        linuxUtils.checkTool('curl')
                        linuxUtils.checkTool('python3')
                    }
                }
            }
        }
        
        post {
            always {
                echo "Pipeline finished executing template actions."
            }
        }
    }
}
