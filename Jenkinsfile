stage("Build") {
    milestone()
    lock("Build") {
        node{
            wrap([$class: 'AnsiColorBuildWrapper', colorMapName: "xterm"]) {

                env.JAVA_HOME="${tool 'JDK 1.8'}"
                env.MAVEN_HOME="${tool 'Maven 3.3.9'}"
                env.PATH="${env.MAVEN_HOME}/bin:${env.JAVA_HOME}/bin:${env.PATH}"

                git branch: 'Multi-Module-Version1.0', credentialsId: 'deploykeys-invoice-manager', url: 'git@github.com:milesfk/invoice_manager.git'
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
                stash includes: "PDFGenerator/target/*.jar,PDFGenerator/target/*.zip,Preprocessor/target/*.jar, InvoiceManagerUI/target/*.jar,ansible/**,ansible/**/**", name: "built"
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
                        playbook: "ansible/manage_all.yml",
                        inventory: "ansible/test",
                        credentialsId: "miles2-login",
                        extras: "--vault-password-file ~/ansible-vault/mobile.txt",
                        colorized: true)
                }
                catch(e) {
                    throw e
                }
            }
        }
    }
}


stage('Promote to production?') {
    timeout(time:1, unit:'DAYS') {
        milestone()
        def inputResponse = input(message: 'Promote to production?', submitter: "bhavik,prashant", submitterParameter: 'approver', parameters:[booleanParam(defaultValue: true, description: 'I understand that this will update PRODUCTION', name: 'Confirmation')] )
        if(!inputResponse['Confirmation']) {
            error("You need to confirm that you want to deploy to production")
        }
        env.APPROVER = inputResponse['approver']
        milestone()
    }
}


stage('Deploy to production') {
    lock("Deploy to prod") {
        node {
            unstash 'built'
            wrap([$class: 'AnsiColorBuildWrapper', colorMapName: "xterm"]) {
                try {
                    ansiblePlaybook(
                        playbook: "ansible/manage_all_prod.yml",
                        inventory: "ansible/prod",
                        credentialsId: "bad1dbcc-9d24-4922-a66e-8be042ab725b",
                        extras: "--vault-password-file ~/ansible-vault/mobile.txt",
                        colorized: true)
                }
                catch(e) {
                    throw e
                }
            }
        }
    }
}