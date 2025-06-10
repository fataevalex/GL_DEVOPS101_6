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
            steps {
                if (SKIP_TESTS) {sh "make test"}
            }

        }
        stage("lint") {
            steps {
                if (SKIP_LINT) {sh "make lint"}
            }

        }
        stage("build") {
            steps {sh "make lint"}
        }

    }
}