node('MASTER') {
    stage('scm') {
       git 'https://github.com/manjulaaporeddy/openmrs-core.git'
    }
    stage('build'){
        sh 'mvn package'
    }
    stage('postbuild') {
       archive '**/*.war'
       junit '**/TEST-*.xml'
    }
}