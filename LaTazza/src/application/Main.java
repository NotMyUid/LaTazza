package application;
	
import java.io.IOException;
import java.sql.SQLException;

import DataAccessObject.Database.DataBaseConnection;
import DataAccessObject.Database.DataBasePopulator;
import application.controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class Main extends Application {
	
	Stage primaryStage;
	Scene scene, scene2;
	Controller contr;
	DataBaseConnection conn;
	DataBasePopulator init;
	
	@Override
	public void start(Stage primaryStage) throws ClassNotFoundException, SQLException {
		conn = new DataBaseConnection();
		conn.initDataBase();
		init = new DataBasePopulator(conn);
		if(!init.existsSchema()) { init.createSchema(); init.inserimentiTabellePresentazione(true); }
		this.primaryStage = primaryStage;
		this.primaryStage.setResizable(true);
		this.primaryStage.setMinHeight(450);
		this.primaryStage.setMinWidth(600);
		primaryStage.getIcons().add(new Image("file:res/icon.png"));
		FXMLLoader loader = new FXMLLoader(
					getClass().getResource("view/MainView.fxml"));
		try {
				scene = new Scene(loader.load(),800,600);
		} catch (IOException e) {
				e.printStackTrace();		
		}
		contr=loader.getController();
		contr.init(conn.getConnection());
		primaryStage.setScene(scene);
		primaryStage.setTitle("LaTazza");
		primaryStage.show();
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}


	@Override
	public void stop() throws Exception {
		contr.saveAllInFile(conn.getConnection());
		conn.closeDataBase();
	}
}
