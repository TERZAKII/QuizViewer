package com.example.java2project;

import java.util.*;
import java.lang.*;
import java.io.*;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.scene.text.*;
import javafx.event.*;
import javafx.collections.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;

public class QuizViewer extends Application {
	private Quiz quiz; // Quiz class
	private ArrayList<Question> allQuestions; // Questions ---> addQuestions();
	private File file; // the File
	private String fileName; // the FileName

	private Stage stageForException; // Stage for expception
	private Scene mainScene;

	private int cur = 0; // current question

	private HashMap<QuizPane, Question> map = new HashMap<>(); // For saving all panes with its questions
	private ArrayList<QuizPane> listOfQuizPanes;

	public void start(Stage primaryStage)
	{
		primaryStage.setTitle("QuizViewer");

		BorderPane pane = new BorderPane();
		primaryStage.setMinHeight(400);
		primaryStage.setMinWidth(500);
		primaryStage.setResizable(false);

		Scene scene = new Scene(pane);
		
		Button fileChooserButton = new Button("Load File");
		fileChooserButton.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
		fileChooserButton.setOnAction(e -> {

			FileChooser fileChooser = new FileChooser(); // FileChooser
			fileChooser.setTitle("Choose a txt file...");
			file = fileChooser.showOpenDialog(null);	
			while(true)
			{
				boolean ok = true;
				if(ok==true)
				{
					fileName = file.getName();
					if(!fileName.substring(fileName.length()-4, fileName.length()).equals(".txt"))
					{
						ok = false;
						file = null;
						showExceptionStage();
					}
					try
					{
						//Loading quiz
						quiz = Quiz.loadFromFile(fileName);
						//Getting all questions from the list	
						allQuestions = quiz.getQuestions();

						long startTime = System.currentTimeMillis();

						// Saving all panes with its questions 
						for(int i=0; i<allQuestions.size(); i++)
						{
							//Text Area for Description
							TextArea textArea = new TextArea();
							String des = allQuestions.get(i).getDescription();
							//TextArea for its Description
							if(des.contains("{blank}"))
							{
								des = des.replace("{blank}", "______");
							}
							textArea.setText(des);
							textArea.setEditable(false);
							textArea.setScrollLeft(0);
							textArea.setWrapText(true);
							textArea.setFont(Font.font(14));
							textArea.setMaxHeight(150);

							HBox hboxForTextArea = new HBox();
							hboxForTextArea.getChildren().add(textArea);
							hboxForTextArea.setPadding(new Insets(8,8,8,8));

							//Next and Prev buttons
							Button btPrev = new Button("<<<");
							Button btNext = new Button(">>>");
							btPrev.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 14));
							btNext.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 14));

							//Submit button
							Button submit = new Button("Submit");
							Text textForStatus = new Text();
							Button addQuestion = new Button("Add question");
						
							//Answer box(Fillin or RadioButtons)
							VBox vboxForOptions = new VBox();
							if(allQuestions.get(i) instanceof Test)
							{
								Test test = new Test();
								test = (Test)allQuestions.get(i);
								//getting all answer, options from the Question
								ArrayList<String> optionsList = new ArrayList<>();
								optionsList.add(test.getAnswer());
								for(int k=0; k<3; k++)
								{
									optionsList.add(test.getOptionAt(k));
								}
								
								Collections.shuffle(optionsList);

								RadioButton op1 = new RadioButton(optionsList.get(0));
								RadioButton op2 = new RadioButton(optionsList.get(1));
								RadioButton op3 = new RadioButton(optionsList.get(2));
								RadioButton op4 = new RadioButton(optionsList.get(3));
								op1.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 14));
								op2.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 14));
								op3.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 14));
								op4.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 14));

								vboxForOptions.getChildren().addAll(op1, op2, op3, op4);
							}
							else
							{
								//TextField
								TextField textField = new TextField();
								vboxForOptions.getChildren().add(textField);
							}
							vboxForOptions.setSpacing(5);
							vboxForOptions.setPadding(new Insets(8,8,8,8));


							QuizPane quizPane = new QuizPane(btNext, btPrev, hboxForTextArea, vboxForOptions, textForStatus, submit, addQuestion);

							// Saving panes with questions
							map.put(quizPane, allQuestions.get(i));
						}

						listOfQuizPanes = new ArrayList<>();

						for(QuizPane each: map.keySet())
						{
							listOfQuizPanes.add(each);
						}

						scene.setRoot(listOfQuizPanes.get(cur));

						for(int i=0; i<listOfQuizPanes.size(); i++)
						{
							QuizPane each = listOfQuizPanes.get(i);

							each.setTextForStatus("Status: "
								+ (i+1) + "/" + listOfQuizPanes.size());

							Button nextButton = each.getNextButton();
							nextButton.setOnAction(ea -> {
								if(cur==listOfQuizPanes.size()-1)
								{
									String s = each.getTextForStatus();
									if(!s.contains("End of Quiz!"))
									{
										s += "\nEnd of Quiz!";
									}
									each.setTextForStatus(s);
								}
								else
								{
									cur++;
									scene.setRoot(listOfQuizPanes.get(cur));
								}
							});

							Button prevButton = each.getPrevButton();
							prevButton.setOnAction(ea -> {
								if(cur==0)
								{
									String s = each.getTextForStatus();
									if(!s.contains("Start of Quiz!"))
									{
										s += "\nStart of Quiz!";
									}
									each.setTextForStatus(s);
								}
								else
								{
									cur--;
									scene.setRoot(listOfQuizPanes.get(cur));
								}
							});

							Button submitButton = each.getSubmitButton();
							submitButton.setOnAction(ea -> {
								long endTime = System.currentTimeMillis();
								int correct = 0;
								for(int it = 0; it<listOfQuizPanes.size(); it++)
								{
									QuizPane qp = listOfQuizPanes.get(it);

									Question q = map.get(qp);

									String answer = q.getAnswer();
									answer = answer.toLowerCase();

									if(q instanceof Test)
									{
										VBox vbox = qp.getTheCenter();

										RadioButton rb1 = (RadioButton)vbox.getChildren().get(0);
										RadioButton rb2 = (RadioButton)vbox.getChildren().get(1);
										RadioButton rb3 = (RadioButton)vbox.getChildren().get(2);
										RadioButton rb4 = (RadioButton)vbox.getChildren().get(3);

										String userAnswer = "";
										if(rb1.isSelected())
										{
											userAnswer = rb1.getText();
										}
										else if(rb2.isSelected())
										{
											userAnswer = rb2.getText();
										}
										else if(rb3.isSelected())
										{
											userAnswer = rb3.getText();
										}
										else if(rb4.isSelected())
										{
											userAnswer = rb4.getText();
										}

										userAnswer = userAnswer.toLowerCase();

										if(answer.equals(userAnswer))
										{
											correct++;
										}
									}
									else
									{
										VBox vbox = qp.getTheCenter();
										TextField field = (TextField)vbox.getChildren().get(0);

										String userAnswer = field.getText();
										userAnswer = userAnswer.trim();
										userAnswer = userAnswer.toLowerCase();

										if(answer.equals(userAnswer))
										{
											correct++;
										}
									}
								}
								showSubmitStage(correct, listOfQuizPanes.size(), endTime-startTime);
							});

							Button addQuestionButton = each.getAddQuestionButton();
							addQuestionButton.setOnAction(ea -> {
								QuestionAdder qadder = new QuestionAdder();
								qadder.start(fileName);
							});
						}

					}
					catch(InvalidQuizFormatException ex)
					{
						ok = false;
						file = null;
						showExceptionStage();
					}
					if(ok == true)
					{
						break;
					}
				}
				else
				{
					System.out.println("Cancelled");
				}
			}
		});

		pane.setCenter(fileChooserButton);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void showExceptionStage()
	{
		stageForException = new Stage();
		stageForException.setMinHeight(100);
		stageForException.setMinWidth(200);
		stageForException.setResizable(false);

		VBox pane = new VBox();

		HBox hbox = new HBox();

		Label lbForException = new Label("InvalidQuizFormatException occured");
		lbForException.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
		lbForException.setAlignment(Pos.CENTER);
		Image image = new Image("error.png");
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(100);
		imageView.setFitHeight(100);

		hbox.getChildren().add(lbForException);
		hbox.getChildren().add(imageView);
		hbox.setAlignment(Pos.CENTER);
		hbox.setPadding(new Insets(5,5,5,5));
		hbox.setSpacing(20);

		Label text = new Label();
		text.setText("Try to open another file.");
		text.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 15));
		text.setAlignment(Pos.CENTER);

		HBox hbox2 = new HBox();
		hbox2.getChildren().add(text);
		hbox2.setAlignment(Pos.CENTER);
		hbox2.setPadding(new Insets(5,5,5,5));

		pane.getChildren().add(hbox);
		pane.getChildren().add(hbox2);

		Scene scene = new Scene(pane);
		stageForException.setScene(scene);
		stageForException.show();
	}

	private void showSubmitStage(int x, int y, long time)
	{
		Stage stageForSubmit = new Stage();

		VBox pane = new VBox();

		time = time / 1000;
		long s = time%60;
		time = time/60;
		long m = time%60;
		time = time/60;
		long h = time%24;

		String formalTime = "";
		if(h>9)
		{
			formalTime+=h+":";
		}
		else
		{
			formalTime+="0"+h+":";
		}
		
		if(m>9)
		{
			formalTime+=m+":";
		}
		else
		{
			formalTime+="0"+m+":";
		}
		
		if(s>9)
		{
			formalTime+=s;
		}
		else
		{
			formalTime+="0"+s;
		}

		HBox hbox = new HBox();
		Label label = new Label();
		label.setText("It took "+formalTime+" to take exam to you.");
		label.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 15));
		label.setTextFill(Color.BLUE);
		hbox.getChildren().add(label);
		hbox.setAlignment(Pos.CENTER);
		hbox.setPadding(new Insets(5,5,5,5));


		HBox hbox1 = new HBox();
		Label label1 = new Label("Number of correct answers: " + x);
		label1.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 15));
		label1.setTextFill(Color.GREEN);
		Image image1 = new Image("correct.png");
		ImageView imageView1 = new ImageView(image1);
		imageView1.setFitWidth(75);
		imageView1.setFitHeight(75);
		hbox1.getChildren().addAll(label1, imageView1);
		hbox1.setAlignment(Pos.CENTER);
		hbox1.setPadding(new Insets(5,5,5,5));
		hbox1.setSpacing(20);

		HBox hbox2 = new HBox();
		Label label2 = new Label("Number of incorrect answers: " + (y-x));
		label2.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 15));
		label2.setTextFill(Color.RED);
		Image image2 = new Image("incorrect.png");
		ImageView imageView2 = new ImageView(image2);
		imageView2.setFitWidth(75);
		imageView2.setFitHeight(75);
		hbox2.getChildren().addAll(label2, imageView2);
		hbox2.setAlignment(Pos.CENTER);
		hbox2.setPadding(new Insets(5,5,5,5));
		hbox2.setSpacing(20);

		HBox hbox3 = new HBox();
		Label label3 = new Label("You may try again...");
		label3.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 10));
		hbox3.getChildren().add(label3);
		hbox3.setAlignment(Pos.CENTER);
		hbox3.setPadding(new Insets(5,5,5,5));

		pane.getChildren().addAll(hbox, hbox1, hbox2, hbox3);

		Scene scene = new Scene(pane);
		stageForSubmit.setScene(scene);
		stageForSubmit.show();	
	}
}

class QuizPane extends BorderPane
{
	private Button next;
	private Button prev;
	private HBox textArea;
	private VBox theCenter;

	private Text textForStatus;
	private Button submit;
	private Button addQuestion;

	QuizPane(Button next, Button prev, HBox textArea, VBox theCenter, Text textForStatus, Button submit, Button addQuestion)
	{
		this.next = next;
		this.prev = prev;
		this.textArea = textArea;
		this.theCenter = theCenter;
		this.textForStatus = textForStatus;
		this.submit = submit;
		this.addQuestion = addQuestion;

		HBox hbox = new HBox();
		textForStatus.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 12));
		addQuestion.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 12));
		submit.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 12));
		hbox.getChildren().addAll(this.addQuestion,this.textForStatus, this.submit);
		hbox.setAlignment(Pos.TOP_CENTER);
		hbox.setSpacing(40);
		hbox.setPadding(new Insets(8,8,8,8));

		super.setCenter(theCenter);
		super.setLeft(prev);
		super.setRight(next);
		super.setTop(textArea);
		super.setBottom(hbox);
	}

	public Button getNextButton()
	{
		return this.next;
	}

	public Button getPrevButton()
	{
		return this.prev;
	}

	public Button getSubmitButton()
	{
		return this.submit;
	}

	public Button getAddQuestionButton()
	{
		return this.addQuestion;
	}

	public void setTextForStatus(String s)
	{
		this.textForStatus.setText(s);
	}

	public String getTextForStatus()
	{
		return this.textForStatus.getText();
	}

	public VBox getTheCenter()
	{
		return this.theCenter;
	}
}