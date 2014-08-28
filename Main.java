package main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application{

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
            arg0.setTitle("FXML is Simple");
            arg0.show();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
