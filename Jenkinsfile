node('maven-appdev') {

	def stashedArtifactName = 'orders-poller-archive'
	def groupId;
	def artifactId;
	def version;
	def devTag;
	def testTag;
	def prodTag;


    stage('Checkout Source') {
      echo 'Checkout Source'
      git url: 'https://github.com/briangallagher/orders-poller.git'
    }

    stage('Set version') {

      groupId    = getGroupIdFromPom("pom.xml")
      artifactId = getArtifactIdFromPom("pom.xml")
      version    = getVersionFromPom("pom.xml")
      // Set the tag for the development image: version + build number
      devTag  = "${version}-${BUILD_NUMBER}"
      testTag  = "${version}-${BUILD_NUMBER}"
      // Set the tag for the production image: version
      prodTag = "${version}"
      echo " Dev TAG: ${devTag}"
      echo " Test TAG: ${testTag}"
      echo " Prod TAG: ${prodTag}"

    }

    stage('Build Source') {
      echo 'Build Source'
      sh "mvn clean package -DskipTests"
	  stash name: stashedArtifactName, includes: "target/*"      
    }

    stage('Unit Test Source') {
      sh "mvn test"
    }    

    stage('Build Image') {
      echo 'Build Image'

      unstash name: stashedArtifactName

      openshift.withCluster() {
        openshift.withProject('arcadia-dev') {

		  openshift.selector("bc/orders-poller").startBuild("--from-archive=orders-poller-"+prodTag+".jar", "--wait")
          openshift.tag("arcadia-dev/orders-poller:latest", "arcadia-dev/orders-poller:" + devTag)
        }
   	  }
    }    

    stage('Deploy Image to Dev') {
      echo 'Deploy Image to Dev'
      deployToEnvironment(project: 'arcadia-dev', env: 'dev', appName: 'orders-poller')
    }

    stage('Integration Testing') {
      echo 'Integration Testing'
    }

    stage('Deploy Image to Test') {
      echo 'Deploy Image to Test'
    }

    stage('UAT Testing') {
      echo 'UAT Testing'
    }

    stage('Deploy Image to Production') {
      echo 'Deploy Image to Production'
    }
}

// Convenience Functions to read variables from the pom.xml. 
// Do not change anything below this line.
def getVersionFromPom(pom) {
  def matcher = readFile(pom) =~ '<version>(.+)</version>'
  matcher ? matcher[0][1] : null
}
def getGroupIdFromPom(pom) {
  def matcher = readFile(pom) =~ '<groupId>(.+)</groupId>'
  matcher ? matcher[0][1] : null
}
def getArtifactIdFromPom(pom) {
  def matcher = readFile(pom) =~ '<artifactId>(.+)</artifactId>'
  matcher ? matcher[0][1] : null
}

def deployToEnvironment(Map params) {
  echo "Deploying application ${params.appName} (project '${params.project}') to ${params.env}"

  def namespace = "${params.project}-${params.env}"

  setConfiguration(appName: params.appName, dcName: params.appName, env: params.env, namespace: namespace)

  openshift.withCluster() {
    openshift.withProject(namespace) {
      echo "Rolling out a new application deployment"
      def dc = openshift.selector( "dc/${params.appName}")
      dc.rollout().latest()
      // Wait for the deployment to complete
      dc.rollout().status()
    }
  }
}

// This module does the following:

// 1. In the root of the source code finds the files `.env.<ENV>`, where
// <ENV> is the name of the environment (e.g. .env.dev, .env.ppte etc.)
//
// 2. Creates a ConfigMap from the file
//
// 3. Applies the ConfigMap to the DeploymentConfig
def call(Map params) {
  echo "Updating envrionment variables ConfigMap in namespace ${params.namespace}"
  openshift.withCluster() {
    openshift.withProject() {
    // openshift.withProject(params.namespace) {

      echo "Setting Configuration"

      // def envvarFile = ".env.${params.env}"
      // def configmapName = "${params.appName}-envvars"
      // def configMapEnv = openshift.selector("configmap/${configmapName}")

      // if (configMapEnv.exists()) {
      //   configMapEnv.delete()
      // }
      // if (fileExists(envvarFile)) {
      //     openshift.create("configmap", configmapName, "--from-env-file=${envvarFile}")
      //     openshift.set("env", "dc/${params.dcName}", "--from=configmap/${configmapName}")
      // } else {
      //     echo "The environment variables file ${envvarFile} doesn't exist"
      // }
    }
  }
}

