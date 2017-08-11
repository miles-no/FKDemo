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


                    sh "mvn install:install-file -Dfile=./libs/obsidian-4.1.jar -DgroupId=com.carfey -DartifactId=obsidian -Dversion=4.1 -Dpackaging=jar"
                    sh "mvn install:install-file -Dfile=./libs/de.c-brell.birt.reportitem.barcode.ui_1.0.1.201205022025.jar -DgroupId=de.c-brell.birt.reportitem -DartifactId=barcodeUI -Dversion=1.0.1.201205022025 -Dpackaging=jar"
                    sh "mvn install:install-file -Dfile=./libs/de.c-brell.birt.reportitem.barcode_1.0.1.201205022025.jar -DgroupId=de.c-brell.birt.reportitem -DartifactId=barcode -Dversion=1.0.1.201205022025 -Dpackaging=jar"
                    sh "mvn install:install-file -Dfile=./libs/ubl-0.0.1-SNAPSHOT.jar -DgroupId=oasis-open -DartifactId=ubl -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar"

                    sh "mvn clean install"
                }
                catch(e) {
                    throw e
                }
                stash includes: "target/**/*.jar,ansible/**", name: "built"
            }
        }
    }
}

stage('Deploy to Test') {
    lock("Deploy to Test") {
        node {
            unstash 'built'
            wrap([$class: 'AnsiColorBuildWrapper', colorMapName: "xterm"]) {
                try {
                    ansiblePlaybook(
                        playbook: "ansible/site.yml",
                        inventory: "ansible/test",
                        credentialsId: "miles2-login",
                        colorized: true)
                }
                catch(e) {
                    throw e
                }
            }
        }
    }
}

