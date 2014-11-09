package br.com.min.utils;

import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import br.com.itau.internet.seguranca.jarvis.sectoken.JarvisToken;
import br.com.min.entity.Role;
import br.com.min.entity.Usuario;
import br.com.min.filter.SecurityFilter;

public class Utils {

	public static final SimpleDateFormat dateFormat  = new SimpleDateFormat("dd/MM/yyyy");
	public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	public static final SimpleDateFormat dateTimeSecondToFileFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
	public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	public static final SimpleDateFormat dayMonthFormat = new SimpleDateFormat("dd/MM");
	public static final NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
	
	private static final Map<String, String> ignoredWords = new HashMap<String, String>();
	
	static{
		ignoredWords.put("DA", "true");
		ignoredWords.put("DE", "true");
		ignoredWords.put("DOS", "true");
		ignoredWords.put("DO", "true");
		ignoredWords.put("DI", "true");
		ignoredWords.put("E", "true");
	}
	
	public static String encriyt(String toEncrypt){
		JarvisToken token = new JarvisToken();
		String encryptedPassword;
		try {
			encryptedPassword = token.getToken("", toEncrypt, "");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return encryptedPassword;
	}
	
	public static boolean isTokenValid(String encrypted, String compare){
		JarvisToken token = new JarvisToken();
		try {
			return token.isTokenValid("", compare, "", encrypted);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
			
	}
	
	public static boolean hasRole(Role role, HttpServletRequest request){
		Map<String, Boolean> hasRole = (Map<String, Boolean>) request.getSession().getAttribute(SecurityFilter.HAS_ROLE);
		if(hasRole.get(Role.ADMIN.name())){
			return true;
		}
		return hasRole.get(role.name());
	}

	public static Usuario getUsuarioLogado(HttpServletRequest request){
		Usuario logado = (Usuario) request.getSession().getAttribute(SecurityFilter.LOGGED_USER);
		return logado;
	}
	
	public static List<String> createListQueryFromString(String query){
		String[] words = query.split(" ");
		List<String> result = new ArrayList<String>();
		for(String word : words){
			word = word.trim();
			if(word.isEmpty()){
				continue;
			}
			if(ignoredWords.containsKey(word.toUpperCase())){
				continue;
			}
			//word = Utils.replaceSpecialChars(word);
			result.add(word);
		}
		return result;
	}
	
	public static String replaceSpecialChars(String word){
		word = Normalizer.normalize(word, Normalizer.Form.NFD);
		word = word.replaceAll("[^\\p{ASCII}]", "");
		word = word.replaceAll("[^\\w]", "");
		return word;
	}
	
	public static void main(String[] args) {
		System.out.println(createListQueryFromString("Mário marques Guimarães da silva"));
	}
}
