
import jenkins.model.*
import org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition
import hudson.plugins.git.*
import hudson.security.*
import jenkins.model.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.impl.*
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*


def instance = Jenkins.getInstance()

// Create admin user
def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount('admin', 'admin')
instance.setSecurityRealm(hudsonRealm)

def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
instance.setAuthorizationStrategy(strategy)
instance.save()

// Set up the pipeline job
def jenkins = Jenkins.instance
def jobName = "flask-mysql-pipeline"

// Check if the job already exists
def job = jenkins.getItem(jobName)
if (job != null) {
    println "Job $jobName already exists. Deleting the existing job."
    job.delete()
}

println "Creating new job: $jobName"

// Create a new pipeline job
def pipelineJob = jenkins.createProject(org.jenkinsci.plugins.workflow.job.WorkflowJob, jobName)

// Define the pipeline script from SCM
def scm = new GitSCM("https://github.com/khueile/flask-mysql.git")
scm.branches = [new BranchSpec("*/main")]
def scmDefinition = new CpsScmFlowDefinition(scm, "Jenkinsfile")

pipelineJob.definition = scmDefinition

// Set up triggers
pipelineJob.addTrigger(new hudson.triggers.SCMTrigger("* * * * *"))
pipelineJob.save()

println "Job $jobName created successfully."


def privateKey = new File('/var/jenkins_home/init.groovy.d/id_rsa_github_personal').text
def githubCredentials = new BasicSSHUserPrivateKey(
    CredentialsScope.GLOBAL,
    'id_rsa_github_personal',  // Credential ID
    'khueile',         // Username
    new BasicSSHUserPrivateKey.DirectEntryPrivateKeySource(privateKey),
    '',                      // Passphrase (if any)
    'SSH Key for Deployment' // Description
)

def pass = new File('/var/jenkins_home/init.groovy.d/pass').text.trim()
def dockerhubCredentials = new UsernamePasswordCredentialsImpl(
    CredentialsScope.GLOBAL,
    "docker_hub_credentials_id", // ID
    "user + pass for ", // Description
    "le_k1@denison.edu", // Username
    pass // Password
)

def domain = Domain.global()
def store = instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()
store.addCredentials(domain, githubCredentials)
store.addCredentials(domain, dockerhubCredentials)