package com.example.java2project;

import java.io.*;
import java.util.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.text.*;
import javafx.geometry.*;

public class QuestionAdder {
	public void start(String fileName) {
		Stage primaryStage = new Stage();
		primaryStage.setMinHeight(200);
		primaryStage.setMinWidth(400);
		primaryStage.setResizable(false);

		HBox toChoose = new HBox();
		BorderPane mainPane = new BorderPane();
		Button test = new Button("Test");
		Button fill = new Button("Fill in");
		test.setMinHeight(200);
		test.setMinWidth(200);
		test.setFont(Font.font("Verdana", 30));
		fill.setFont(Font.font("Verdana", 30));
		fill.setMinHeight(200);
		fill.setMinWidth(200);

		toChoose.getChildren().addAll(test, fill);
		toChoose.setSpacing(8);
		toChoose.setAlignment(Pos.CENTER);

		Scene scene = new Scene(toChoose);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Add question");
		primaryStage.show();

		test.setOnAction(e -> {

			GridPane pane = new GridPane();
			Label labelForDes = new Label("Description: ");
			labelForDes.setFont(Font.font("Verdana",15));
			Label labelForAns = new Label("Answer: ");
			labelForAns.setFont(Font.font("Verdana",15));
			Label labelForOp1 = new Label("Option 1: ");
			labelForOp1.setFont(Font.font("Verdana",15));
			Label labelForOp2 = new Label("Option 2: ");
			labelForOp2.setFont(Font.font("Verdana",15));
			Label labelForOp3 = new Label("Option 3: ");
			labelForOp3.setFont(Font.font("Verdana",15));

			TextField fieldForDes = new TextField();
			TextField fieldForAns = new TextField();
			TextField fieldForOp1 = new TextField();
			TextField fieldForOp2 = new TextField();
			TextField fieldForOp3 = new TextField();

			pane.add(labelForDes, 0, 0);
			pane.add(fieldForDes, 1, 0);
			pane.add(labelForAns, 0, 1);
			pane.add(fieldForAns, 1, 1);
			pane.add(labelForOp1, 0, 2);
			pane.add(labelForOp2, 0, 3);
			pane.add(labelForOp3, 0, 4);
			pane.add(fieldForOp1, 1, 2);
			pane.add(fieldForOp2, 1, 3);
			pane.add(fieldForOp3, 1, 4);
			pane.setVgap(5);

			Button btAdd = new Button("ADD");
			btAdd.setFont(Font.font("Verdana", 15));
			HBox hbox = new HBox();
			hbox.getChildren().add(btAdd);
			hbox.setPadding(new Insets(10, 10, 10, 10));
			hbox.setAlignment(Pos.CENTER);

			Stage stage = new Stage();
			stage.setMinWidth(200);
			stage.setMinHeight(100);
			StackPane paneForStage = new StackPane();
			Text text = new Text();
			text.setFont(Font.font("Verdana", 20));
			paneForStage.getChildren().add(text);
			paneForStage.setPadding(new Insets(8,8,8,8));

			Scene scene2 = new Scene(paneForStage);
			stage.setScene(scene2);

			btAdd.setOnAction(ea -> {
				if(fieldForDes.getText().equals(""))
				{
					text.setText("Description field is empty!");
					stage.show();
				}
				else if(fieldForAns.getText().isEmpty())
				{
					text.setText("Answer field is empty!");
					stage.show();
				}
				else if(fieldForOp1.getText().isEmpty())
				{
					text.setText("Option 1 field is empty!");
					stage.show();
				}
				else if(fieldForOp2.getText().isEmpty())
				{
					text.setText("Option 2 field is empty!");
					stage.show();
				}
				else if(fieldForOp3.getText().isEmpty())
				{
					text.setText("Option 3 field is empty!");
					stage.show();
				}
				else
				{
					try
					{
						FileWriter fw = new FileWriter(fileName, true);
						PrintWriter output = new PrintWriter(fw);
						File file = new File(fileName);
						Scanner ch = new Scanner(file);

						if(!ch.hasNext())
						{
							output.println(fieldForDes.getText());
							output.println(fieldForAns.getText());
							output.println(fieldForOp1.getText());
							output.println(fieldForOp2.getText());
							output.println(fieldForOp3.getText());
						}
						else
						{
							output.println();
							output.println(fieldForDes.getText());
							output.println(fieldForAns.getText());
							output.println(fieldForOp1.getText());
							output.println(fieldForOp2.getText());
							output.println(fieldForOp3.getText());
						}
						output.close();

						fieldForOp3.setText("");
						fieldForOp2.setText("");
						fieldForOp1.setText("");
						fieldForAns.setText("");
						fieldForDes.setText("");

						text.setText("The question has been added.");
						stage.show();
					}
					catch(IOException i)
					{
						System.out.println("Some error happened.");
					}

				}
			});

			mainPane.setCenter(pane);
			mainPane.setBottom(hbox);
			mainPane.setPadding(new Insets(5,5,5,5));
			scene.setRoot(mainPane);
		});

		fill.setOnAction(e -> {
			GridPane pane = new GridPane();
			Label labelForDes = new Label("Description (answer -> {blank}): ");
			labelForDes.setFont(Font.font("Verdana",15));
			labelForDes.setWrapText(true);
			Label labelForAns = new Label("Answer: ");
			labelForAns.setFont(Font.font("Verdana",15));

			TextField fieldForDes = new TextField();
			TextField fieldForAns = new TextField();

			pane.add(labelForDes, 0, 0);
			pane.add(fieldForDes, 1, 0);
			pane.add(labelForAns, 0, 1);
			pane.add(fieldForAns, 1, 1);
			pane.setVgap(5);

			Button btAdd = new Button("ADD");
			btAdd.setFont(Font.font("Verdana", 15));
			HBox hbox = new HBox();
			hbox.getChildren().add(btAdd);
			hbox.setPadding(new Insets(10, 10, 10, 10));
			hbox.setAlignment(Pos.CENTER);

			Stage stage = new Stage();
			stage.setMinWidth(200);
			stage.setMinHeight(100);
			StackPane paneForStage = new StackPane();
			Text text = new Text();
			text.setFont(Font.font("Verdana", 20));
			paneForStage.getChildren().add(text);
			paneForStage.setPadding(new Insets(8,8,8,8));

			Scene scene2 = new Scene(paneForStage);
			stage.setScene(scene2);

			btAdd.setOnAction(ea -> {
				if(fieldForDes.getText().equals(""))
				{
					text.setText("Description field is empty!");
					stage.show();
				}
				else if(fieldForAns.getText().isEmpty())
				{
					text.setText("Answer field is empty!");
					stage.show();
				}
				else
				{
					try
					{
						FileWriter fw = new FileWriter(fileName, true);
						PrintWriter output = new PrintWriter(fw);
						File file = new File(fileName);
						Scanner ch = new Scanner(file);

						if(!ch.hasNext())
						{
							output.println(fieldForDes.getText());
							output.println(fieldForAns.getText());
						}
						else
						{
							output.println();
							output.println(fieldForDes.getText());
							output.println(fieldForAns.getText());
						}
						output.close();

						fieldForAns.setText("");
						fieldForDes.setText("");

						text.setText("The question has been added");
						stage.show();
					}
					catch(IOException i)
					{
						System.out.println("Some error happened.");
					}
				}
			});

			mainPane.setCenter(pane);
			mainPane.setBottom(hbox);
			mainPane.setPadding(new Insets(5,5,5,5));
			scene.setRoot(mainPane);
		});
	}
}