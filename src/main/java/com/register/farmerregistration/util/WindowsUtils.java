package com.register.farmerregistration.util;


import com.jfoenix.controls.*;
import com.register.farmerregistration.FarmerRegistrationApplication;
import com.register.farmerregistration.controller.BaseController;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.register.farmerregistration.FarmerRegistrationApplication.springContext;

public class WindowsUtils {
	
	public static final String BASE_APPLICATION_CSS_PATH = FarmerRegistrationApplication.class.getResource("/css/application.css").toExternalForm();
	public static final String ICON_APP_PATH = FarmerRegistrationApplication.class.getResource("/images/icon.png").toExternalForm();
	
	public static <T> void replaceFxmlOnWindow(Pane root, String path, Stage stage, HashMap<String, T> parameters) throws Exception {
		FXMLLoader loader = loadFxml(path);
		
		root.getChildren().clear();
        root.getChildren().add(loader.load());
        
        BaseController baseController = loader.getController();
        baseController.init(stage, parameters);
	}
	
	public static <T> Stage openNewWindow(String fxmlPath, String title, String iconPath, HashMap<String, T> parameters, Modality windowModality) throws Exception {
		
		Stage stage = new Stage();
        stage.initModality(windowModality);
        
        return openNewWindow(stage, fxmlPath, title, iconPath, parameters);
	}
	
	public static <T> Stage openNewWindow(Stage stage, String fxmlPath, String title, String iconPath, HashMap<String, T> parameters) throws Exception {
		
        stage.getIcons().add(new Image(WindowsUtils.ICON_APP_PATH));
        stage.setTitle(title);
        stage.setResizable(false);

        FXMLLoader loader = loadFxml(fxmlPath);
		//loader.setControllerFactory(springContext::getBean);
        try {
    		Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(BASE_APPLICATION_CSS_PATH);
            stage.setScene(scene);
            stage.show();
        } catch(Exception e) {
        	e.printStackTrace();
        }
        
        BaseController baseController = loader.getController();
        baseController.init(stage, parameters);
        
        return stage;
	}
	
	public static FXMLLoader loadFxml(String url) {
		  
		 try (InputStream fxmlStream = WindowsUtils.class.getResourceAsStream(url)) {
			 FXMLLoader loader = new FXMLLoader();
			 loader.setLocation(WindowsUtils.class.getResource(url));
			 loader.setControllerFactory( clazz -> { return springContext.getBean(clazz);});
			 loader.setResources(FarmerRegistrationApplication.i18n.getBundle());  
			 
			 return loader;
		 } catch (IOException ioException) {
			 throw new RuntimeException(ioException);
		 }
	}
	
	public static void createDefaultDialog(StackPane root, String title, String body, DialogAction action) {
		
		JFXDialogLayout content = new JFXDialogLayout();
		content.setHeading(new Text(title));
		content.setBody(new Text(body));
		
		JFXDialog dialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);
		
		JFXButton okButton = new JFXButton("Ok");
		okButton.setOnAction(event -> { dialog.close(); action.onAction(); });
		content.setActions(okButton);
		
		dialog.show();
	}
	
	public static void createQuestionDialog(StackPane root, String title, String body, DialogAction action) {
		
		JFXDialogLayout content = new JFXDialogLayout();
		content.setHeading(new Text(title));
		content.setBody(new Text(body));
		
		JFXDialog dialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);
		
		JFXButton yesButton = new JFXButton("Yes");
		yesButton.setOnAction(event -> { dialog.close(); action.onAction(); });
		content.setActions(yesButton);
		
		JFXButton noButton = new JFXButton("No");
		noButton.setOnAction(event -> { dialog.close(); });
		content.setActions(noButton);
		
		dialog.show();
	}
	
	public static void validateTextField(JFXTextField textField) {
		
		textField.focusedProperty().addListener((o, oldValue, newValue) -> {
			if (!newValue) {
				textField.validate();
			}
		});
	}
	
	public static void validateTextField(JFXPasswordField textField) {
		
		textField.focusedProperty().addListener((o, oldValue, newValue) -> {
			if (!newValue) {
				textField.validate();
			}
		});
	}

	public static void watchEvents(TextField textField, WatchListener listener) {
		textField.focusedProperty().addListener((o, oldValue, newValue) -> {
			listener.watch(newValue);
		});
	}
	
	public static <T> void watchEvents(ComboBox<T> comboBox, WatchListener listener) {
		comboBox.focusedProperty().addListener((o, oldValue, newValue) -> {
			listener.watch(newValue);
		});
	}
	
	public static void onTextFieldTextChange(TextField textField, ChangeListener<? super String> listener) {
		textField.textProperty().addListener(listener);
	}
	
	public static boolean isTextFieldEmpty(TextField textField) {
		return textField.getText().trim().isEmpty();
	}
	
	public static boolean isLabelEmpty(Label label) {
		return label.getText().trim().isEmpty();
	}
	
	public static boolean isTextAreaEmpty(TextArea textArea) {
		return textArea.getText().trim().isEmpty();
	}
	
	@SuppressWarnings("rawtypes") 
	public static boolean isComboBoxSelected(ComboBox comboBox) {
		return comboBox.getSelectionModel().isEmpty();
	}
	
	public static String getTextFromTextField(TextField textField) {
		return isTextFieldEmpty(textField) ? null : textField.getText();
	}
	
	public static String getTextFromLabel(Label label) {
		return isLabelEmpty(label) ? null : label.getText();
	}
	
	public static void setTextInLabel(Label label, String text) {
		if (text != null) {
			label.setText(text);
		}
	}
	
	public static void setTextInLabel(Label label, double number) {
		if (number != 0.0) {
			label.setText(Double.toString(number));
		}
	}
	
	public static void setTextInTextField(TextField textField, String text) {
		if (text != null) {
			textField.setText(text);
		}
	}
	
	public static void setTextInTextField(TextField textField, double number) {
		if (number != 0.0) {
			textField.setText(Double.toString(number));
		}
	}
	
	public static void setTextInTextField(TextField textField, int number) {
		if (number != 0) {
			textField.setText(Integer.toString(number));
		}
	}
	
	public static double getDoubleFromTextField(TextField textField) {
		return isTextFieldEmpty(textField) ? 0.0 : Double.parseDouble(textField.getText());
	}
	
	public static int getIntegerFromTextField(TextField textField) {
		return isTextFieldEmpty(textField) ? 0 : Integer.parseInt(textField.getText());
	}
	
	public static long getLongFromTextField(TextField textField) {
		return isTextFieldEmpty(textField) ? 0 : Long.parseLong(textField.getText());
	}

	public static String getTextFromTextArea(TextArea textArea) {
		return isTextAreaEmpty(textArea) ? null : textArea.getText();
	}
	
	public static void setTextInTextArea(TextArea textArea, String text) {
		if (text != null) {
			textArea.setText(text);
		}
		
	}
	
//	@SuppressWarnings({ "unchecked" })
//	public static <T> void addComboBoxItens(ComboBox<T> comboBox, IBaseService<T> service) {
//		service.findAll(e -> {
//			comboBox.getItems().addAll((List<T>) e.getSource().getValue());
//		}, null);
//	}
	
	public static <T> void addComboBoxItens(ComboBox<T> comboBox, List<T> items) {
		comboBox.getItems().addAll(items);
	}
	
	public static <T> void onComboBoxItemSelected(ComboBox<T> comboBox, ComboBoxSelectListener<T> listener) {
		comboBox.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
			listener.onSelected(newValue);
		});
	}
	
	public static <U> U getSelectedComboBoxItem(ComboBox<U> comboBox) {
		return comboBox.getValue() != null ? comboBox.getValue() : null;
	}
	
	public static <T> void setSelectedComboBoxItem(ComboBox<T> comboBox, T item) {
		if (item != null) {
			comboBox.getSelectionModel().select(item);
		}
	}
	
	public static String formatCPF(String cpf) {
		Pattern pattern = Pattern.compile("(\\d{3})(\\d{3})(\\d{3})(\\d{2})");
		Matcher matcher = pattern.matcher(cpf);
		if (matcher.matches()) 
			cpf = matcher.replaceAll("$1.$2.$3-$4");
		return cpf;		
	}
	
	
}
