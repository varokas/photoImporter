import com.beust.jcommander.Parameter

class PhotoImporter {
	def main(args) {
		settings = parseSettingsFromCommandLine(args)
	}

	def parseSettingsFromCommandLine(args) {
	}
}

class PhotoImporterSettings {
	@Parameter(names = [ "-i", "-input" ], description = "Input directory to scan")
	String inputDirectory
}
