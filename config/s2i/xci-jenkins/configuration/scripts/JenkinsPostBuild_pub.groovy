class JenkinsPostBuild_pub{
    static def run(manager){
                def artifactdir = manager.build.getArtifactsDir().getAbsolutePath()
		def jobname = manager.envVars['JOB_NAME']
		def jobnumber = manager.envVars['BUILD_NUMBER']
		
		Properties props = new Properties()
		File propsFile = new File(artifactdir + '/pubdata.txt')
		props.load(propsFile.newDataInputStream())

		def task_id =  props.getProperty('task_id')
		def push_id =  props.getProperty('push_id')
		def target =  props.getProperty('target')
		def reason = props.getProperty('reason')
		def trigger = manager.envVars['BUILD_CAUSE']
		
		def script = "<script type='text/javascript'>" +
		    "jQuery(document).ready(function( \$ ) { " +
		    "\$('#infoJJN').hide();" +
		    "\$( '#buildJJN' ).mouseenter(function() {" +
		    "    \$('#infoJJN').show();" +
		    "});" +
		    "\$( '#buildJJN' ).mouseleave(function() {" +
		    "    \$('#infoJJN').hide();" +
		    "});" + 
		"});" +
		"</script>" +
		"<span id='buildJJN'>" +
		"<span style='color:JCOLOR'>JTEXT</span>" + 
		" <b>more details</b>" +
		" <div id='infoJJN' style='padding:1px;border:1px solid #C0C080;margin:0px;background:beige;color:black'>" +
		" Reason: JTEXT" +
		"<br>Task ID: JTASKID" +
		"<br>Push ID: JPUSHID" +
		"<br> Target: JTARGET" +
		"</div>" +
		"</span>"
		
		if (reason == null){
		  reason = ""
		}
		
		script = script.replace("JJN", jobnumber)
		script = script.replace("JTASKID", task_id)
		script = script.replace("JPUSHID", push_id)
		script = script.replace("JTARGET", target)
		
		if (reason){
		    script = script.replace("JTEXT", reason)
		    script = script.replace("JCOLOR", "red")
		}    
		else{
		    script = script.replace("JTEXT", "successfully triggered")
		    script = script.replace("JCOLOR","green")
		}
		
		manager.addShortText(script, "black", "white", "0px", "white")
	}
}
