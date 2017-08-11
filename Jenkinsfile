stage("Build") {
    milestone()
    lock("Build") {
        node{
            wrap([$class: 'AnsiColorBuildWrapper', colorMapName: "xterm"]) {

                env.JAVA_HOME="${tool 'JDK 1.8'}"
                env.MAVEN_HOME="${tool 'Maven 3.3.9'}"
                env.PATH="${env.MAVEN_HOME}/bin:${env.JAVA_HOME}/bin:${env.PATH}"

                git branch: 'master', credentialsId: 'deploykeys-invoice-manager', url: 'git@github.com:milesfk/invoice_manager.git'
                try {
                    sh "mvn clean install"
                }
                catch(e) {
                    slack ('danger', ":boom: ${env.JOB_BASE_NAME}: Build is broken: ${env.BUILD_URL}")
                    throw e
                }
                stash includes: "target/**/*.jar,ansible/**", name: "built"
            }
        }
    }
}

