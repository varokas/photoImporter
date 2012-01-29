import com.beust.jcommander.Parameter
import com.beust.jcommander.JCommander
import org.apache.commons.io.FileUtils

class PhotoImporter {
	def main(args) {
		def settings = parseSettingsFromCommandLine(args)
	    def files = getFilesToProcess(settings.inputDirectory)
    }

	def parseSettingsFromCommandLine(args) {
        def settings = new PhotoImporterSettings()
        new JCommander(settings, args as String[]);
        return settings
	}

    def getFilesToProcess(inputDirectory) {
        return FileUtils.listFiles(new File(inputDirectory), ["rw2"] as String[], true)
    }
}

class PhotoImporterSettings {
	@Parameter(names = [ "-i", "-input" ], description = "Input directory to scan")
	String inputDirectory

    @Parameter(names = [ "-o", "-output" ], description = "Output directory")
    String outputDirectory
}
