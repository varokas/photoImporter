import com.beust.jcommander.Parameter
import com.beust.jcommander.JCommander
import org.apache.commons.io.FileUtils
import com.drew.imaging.ImageMetadataReader
import org.joda.time.DateTime
import com.drew.metadata.iptc.IptcDirectory
import com.drew.metadata.Directory
import com.drew.metadata.Tag
import com.drew.metadata.exif.ExifSubIFDDirectory
import org.joda.time.format.DateTimeFormat

class PhotoImporter {
	private static final ymdFormat = DateTimeFormat.forPattern("yyyyMMdd");

    def main(args) {
		def settings = parseSettingsFromCommandLine(args)
	    def files = getFilesToProcess(settings.inputDirectory)
        files.each( { f -> copyFileToOutputDir(f, new File(settings.outputDirectory)) })
    }

	def parseSettingsFromCommandLine(args) {
        def settings = new PhotoImporterSettings()
        new JCommander(settings, args as String[]);
        return settings
	}

    def getFilesToProcess(String inputDirectory) {
        return FileUtils.listFiles(new File(inputDirectory), ["rw2"] as String[], true)
    }

    def copyFileToOutputDir(File file, File directory) {
        DateTime dt = getDateTimeMetaDataFromFile(file)
        def year = dt.getYear()
        def ymd = ymdFormat.print(dt)

        def dirToCreateString = "${directory}/${year}/${ymd}"
        new File(dirToCreateString).mkdirs()
    }

    private DateTime getDateTimeMetaDataFromFile(File file) {
        def metadata = ImageMetadataReader.readMetadata(file)
        def directory = metadata.getDirectory(ExifSubIFDDirectory.class)
        def date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)
        
        return new DateTime(date)
    }
}

class PhotoImporterSettings {
	@Parameter(names = [ "-i", "-input" ], description = "Input directory to scan")
	String inputDirectory

    @Parameter(names = [ "-o", "-output" ], description = "Output directory")
    String outputDirectory
}
