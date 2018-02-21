class JenkinsPostBuild_nvr{
    static def run(manager){
                def artifactdir = manager.build.getArtifactsDir().getAbsolutePath()
		def jobname = manager.envVars['JOB_NAME']
		def jobnumber = manager.envVars['BUILD_NUMBER']
		
		Properties props = new Properties()
                File propsFile = new File(artifactdir + '/brew.txt')
		props.load(propsFile.newDataInputStream())
		
		
		def aut =  props.getProperty('nvr')
		def arch =  props.getProperty('arches')
		def tags =  props.getProperty('taglist')
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
		"<br>NVR: JNVR" +
		"<br>Taglist: JTAGS" +
		"<br> Arches: JARCHES" +
		"</div>" +
		"</span>"
		
		if (aut == null){
		  aut = "not set"
		}
		
		if (arch == null){
		  arch = "not set"
		}
		
		if (tags == null){
		  tags = "not set"
		}
		
		if (reason == null){
		  reason = "not set"
		}
		
		script = script.replace("JJN", jobnumber)
		script = script.replace("JNVR", aut)
		script = script.replace("JTAGS",tags)
		script = script.replace("JARCHES",arch)
		
		if (reason != "not set"){
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

