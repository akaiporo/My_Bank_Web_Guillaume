package application;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains a list of tools which be can be use in most projects
 * @author Guillaume
 *
 */
public class Tools {

	/**
	 * Static Pattern type variable that contains the Regex of a valide email. (xxx@yyy.zz)
	 */
	private static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	private static String url = "jdbc:mysql://localhost/my_personnal_bank?autoReconnect=true&useSSL=false";
	
	/**
	 * 
	 * @param sep : Pour les espaces, passer "\\s" en param�tre
	 * @param str : Chaine � spliter selon sep
	 * @return La chaine split�e selon le s�parateur entr� en param�tre
	 */
	public static StringBuilder eraseChar(String str, String sep){
		String[] string = str.split(sep);
		StringBuilder finalStr = new StringBuilder();
		//Preventing the user from trying to set an empty password composed of multiples white-spaces
		for(String s : string){
			finalStr.append(s);
		}
		
		return finalStr;
	}
	
	public static Pattern getMailRegex(){
		return VALID_EMAIL_ADDRESS_REGEX;
	}
	
	public static String getUrl(){
		return url;
	}
	
	/**
	 * Check an e-mail address is valid or not
	 * @param email to check
	 * @return true if the mail respect the Pattern regex
	 */
	public static boolean checkMail(String email) {
		Matcher matcher = Tools.VALID_EMAIL_ADDRESS_REGEX.matcher(email);
		return matcher.find();
	}
	/**
	 * @return la date du jour
	 */
	public static Date today(){
		return Calendar.getInstance().getTime();
	}
	public static Date futureDate(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH,1);
		return cal.getTime();
	}
	public static Date pastDate(){
		return new GregorianCalendar(2014,4,10).getTime();
	}
	
	/**
	 * 
	 * @param local : Date de type javafx (date picker)
	 * @return date de type java
	 */
	public static Date LocalDateToDate(LocalDate local) {
		return Date.from(local.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	/**
	 * 
	 * @param date : Date de type java
	 * @return :  date de type javafx (date picker)
	 */
	public static LocalDate DateToLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}	
}
