pipeline {
    agent any

    parameters {
        choice(
                name: 'OS',
                choices: ['linux', 'darwin', 'windows'],
                description: 'Target operating system'
        )
        choice(
                name: 'ARCH',
                choices: ['amd64', 'arm64'],
                description: 'Target architecture'
        )
        booleanParam(
                name: 'SKIP_TESTS',
                defaultValue: false,
                description: 'Skip running tests'
        )
        booleanParam(
                name: 'SKIP_LINT',
                defaultValue: false,
                description: 'Skip running linter'
        )
    }
    environment {
        TARGETOS = "${env.OS}"
        TARGETARCH= "${ARCH}"
    }
    stages {
        stage("test") {
            when {
                expression { return !params.SKIP_TESTS }
            }
            steps {
                sh "make test"
            }

        }
        stage("lint") {
            when {
                expression { return !params.SKIP_LINT }
            }
            steps {
                sh "make lint"
            }
        }
        stage("build") {
            steps { sh "make lint" }
        }

    }
}