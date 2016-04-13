node {
   // Mark the code checkout 'stage'....
   stage 'Checkout'

   // Checkout code from repository
   checkout scm

   // Get the maven tool.
   // ** NOTE: This 'M3' maven tool must be configured
   // **       in the global configuration.
   def mvnHome = tool 'M3'

   // Mark the code build 'stage'....
   stage 'Build'
   // Run the maven build
   sh "${mvnHome}/bin/mvn -Dmaven.test.failure.ignore clean install"
   
   stage 'Archive'
   step([$class: 'ArtifactArchiver', artifacts: '**/target/*.jar', fingerprint: true])
   //step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
   
   stage 'Trigger Downstream'
   // Trigger downstream projects.
   build job: '../can-i-kayak-baltimore/jenkins', wait: false
}
