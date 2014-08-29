package main;
import java.util.LinkedList;

import com.sun.javafx.scene.control.behavior.TextAreaBehavior;
import com.sun.javafx.scene.control.skin.TextAreaSkin;

import gui.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
            Label label = (Label) scene.lookup("#label");
            
            tb.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.TAB) {
                        TextAreaSkin skin = (TextAreaSkin) tb.getSkin();
                        if (skin.getBehavior() instanceof TextAreaBehavior) {
                            TextAreaBehavior behavior = (TextAreaBehavior) skin.getBehavior();
                            LinkedList<Prediction> pred = getPredictions(tb.getText());
                            
                            tb.appendText(pred.get(0).getTerm());
                            event.consume();
                        }

                    }
                }
            });
            
            tb.textProperty().addListener((observable, oldValue, newValue) -> {

//        		System.out.println(newValue);
            	LinkedList<Prediction> predictions = getPredictions(newValue);
				String word = "";
				
				for(Prediction pr : predictions) {
					word += pr.getTerm() + ", ";
				}
				
				label.setText(word);
				
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
			
			boolean isSentence = false;
			String lastWord = "";
			for(int i = newValue.length()-1; i >= 0; i--) {
				if((i-1) < 0 || newValue.charAt(i-1) == '.') {
					isSentence = true;
					System.out.println("This is the start of a new sentence");
				}
				if(newValue.charAt(i) == ' ') {
					break;
				}
				
				lastWord = newValue.charAt(i) + lastWord;
			}
			
//			System.out.println("Context: " + newValue.substring(0, newValue.length() - lastWord.length()));
//			System.out.println("Current word: " + lastWord);
			predictions = p.predict((isSentence ? "^" : "") + newValue.substring(0, newValue.length() - lastWord.length()), lastWord);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return predictions;
	}

}
