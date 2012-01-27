package com.varokas.tools.photoimporter

import spock.lang.*

class PhotoImporterTest extends Specification {
  def photoImporter = new PhotoImporter()

  def "Reads input directory from -i parameter"() {
  	def settings = photoImporter.parseSettingsFromCommandLine(["-i","photos/"]);

  	expect:
  		settings.inputDirectory == "photos/"
  }
}

class Pending extends Specification {

  def "Reads output directory from -o parameter"() {
  }

  def "Scans for every rw2 files recursively in the input directory"() {
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