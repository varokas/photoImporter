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
      list.contains(new File("${testDir}/dirA/fileOne.rw2"))
      list.contains(new File("${testDir}/dirA/fileTwo.rw2"))
  }

  def "Scans for every rw2 files recursively in the input directory"() {
      withFileInDirectoryAndFilename("dirA","fileOne.rw2")
      withFileInDirectoryAndFilename("dirA/innerDirA","fileTwo.rw2")

      def list = photoImporter.getFilesToProcess(testDir)

      expect:
      list.size() == 2
      list.contains(new File("${testDir}/dirA/fileOne.rw2"))
      list.contains(new File("${testDir}/dirA/innerDirA/fileTwo.rw2"))
  }

  def "Ignores other extensions that is not rw2"() {
      withFileInDirectoryAndFilename("dirA","fileOne.jpg")

      def list = photoImporter.getFilesToProcess(testDir)

      expect:
      list.size() == 0
  }
}

class FileCopyTest extends  Specification {
    private final def testDir = "out/testOutput"
    private final def testDirectory = new File(testDir)
    private final def filename = "file-20120128.RW2"
    def photoImporter = new PhotoImporter()

    def setup() {
        testDirectory.mkdirs()
    }

    def cleanup() {
        testDirectory?.deleteDir()
    }

    def "Creates a year YYYY directory in the destination if we have a photo"() {
        File file = new File("test/resources/images/${filename}")
        File outDir = new File(testDir)
        
        photoImporter.copyFileToOutputDir(file, outDir)

        def f = new File("${testDir}/2011")
        expect:
            f.exists() == true
            f.isDirectory() == true
    }
}

class Pending extends Specification {
  def "Creates output dir if it does not already exists"() {
  }


  def "Creates a day YYYY/MM/DD directory inside year directory for a photo"() {
  }

  def "A total number of files found in the input path is displayed"() {
  }
  def "A total number of files actually written is displayed"() {
  }
}