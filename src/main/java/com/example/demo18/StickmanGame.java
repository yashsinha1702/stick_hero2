package com.example.demogame;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.security.cert.PolicyNode;
import java.util.ArrayList;

public class StickmanGame extends Application {

    private static final double PLATFORM_HEIGHT = 150;
    private static double platform_distance;

    AnchorPane root;
    Stickman stickman;
    Platform platform;

    int status=1;
    private Timeline growingTimeline;
    private Timeline walkingTimeline;

    boolean stickSpawned = false;
    boolean isWalking = false;

    boolean growingInProgress;

    Scene scene;

    Scene endscene;

    AnchorPane root2;

    Button retry;


    ArrayList<Double> newrect = new ArrayList<Double>();

    @Override
    public void start(Stage primaryStage) {
        root2=new AnchorPane();
        retry=new Button("Retry");
        root2.getChildren().add(retry);
        endscene=new Scene(root2,800,600);
        retry.setOnAction(e->{
            primaryStage.setScene(scene);
            initializePlatform();
            initializeStickman();
            startGameLoop();
        });
        root = new AnchorPane();
        root.setPrefSize(800,600);
        Image backgroundimage=new Image("file:C:\\Users\\syash\\OneDrive\\Pictures\\mobile-video-game-vector-background-4406706_1280.png");
        BackgroundImage bi=new BackgroundImage(backgroundimage,null,null,null,null);
        Background background=new Background(bi);
        root.setBackground(background);
        scene = new Scene(root, 800, 600);

        initializePlatform();
        initializeStickman();
        stickman.setStage(primaryStage);

        Button button = new Button("Start Game");
        button.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #3498db, #2980b9); " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 18px; " +
                        "-fx-font-family: 'Arial'; " +
                        "-fx-padding: 15px 25px; " +
                        "-fx-border-radius: 30px; " +
                        "-fx-border-color: derive(#2980b9, -30%); " +
                        "-fx-border-width: 3px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 3); " +
                        "-fx-cursor: hand;"
        );

        // Add a scaling animation on hover
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), button);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);

        button.setOnMouseEntered(event -> scaleTransition.playFromStart());
        button.setOnMouseExited(event -> scaleTransition.setToX(1.0));
        Pane root1=new Pane();
        Scene scene1 = new Scene(root1, 800, 400);
        button.translateXProperty().bind(scene.widthProperty().divide(2).subtract(button.widthProperty().divide(2)));
        button.translateYProperty().bind(scene.heightProperty().divide(2).subtract(button.heightProperty().divide(2)));
        root1.getChildren().add(button);

        button.setOnAction(e -> {
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            root.requestFocus();
            startGameLoop();
        });

        primaryStage.setTitle("Stickman Game");
        primaryStage.setScene(scene1);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    void initializePlatform() {
        if (platform==null){
            platform = new Platform(root);
            platform.initialize();
            platform_distance = platform.getDistance();
        }
        else{
            platform.initialize();
            platform_distance = platform.getDistance();
        }
    }

    void initializeStickman() {
        stickman = new Stickman(root);
        stickman.initialize();
    }

    void startGameLoop() {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE && !stickSpawned && !growingInProgress) {
                startGrowingStick();
                stickSpawned = true;
                growingInProgress = false;
            }
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.SPACE && !growingInProgress) {
                growingInProgress=true;
                stopGrowingStick(platform, this);
            }
            startGameLoop();
        });

    }

    /*private void gameloop() {
        Timeline gameLoopTimeline = new Timeline(
                new KeyFrame(Duration.millis(10), e -> {
                    if (isWalking && !stickman.isWalking()) {
                        stickSpawned = false;
                        isWalking = false;
                        stickman.resetPosition(platform);
                        startGrowingStick();  // Start growing stick again after the platform shifts
                    }
                })
        );
        gameLoopTimeline.setCycleCount(Timeline.INDEFINITE);
        gameLoopTimeline.play();
    }*/

    private void startGrowingStick() {
        stickman.startGrowing();
        growingTimeline = new Timeline(
                new KeyFrame(Duration.millis(10), e -> growStick())
        );
        growingTimeline.setCycleCount(Animation.INDEFINITE);
        growingTimeline.play();
    }

    private void growStick() {
        stickman.grow();
    }

    private void stopGrowingStick(Platform platform, StickmanGame stickmangame) {
        growingTimeline.pause();
        stickman.stopGrowing(platform, this);
        stickman.startWalking(platform, this);
        isWalking = true;
    }

    public void exitscreen(){
    }

    public static void main(String[] args) {
        launch(args);
    }
}
