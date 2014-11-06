public class csvDriver {
	public static void main(String[] args){
		def test = new ActAsCSV() //makes a ActAsCSV with the supplied .csv
		test.parse('test.csv')
		//A little test to print out all "FirstCol" items in test.csv
		test.each({println it.FirstCol})

	}
}

//Class to create objects that hold rows and their respective column headers
public class CSVRow{
	def row
	def header
	def CSVRow(head, list){
		row = list
		header = head
	}

	//Method checks to see if the called property exists in the attributes
	//and returns the appropriate row item if so. Otherwise kicks out to
	//default message.
	def propertyMissing(value){
		for(i in (0..header.size()-1)){
			if(header[i] == value)
				return row[i]
		}
		return "No such property: $value"
	}
}



public class ActAsCSV{
	private def csvRows
	private def eachCount 

	def ActAsCSV(){
		csvRows = []
		eachCount = 0
	}
	
	def ActAsCSV(csvFile) {
		csvRows = []
		eachCount = 0
		parse(csvFile)
	}

	//Parses a .csv using regex, creating a list of CSVRow objects
	def parse(csvFile){
		def regex = ~/\G(?:^|,)(?:"([^"]*+)"|([^",]*+))/
		def checkAttr = true
		def attributes
		new File(csvFile).eachLine { line ->
		    def fields = []
		    def matcher = regex.matcher(line)
		    while (matcher.find()) {
		        fields << (matcher.group(1) ?: matcher.group(2))
		    }
		    if(checkAttr){
		    	attributes = fields
		    	checkAttr = false
		    }else
		    	csvRows << new CSVRow(attributes, fields)
			
		}
	}

	//returns the CSVRow object at the position of eachCount
	//in the csvRows list. Subsequent calls access the next
	//index until the end of list is reached, at which point 
	//eachCount is reset to 0 and the method returns null.
	//This would be used in a while loop with a try/catch to
	//access each row
	def each(Closure c){
		for(i in (0..csvRows.size()-1))
			  c(csvRows[i])
	}
}



