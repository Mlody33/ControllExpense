package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class ConvertDate {
	
	private Date date;
	private LocalDate localDate;
	private String stringDate;
	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");//TODO CHANGE FORMAT DATE TO dd-MM-yyyy HERE AND INTO DATABASE
	private final SimpleDateFormat DATE_FORMAT_DAY_NAME = new SimpleDateFormat("E");
	
	public ConvertDate(Date date){
		this.date = date;
		this.stringDate = DATE_FORMAT.format(date);
		this.localDate =  LocalDate.parse(stringDate);		
	}
	public ConvertDate(LocalDate localDate){
		this.localDate = localDate;
		this.stringDate = localDate.toString();
		this.date = java.sql.Date.valueOf(localDate);
	}
	public ConvertDate(String stringDate){
		this.stringDate = stringDate;
		//this.date = DATE_FORMAT.parse(stringDate);
		this.localDate = new java.sql.Date(this.date.getTime()).toLocalDate();
	}
	
	public Date getDate(){
		return this.date;
	};
	
	public LocalDate getLocalDate(){
		return this.localDate;
	};
	
	public String getStringDate(){
		return this.stringDate;
	};
	
	
	
	public Date toDateFormat() throws ParseException{
        Date formattedDate = DATE_FORMAT.parse(stringDate);
		return formattedDate;
	}
	
	public String toStringFormat(){
		String formattedDate = DATE_FORMAT.format(date);
		return formattedDate;
	}
	
	public String toStringFormatDayName(){
		String formattedDate = DATE_FORMAT_DAY_NAME.format(date);
		return formattedDate;
	}
	
}


/*

G   Era designator  Text    AD
y   Year    Year    1996; 96
Y   Week year   Year    2009; 09
M   Month in year   Month   July; Jul; 07
w   Week in year    Number  27
W   Week in month   Number  2
D   Day in year Number  189
d   Day in month    Number  10
F   Day of week in month    Number  2
E   Day name in week    Text    Tuesday; Tue
u   Day number of week (1 = Monday, ..., 7 = Sunday)    Number  1
a   Am/pm marker    Text    PM
H   Hour in day (0-23)  Number  0
k   Hour in day (1-24)  Number  24
K   Hour in am/pm (0-11)    Number  0
h   Hour in am/pm (1-12)    Number  12
m   Minute in hour  Number  30
s   Second in minute    Number  55
S   Millisecond Number  978
z   Time zone   General time zone   Pacific Standard Time; PST; GMT-08:00
Z   Time zone   RFC 822 time zone   -0800
X   Time zone   ISO 8601 time zone  -08; -0800; -08:00

*/