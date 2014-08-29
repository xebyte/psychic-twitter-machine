package main;
import java.util.LinkedList;
import java.util.Random;

import com.sun.javafx.scene.control.behavior.TextAreaBehavior;
import com.sun.javafx.scene.control.skin.TextAreaSkin;

import gui.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class Main extends Application{
	static boolean flag = false;

	public static void main(String[] args) {
		System.out.println("hello world");
		
        Application.launch(Main.class, (java.lang.String[])null);

		

	}

	@Override
	public void start(Stage arg0) throws Exception {
		try {
			Pane page = (Pane) FXMLLoader.load(Main.class.getResource("gui.fxml"));
            Scene scene = new Scene(page);
            arg0.setScene(scene);
            arg0.setTitle("Arthur");
            arg0.show();
            
//            boolean flag = false;
            
            TextArea tb = (TextArea) scene.lookup("#textbox");
            FlowPane pane = (FlowPane) scene.lookup("#pane");
            
            tb.setStyle("-fx-font: 60px Tahoma;");
            pane.setStyle("-fx-font: 30px Tahoma;");
            
            tb.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.TAB) {
                        TextAreaSkin skin = (TextAreaSkin) tb.getSkin();
                        if (skin.getBehavior() instanceof TextAreaBehavior) {
                            TextAreaBehavior behavior = (TextAreaBehavior) skin.getBehavior();
                            LinkedList<Prediction> pred = getPredictions(tb.getText());
                            
                            String word = getLastWord(tb.getText());
                            word = word.replaceAll("\n", "");
                            Random rand = new Random();

                            // nextInt is normally exclusive of the top value,
                            // so add 1 to make it inclusive
                            int randomNum = rand.nextInt((pred.size()) + 1);
                            tb.replaceText(tb.getText().length() - word.length() - (pred.get(0).getTerm().equals(".") || pred.get(0).getTerm().equals(",") ? 1 : 0), tb.getText().length(), pred.get(0).getTerm() + " ");
                            event.consume();
                        }

                    }
                }
            });
            
            tb.textProperty().addListener((observable, oldValue, newValue) -> {

//        		System.out.println(newValue);
            	LinkedList<Prediction> predictions = getPredictions(newValue);
				String word = "";
				
				pane.getChildren().removeAll(pane.getChildren());
				
				double opacity = 1;
				for(Prediction pr : predictions) {
					Label label = new Label(pr.getTerm());
					label.setPadding(new Insets(5, 5, 5, 5));
					
					label.setStyle("-fx-background-color:rgba(85, 255, 68," + opacity + ");");
					pane.getChildren().add(label);
					opacity -= 0.1;
				}
				
				
//    				tb.appendText(word);
//    				flag = true;
				
    				
            	
//            	tb.replaceText(0, tb.getText().length(), oldValue + " " + word);
            });
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	private LinkedList<Prediction> getPredictions(String newValue) {
		SKPredicter p = new SKPredicter();
    	LinkedList<Prediction> predictions = new LinkedList<Prediction>();
		try {
			
			boolean isSentence = isSentence(newValue);
			String lastWord = getLastWord(newValue);
			
//			System.out.println("Context: " + newValue.substring(0, newValue.length() - lastWord.length()));
//			System.out.println("Current word: " + lastWord);
			predictions = p.predict((isSentence ? "^" : newValue.substring(0, newValue.length() - lastWord.length())), lastWord);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return predictions;
	}

	private String getLastWord(String newValue) {
		String lastWord = "";
		for(int i = newValue.length()-1; i >= 0; i--) {
			if(newValue.charAt(i) == ' ') {
				break;
			}
			
			lastWord = newValue.charAt(i) + lastWord;
		}
		
		return lastWord;
	}
	
	private boolean isSentence(String newValue) {
		for(int i = newValue.length()-1; i >= 0; i--) {
			if((i-1) < 0 || newValue.charAt(i-1) == '.') return true;
			if(newValue.charAt(i) == ' ') {
				break;
			}
		}
		
		return false;
	}

}
