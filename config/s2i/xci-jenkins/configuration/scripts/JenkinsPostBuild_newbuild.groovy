class JenkinsPostBuild_newbuild{
    static def run(manager){
                def artifactdir = manager.build.getArtifactsDir().getAbsolutePath()
		def jobname = manager.envVars['JOB_NAME']
		def jobnumber = manager.envVars['BUILD_NUMBER']
		
		Properties props = new Properties()
		File propsFile = new File(artifactdir + '/composedata.txt')
		props.load(propsFile.newDataInputStream())
		
		
		def majorver =  props.getProperty('majorver')
		def minorver =  props.getProperty('minorver')
		def variant =  props.getProperty('variant')
		def arch = props.getProperty('arch')
		def vm_image = props.getProperty('image')
		def metal_image = props.getProperty('distro')
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
		"<br>Major Version: JMAJOR" +
		"<br>Minor Version: JMINOR" +
		"<br> Variant: JVARIANT" +
		"<br> Arch: JARCH" +
		"<br> VM_Image: JVMIMAGE" +
		"<br> HW Name: JHWIMAGE" +
		"</div>" +
		"</span>"
		
		if (vm_image == null){
		  vm_image = "not set"
		}
		
		if (metal_image == null){
		  metal_image = "not set"
		}
		
		if (reason == null){
		  reason = ""
		}
		
		script = script.replace("JJN", jobnumber)
		script = script.replace("JMAJOR", majorver)
		script = script.replace("JMINOR", minorver)
		script = script.replace("JVARIANT", variant)
		script = script.replace("JARCH",arch)
		script = script.replace("JVMIMAGE",vm_image)
		script = script.replace("JHWIMAGE",metal_image)
		
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
