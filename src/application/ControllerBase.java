package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public abstract class ControllerBase {
	private Mediator mediator = null;
	
	public abstract void initialize(Mediator mediator);
	
	/**
	 * Initialise le mediator qui servira à créer l'entity manager dans les classes filles
	 * @param mediator : Si il existe déjà, on ne le recrée pas (un seul mediator par programme)
	 */
	public void initMediator(Mediator mediator) {
		if(mediator==null) {
			throw new NullPointerException("mediator cannot be null");
		}
		if(this.mediator!=null) {
			throw new UnsupportedOperationException("Cannot initMediator twice");
		}
		this.mediator = mediator;
		this.initialize(this.mediator);
	}
	public Mediator getMediator() {
		return this.mediator;
	}
	public Parent loadFxml(String fxml) throws IOException {
		return loadFxml(fxml, this.mediator);
	}
	/**
	 * Loader une scène dans le main en passant un mediator
	 * @param fxml : fxml à charger
	 * @param mediator : mediator à passer
	 * @return : le Parent loadé
	 * @throws IOException : 
	 */
	public static Parent loadFxml(String fxml, Mediator mediator) throws IOException {
		FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxml));
		Parent root = (Parent)loader.load();
		ControllerBase controller = loader.getController();
		controller.initMediator(mediator);
		return root;
	}
	/**
	 * Load une scène dans le StackPane "content" de la MainWindow
	 * @param fxml : fxml à charger
	 */
	public void loadSubScene(String fxml){
		try{
			MainWindowController.contentPane.getChildren().setAll(loadFxml(fxml));
		}
		catch(IOException e){	
			System.out.println(e.getMessage());
		}
	}
}
