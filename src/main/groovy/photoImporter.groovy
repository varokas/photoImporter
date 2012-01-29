import com.beust.jcommander.Parameter
import com.beust.jcommander.JCommander

class PhotoImporter {
	def main(args) {
		settings = parseSettingsFromCommandLine(args)
	}

	def parseSettingsFromCommandLine(args) {
        def settings = new PhotoImporterSettings()
        new JCommander(settings, args as String[]);
        return settings
	}
}

class PhotoImporterSettings {
	@Parameter(names = [ "-i", "-input" ], description = "Input directory to scan")
	String inputDirectory
}
