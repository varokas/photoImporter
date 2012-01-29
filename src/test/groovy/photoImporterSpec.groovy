import spock.lang.*

class PhotoImporterParametersTest extends Specification {
  def photoImporter = new PhotoImporter()

  def "Reads input directory from -i parameter"() {
  	def settings = photoImporter.parseSettingsFromCommandLine(["-i","photos/"]);

  	expect:
      settings.inputDirectory == "photos/"
  }


  def "Reads output directory from -o parameter"() {
    def settings = photoImporter.parseSettingsFromCommandLine(["-o","output/"]);

    expect:
      settings.outputDirectory == "output/"
  }
}

class PhotoImporterScannerTest extends Specification {
  private final def testDir = "out/testInput"
  private final def testDirectory = new File(testDir)
  def photoImporter = new PhotoImporter()

  def setup() {
    testDirectory.mkdirs()
  }

  def cleanup() {
    testDirectory?.deleteDir()
  }

  private def withFileInDirectoryAndFilename(dir, fileName) {
      def wholeDirectory = new File("${testDir}/${dir}")
      if(!wholeDirectory.exists())
        wholeDirectory.mkdirs()
      
      def file = new File("${testDir}/${dir}/${fileName}")
      file.createNewFile()
      
      return file
  }

  def "Scans for every rw2 files in the input directory"() {
    withFileInDirectoryAndFilename("dirA","fileOne.rw2")
    withFileInDirectoryAndFilename("dirA","fileTwo.rw2")

    def list = photoImporter.getFilesToProcess(testDir)

    expect:
        list.size() == 2
  }
}

class Pending extends Specification {
  def "Scans for every rw2 files recursively in the input directory"() {
  }

  def "Ignores other extensions besides rw2"() {

  }

  def "Creates a year YYYY directory in the destination if we have a photo"() {
  }
  def "Creates a day YYYY/MM/DD directory inside year directory for a photo"() {
  }

  def "A total number of files found in the input path is displayed"() {
  }
  def "A total number of files actually written is displayed"() {
  }
}