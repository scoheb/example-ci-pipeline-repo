import java.util.logging.Logger
import jenkins.security.s2m.*
import jenkins.model.*;
import com.redhat.jenkins.plugins.ci.*
import com.redhat.jenkins.plugins.ci.messaging.*
import hudson.security.csrf.DefaultCrumbIssuer
import jenkins.plugins.git.GitSCMSource
import org.jenkinsci.plugins.workflow.libs.*
import jenkins.model.GlobalConfiguration
import org.jenkinsci.plugins.pipeline.modeldefinition.config.GlobalConfig
import hudson.node_monitors.*


def logger = Logger.getLogger("")

logger.info("Disabling CLI over remoting")
jenkins.CLI.get().setEnabled(false);

logger.info("Enable Slave -> Master Access Control")
Jenkins.instance.injector.getInstance(AdminWhitelistRule.class).setMasterKillSwitch(false);
Jenkins.instance.save()

logger.info("CSRF Protection")
Jenkins.instance.setCrumbIssuer(new DefaultCrumbIssuer(true))
Jenkins.instance.save()

logger.info("Fedmsg Configuration")
FedMsgMessagingProvider fedmsg = new FedMsgMessagingProvider("fedmsg", "tcp://hub.fedoraproject.org:9940", "tcp://hub.fedoraproject.org:9940", "org.centos");
GlobalCIConfiguration.get().addMessageProvider(fedmsg)

logger.info("Protocols deprecated")
Set<String> agentProtocolsList = ['JNLP4-connect']
Jenkins.instance.getAgentProtocols().equals(agentProtocolsList)
Jenkins.instance.setAgentProtocols(agentProtocolsList)
Jenkins.instance.save()


logger.info("Global Pipeline Libraries")
List<LibraryConfiguration> libs = []
['global_utilities'].each {
  GitSCMSource source= new GitSCMSource(it, "https://gitlab.cee.redhat.com/Feuerkasten/xCI-shared-libraries.git",
                                       null, null, null, false)
  LibraryConfiguration lib = new LibraryConfiguration(it, new SCMSourceRetriever(source))
  lib.implicit = false
  lib.defaultVersion = 'master'
  libs.add (lib)
}
GlobalLibraries.get().libraries = libs

logger.info("Preventive Node Monitoring")
ComputerSet.getMonitors().replace(new DiskSpaceMonitor("200MB"))
ComputerSet.getMonitors().replace(new TemporarySpaceMonitor("200MB"))

