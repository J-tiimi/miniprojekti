#Feature: user can download a bibtex-file which has the saved references in a correct format

#	Scenario: bibtex-file can be downloaded from the system if there are saved references
#		Given there are saved references in the system
#		When user tries to download the references
#		Then correct file is served for the user to download

#	Scenario: bibtex-file cannot be downloaded from the system if there are no saved references
#		Given there are no references in the system
#		When user tries to download the references
#		Then no file is downloaded and the system displays error message
