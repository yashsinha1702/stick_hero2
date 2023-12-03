package com.example.demogame;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.RotateEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

import static com.example.demogame.Platform.*;

public class Stickman {

    private static final double STICKMAN_SIZE = 50;
    public boolean stickmanfall;

    private Pane root;
    ImageView stickman;
    Line stick;

    private boolean endgame=false;

    private double stickHeight = 0;
    double stickmanX = 0;
    private double stickmanY = 0;

    ImageView cherry;

    Random random;

    private boolean isWalking = false;

    private Timeline walkingTimeline;

    private Timeline fallingTimeline;

    private Timeline rotatingTimeline;

    boolean reverse=false;

    boolean isreversible=false;

    private boolean isrotating;

    private Stage stage;

    private int rotateangle=0;

    Rotate rotate;

    private double positionX=0,positionY=0;

    boolean cherr_iscollected=false;

    public Stickman(Pane root) {
        this.root = root;
    }

    public void initialize() {
        Image stickmanImage = new Image("file:C:\\Users\\yash22590\\Downloads\\character_vector.png");
        stickman = new ImageView(stickmanImage);
        stickman.setFitHeight(STICKMAN_SIZE);
        stickman.setFitWidth(STICKMAN_SIZE);
        stickman.setLayoutY(root.getHeight() - PLATFORM_HEIGHT - STICKMAN_SIZE);
        root.getChildren().add(stickman);
        cherry=new ImageView();
        cherry.setImage(new Image("file:C:\\Users\\yash22590\\Downloads\\cherry.png"));
        cherry.setFitHeight(20);
        cherry.setFitWidth(20);
        cherry.setY(root.getHeight() - PLATFORM_HEIGHT);
        random=new Random();
        /*int temp=random.nextInt(5,90);
        cherry.setX(root.getWidth()*0.1+platform_distance*temp/100);
        root.getChildren().add(cherry);*/
    }

    public void startGrowing() {
        stick = new Line(root.getWidth()*0.1, root.getHeight() - PLATFORM_HEIGHT,
                root.getWidth()*0.1, root.getHeight() - PLATFORM_HEIGHT);
        stick.setStroke(Color.BLUE);
        stick.setStrokeWidth(5);
        root.getChildren().add(stick);
    }

    public void grow() {
        stickHeight += 1;
        stick.setEndY(root.getHeight() - PLATFORM_HEIGHT - stickHeight);
    }

    public void stopGrowing(Platform platform, StickmanGame stickmangame) {
        /*isrotating=true;
        positionX=stick.getStartX();
        positionY=stick.getEndY();
        rotatingTimeline = new Timeline(
                new KeyFrame(Duration.millis(10), e -> rotatestick(platform, stickmangame))
        );
        rotatingTimeline.setCycleCount(Timeline.INDEFINITE);
        rotatingTimeline.play();*/
        rotate = new Rotate();
        rotate.setPivotX(stickmanX + STICKMAN_SIZE / 2 + 55);
        rotate.setPivotY(root.getHeight() - PLATFORM_HEIGHT);
        stick.getTransforms().add(rotate);
        rotatingTimeline = new Timeline(
                new KeyFrame(Duration.millis(10), e -> rotatestick(platform,stickmangame)));
        rotatingTimeline.setCycleCount(Timeline.INDEFINITE);
        rotatingTimeline.play();

    }

    private void rotatestick(Platform platform,StickmanGame stickmangame) {
        rotateangle+=1;
        rotate.setAngle(rotateangle);
        if (rotateangle==90){
            rotatingTimeline.stop();
            isWalking = true;
            startWalking(platform, stickmangame);
        }
    }

    public void startWalking(Platform platform, StickmanGame stickmangame) {
        this.isreversible=true;
        if (walkingTimeline == null || !walkingTimeline.getStatus().equals(Animation.Status.RUNNING)) {
            walkingTimeline = new Timeline(
                    new KeyFrame(Duration.millis(10), e -> {
                        stickmangame.stickman.stickman.setOnKeyPressed(event -> {
                            if (event.getCode() == KeyCode.SPACE && isreversible && isWalking && stickmanX>root.getWidth()*0.1-STICKMAN_SIZE/2) {
                                reverse = !reverse; // Toggle the direction

                                if (reverse) {
                                    stickman.setRotate(180);
                                    stickman.setLayoutY(stickman.getLayoutY() + 60); // Move down
                                } else {
                                    stickman.setRotate(0); // Assuming the original rotation is 0
                                    stickman.setLayoutY(stickman.getLayoutY() - 60); // Move up
                                }
                            }
                        });
                        stickman.requestFocus();
                        walkStickman(platform, stickmangame);
                    })
            );
            walkingTimeline.setCycleCount(Timeline.INDEFINITE);
            walkingTimeline.play();

        }
    }

    private void walkStickman(Platform platform, StickmanGame stickmangame) {
        if (stickHeight<platform_distance+root.getWidth() * (((double) newrect.get(1) - (double) newrect.get(0))/ 100)){
            if (isWalking) {
                if (stickmanX > stickHeight + STICKMAN_SIZE && stickHeight < platform_distance) {
                    isWalking = false;
                    walkingTimeline.pause();
                    stickmanfall(platform, stickmangame);
                    isreversible = false;
                } else {
                    stickmanX += 1;
                    stickman.setTranslateX(stickmanX);
                    if (stickmanX - cherry.getX() >= -1 && stickmanX - cherry.getX() <= 1 && stickman.getLayoutY() > root.getHeight() - PLATFORM_HEIGHT - STICKMAN_SIZE) {
                        root.getChildren().remove(cherry);
                        cherr_iscollected = true;
                    }
                    System.out.println(stickmanX);
                    System.out.println(root.getWidth() * newrect.get(0));
                    if (root.getWidth() * newrect.get(0) / 100 - stickmanX < STICKMAN_SIZE - 10) {
                        if (stickman.getRotate() == 180) {
                            walkingTimeline.stop();
                            stickmanfall(platform, stickmangame);
                        }
                    }
                    if (stickmanX >= (root.getWidth() * (newrect.get(0)) / 100)) {
                        isWalking = false;
                        isreversible = false;
                        walkingTimeline.pause();
                        platform.nextPlatform(stickmangame);
                        //stickman.setTranslateX(-1 * (stick.getEndX() - stick.getStartX()));
                        //stick.setTranslateX(-1 * (platform_distance + platform.newRectangle.getWidth()));
                    }
                }
            }

        }
        else{
            if (isWalking) {
                if (stickmanX > stickHeight + STICKMAN_SIZE) {
                    isWalking = false;
                    walkingTimeline.pause();
                    stickmanfall(platform, stickmangame);
                    isreversible = false;
                } else {
                    stickmanX += 1;
                    stickman.setTranslateX(stickmanX);
                    if (stickmanX - cherry.getX() >= -1 && stickmanX - cherry.getX() <= 1 && stickman.getLayoutY() > root.getHeight() - PLATFORM_HEIGHT - STICKMAN_SIZE) {
                        root.getChildren().remove(cherry);
                        cherr_iscollected = true;
                    }
                    System.out.println(stickmanX);
                    System.out.println(root.getWidth() * newrect.get(0));
                    if (root.getWidth() * newrect.get(0) / 100 - stickmanX < STICKMAN_SIZE - 10) {
                        if (stickman.getRotate() == 180) {
                            walkingTimeline.stop();
                            stickmanfall(platform, stickmangame);
                        }
                    }

                }
            }
        }

    }

    private void stickmanfall(Platform platform, StickmanGame stickmanGame) {
        if (fallingTimeline==null || !walkingTimeline.getStatus().equals(Animation.Status.RUNNING)) {
            fallingTimeline = new Timeline(
                    new KeyFrame(Duration.millis(10), e -> fallStickman(platform, stickmanGame))
            );
        }
        fallingTimeline.setCycleCount(500);
        fallingTimeline.play();
        System.out.println("game end");
        fallingTimeline.setOnFinished(e->{
            stickmanY=0;
            resetPosition(platform,stickmanGame);
            stickmanGame.stickSpawned=false;
            reverse=false;
            stickman.setRotate(0);
            stickman.setTranslateY(-1*0.5);
            stickman.setTranslateX(0);
            stickman.setLayoutY(root.getHeight() - PLATFORM_HEIGHT - STICKMAN_SIZE);
            //stickmanGame.root.getChildren().clear();
            stage.setScene(stickmanGame.endscene);
            stickmanGame.retry.setOnAction(ea->{
                stage.setScene(stickmanGame.scene);
                /*stickmanGame.initializePlatform();
                stickmanGame.initializeStickman();*/
                stickmanGame.startGameLoop();
            });
        });
    }


    private void fallStickman(Platform platform, StickmanGame stickmanGame) {
        stickmanY += 0.5;
        stickman.setTranslateY(stickmanY);
    }


    public void resetPosition(Platform platform, StickmanGame stickmanGame) {
        if (!cherr_iscollected){
            root.getChildren().remove(cherry);
        }
        if(platform_distance>root.getWidth()*0.1){
            int temp=random.nextInt(5,90);
            cherry.setX(root.getWidth()*0.1+(platform_distance*temp/100));
            root.getChildren().add(cherry);
        }
        cherr_iscollected=false;
        rotateangle=0;
        isWalking = false;
        stickHeight = 0;
        stickmanX=0;
        stick.setEndY(root.getHeight() - PLATFORM_HEIGHT - stickHeight);
        root.getChildren().remove(stick);
        stickmanGame.growingInProgress=false;
    }

    public boolean isWalking() {
        return isWalking;
    }

    public boolean checkContinue() {
        return stick.getEndX() - stick.getStartX() < platform_distance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
