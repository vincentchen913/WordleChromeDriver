import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
  
public class Main {  
	
	static ArrayList<Word> words;
	static String correct;
	
	final static int NUM_GUESSES = 7;
	final static int NUM_LETTERS = 6;
  
    public static void main(String[] args) throws InterruptedException, FileNotFoundException {  
    	
		words = new ArrayList<>();
		correct = "";
		Word.fillScores();
		File f = new File("words.txt");
		Scanner sc = new Scanner(f);
    	while(sc.hasNext()) {
			String curr = sc.next();
			Word temp = new Word(curr);
			words.add(temp);
		}
    	sc.close();
		Collections.sort(words);
    	
        // System Property for Chrome Driver   
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");  
          
        // Instantiate a ChromeDriver class.     
        WebDriver driver=new ChromeDriver();  
          
        // Launch Website  
        driver.navigate().to("https://www.nytimes.com/games/wordle/index.html");  
          
        // Maximize the browser  
        driver.manage().window().maximize();    
          
        // Click out of tutorial
        Actions a = new Actions(driver);
        a.click().perform();
        Thread.sleep(1000);
        WebElement word = driver.switchTo().activeElement();
        
        Word guess = new Word("soare");
        a.moveToElement(word).sendKeys(guess.name).perform();
        a.moveToElement(word).sendKeys(Keys.ENTER).perform();
                
        Thread.sleep(600);
        
        String[] seq = new String[5];
        
        for(int i = 1; i < NUM_GUESSES; i++) {
        	for(int j = 1; j < NUM_LETTERS; j++) {
	        	String path = "//*[@id=\"wordle-app-game\"]/div[1]/div/div[" + i + "]/div[" + j + "]/div";
	        	WebElement firstSquare = driver.findElement(By.xpath(path));
	            seq[j - 1] = firstSquare.getAttribute("data-state");
	            Thread.sleep(200);
	        }
        	if(allCorrect(seq)) {
        		break;
        	}
        	Word next = getNextWord(guess, seq);
        	guess = next;
        	a.moveToElement(word).sendKeys(guess.name).perform();
            a.moveToElement(word).sendKeys(Keys.ENTER).perform();
            Thread.sleep(600);
        }
        Thread.sleep(5000);
        driver.close();
    }  
    
    public static boolean allCorrect(String[] seq) {
    	for(int i = 0; i < seq.length; i++) {
    		if(!seq[i].equals("correct")) return false;
    	}
    	return true;
    }
    public static Word getNextWord(Word w, String[] seq) {
		//check for greens
		for(int i = 0; i < seq.length; i++) {
			if(seq[i].equals("correct")) {
				if(correct.indexOf(w.name.charAt(i)) == -1) {
					correct += (w.name.charAt(i));
				}
				Iterator<Word> it = words.iterator();
				while(it.hasNext()) {
					Word curr = it.next();
					if(w.name.charAt(i) != curr.name.charAt(i)) {
						it.remove();
					}
				}
			}
		}
		//check for blacks
		for(int i = 0; i < seq.length; i++) {
			if(seq[i].equals("absent")) {
				char c = w.name.charAt(i);
				Iterator<Word> it = words.iterator();
				while(it.hasNext()) {
					Word curr = it.next();
					if(curr.name.indexOf(c) != -1 && correct.indexOf(c) == -1) {
						it.remove();
					}
				}
			}
		}
		//check for yellows
		for(int i = 0; i < seq.length; i++) {
			if(seq[i].equals("present")) {
				Iterator<Word> it = words.iterator();
				while(it.hasNext()) {
					Word curr = it.next();
					if(curr.name.indexOf(w.name.charAt(i)) == -1 || curr.name.charAt(i) == w.name.charAt(i)) {
						it.remove();
					}
				}
			}
		}
		Collections.sort(words);
		return words.size() == 0 ? null : words.get(words.size() - 1);
	}
}  
