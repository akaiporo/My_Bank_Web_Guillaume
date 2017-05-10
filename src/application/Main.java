package application;
	
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Main extends Application {
	
	private static Stage pStage;
	 
	public static Stage getPrimaryStage() {
	        return pStage;
	}
	/**
	 * Setter le stage en static permet de faire des add Scene n'importe o� dans le programme
	 * @param pStage : Stage principal de l'appli
	 */
	private void setPrimaryStage(Stage pStage) {
	     Main.pStage = pStage;
	     Main.pStage.setResizable(true);
	}
	@Override
	public void start(Stage primaryStage) {
		try {
			this.setPrimaryStage(primaryStage);
			this.emf = Persistence.createEntityManagerFactory("My_Bank");
			this.mediator = new Mediator( this.emf );
			
			Scene scene = new Scene(
				ControllerBase.loadFxml("MainWindow.fxml", mediator)
			);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop() throws Exception {
		this.emf.close();
		super.stop();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	private Mediator mediator = null;
	private EntityManagerFactory emf = null;

}
