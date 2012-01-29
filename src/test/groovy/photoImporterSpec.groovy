import spock.lang.*
import org.apache.commons.io.FileUtils
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.CopyOption
import java.nio.file.attribute.BasicFileAttributes

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
    private final def filePathStr = "src/test/resources/images/${filename}"

    File file = new File(filePathStr)
    File outDir = new File(testDir)

    def photoImporter = new PhotoImporter()

    def setup() {
        testDirectory.mkdirs()
    }

    def cleanup() {
        testDirectory?.deleteDir()
    }

    def "Creates a year YYYY directory in the destination if we have a photo"() {
        photoImporter.copyFileToOutputDir(file, outDir)

        def f = new File("${testDir}/2012")
        expect:
            f.exists()
            f.isDirectory()
    }

    def "Creates a day YYYYMMDD directory inside year directory for a photo"() {
        photoImporter.copyFileToOutputDir(file, outDir)

        def f = new File("${testDir}/2012/20120128")
        expect:
            f.exists()
            f.isDirectory()
    }

    def "RAW files copied to YYYY/YYYYMMDD directory"() {
        photoImporter.copyFileToOutputDir(file, outDir)

        def f = new File("${testDir}/2012/20120128/${filename}")
        expect:
            f.exists()
            f.isFile()
    }

    def "Skips if the photo already exists on destination dir"() {
        //Needs Java 7 NIO 2 just to be able to get the createdTime !!!
        def destPathString = "${testDir}/2012/20120128/${filename}"

        def srcPath = Paths.get(filePathStr)
        def destPath = Paths.get(destPathString)
        
        Files.createDirectories(Paths.get("${testDir}/2012/20120128"))
        Files.copy(srcPath, destPath)

        def attr = Files.readAttributes(destPath, BasicFileAttributes.class)
        def originalCreationTime = attr.creationTime()

        photoImporter.copyFileToOutputDir(file, outDir)

        def attrAfter = Files.readAttributes(destPath, BasicFileAttributes.class)

        expect:
            attrAfter.creationTime() == originalCreationTime
    }
}

class Pending extends Specification {



  def "A total number of files found in the input path is displayed"() {
  }
  def "A total number of files actually written is displayed"() {
  }
}