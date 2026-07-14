#!groovy

@Library('cib-pipeline-library') _

import de.cib.pipeline.library.Constants
import de.cib.pipeline.library.kubernetes.BuildPodCreator
import de.cib.pipeline.library.ConstantsInternal
import de.cib.pipeline.library.MavenProjectInformation
import groovy.transform.Field

// This pipeline packages the Helm chart under helm/cibseven-mcp-restapi and
// pushes it to the CIB Harbor OCI registry (harbor.cib.de). The Docker image
// the chart references (cibseven/cibseven-mcp-restapi) is built and published
// separately via the GitHub Actions workflow (.github/workflows/build-and-publish.yml);
// this pipeline only ships the chart.

@Field MavenProjectInformation mavenProjectInformation = null
@Field Map pipelineParams = [
    pom: ConstantsInternal.DEFAULT_MAVEN_POM_PATH,
    mvnContainerName: Constants.MAVEN_JDK_17_CONTAINER,
    // Branch that carries the MCP getting-started sources and the Helm chart,
    // and from which the chart is published automatically (SNAPSHOT versions).
    primaryBranch: 'cibseven-mcp-servers-helm-chart',
    // Path of the Helm chart to publish to Harbor.
    helmChartPath: 'helm/cibseven-mcp-restapi',
    uiParamPresets: [:],
    testMode: false
]

pipeline {
    agent {
        kubernetes {
            yaml BuildPodCreator.cibStandardPod()
                    .withContainerFromName(pipelineParams.mvnContainerName)
                    .withHelm4Container()
                    .asYaml()
            defaultContainer pipelineParams.mvnContainerName
        }
    }

    // Parameter that can be changed in the Jenkins UI
    parameters {
        booleanParam(
            name: 'DEPLOY_ANY_BRANCH_TO_HARBOR',
            defaultValue: false,
            description: '└─ 🚀 Deploy the Helm chart to harbor.cib.de from any branch'
        )
    }

    options {
        buildDiscarder(
            logRotator(
                // number of build logs to keep
                numToKeepStr:'5',
                // history to keep in days
                daysToKeepStr: '15',
                // artifacts are kept for days
                artifactDaysToKeepStr: '15',
                // number of builds have their artifacts kept
                artifactNumToKeepStr: '5'
            )
        )
        // Stop build after 60 minutes
        timeout(time: 60, unit: 'MINUTES')
        disableConcurrentBuilds()
    }

    stages {
        stage('Print Settings & Checkout') {
            steps {
                script {
                    printSettings()

                    def pom = readMavenPom file: pipelineParams.pom

                    // for overlays often no groupId is set as the parent groupId is used
                    def groupId = pom.groupId
                    if (groupId == null) {
                        groupId = pom.parent.groupId
                        echo "parent groupId is used"
                    }

                    mavenProjectInformation = new MavenProjectInformation(groupId, pom.artifactId, pom.version, pom.name, pom.description)

                    echo "Build Project: ${mavenProjectInformation.groupId}:${mavenProjectInformation.artifactId}, ${mavenProjectInformation.name} with version ${mavenProjectInformation.version}"

                    // Avoid Git "dubious ownership" error in checked out repository. Needed in
                    // build containers with newer Git versions. Originates from Jenkins running
                    // pipeline as root but repository being owned by user 1000. For more, see
                    // https://stackoverflow.com/questions/72978485/git-submodule-update-failed-with-fatal-detected-dubious-ownership-in-repositor
                    sh "git config --global --add safe.directory \$(pwd)"
                }
            }
        }

        stage('Deploy Helm Chart to Harbor') {
            when {
                anyOf {
                    allOf {
                        branch pipelineParams.primaryBranch
                        expression { mavenProjectInformation.version.endsWith("-SNAPSHOT") == true }
                    }
                    expression { params.DEPLOY_ANY_BRANCH_TO_HARBOR }
                }
            }
            steps {
                // Chart and app version track the Maven project version, so the
                // published chart pulls cibseven/cibseven-mcp-restapi:<version>.
                deployHelmChart(
                    path: pipelineParams.helmChartPath,
                    version: mavenProjectInformation.version,
                    appVersion: mavenProjectInformation.version
                )
            }
        }
    }

    post {
        always {
            script {
                echo 'End of the build'
            }
        }

        success {
            script {
                echo '✅ Build successful'
            }
        }

        unstable {
            script {
                echo '⚠️ Build unstable'
            }
        }

        failure {
            script {
                echo '❌ Build failed'
            }
        }

        fixed {
            script {
                echo '✅ Previous issues fixed'
            }
        }
    }
}
