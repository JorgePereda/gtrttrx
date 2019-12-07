package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.Subsystems_2019.MecanumDrive;
import org.firstinspires.ftc.teamcode.Subsystems_2019.OutTake;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;


@Autonomous(name = "AutoRight", group = "Autonomous")
public class AutoRight extends LinearOpMode {


    private MecanumDrive mecanumDrive;

    private OutTake outTake;

    VuforiaLocalizer vuforia;

    private ElapsedTime runtime = new ElapsedTime();

    VuforiaTrackables targetsSkystone;

    private boolean IsTargetVisible;

    TFObjectDetector tfod;


    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_STONE = "Stone";
    private static final String LABEL_SKYSTONE = "Skystone";

    private static final String VUFORIA_KEY = "AeGowNf/////AAABmfGGsAcH5U5Khi1MWibkKP48WQAllMTOTOTNemAkYHOABg5QAL9bW66pHoNZLRHMEf8EwUXIxpGZdfKE3hI4H63z9jFXnNmB3nkSSgq+IxrzWsY5qw8jzUs7OzMW+p47QpuCWFAferD97gb9/9bcaB/luYsolbnUo4V6kg7V49ks/Tcc8WjZ+Sqp5Aij4uUkOcEDIzJ6gzjT5LlfEEfsIzIchGvt7mh1ZXQwvxZw/gTGuG6rfZjVwovCAcogkqHCEUCqjrysKyMXYbfHwPnmCziS84JD5n7BzJJlWWINImPTd00lu2gNtYYiw/P6BTKQSPtWJFimPTh7UuL45P8qy7WC11TXDIMYyPhJa+ceGf9m";
    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = FRONT;

    private static final double COUNTS_PER_MOTOR_GOBILDA = 383.6;
    private static final double DRIVE_GEAR_REDUCTION = 1.0;
    private static final double WHEEL_DIAMETER_INCHES = 100 / 2.54;
    private static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_GOBILDA * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.14159);
    private static final double DRIVE_SPEED = 0.40;
    private static final double STRAFE_SPEED = 1.0;
    private static final double DISTANCE_STONE_TO_ANOTHER = 8;
    private static final double DISTANCE_SKYSTONE_TO_ANOTHER = DISTANCE_STONE_TO_ANOTHER * 3; //DISTANCIA EN PULGADAS


    @Override
    public void runOpMode() {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        if (tfod != null) {
            tfod.activate();
        }

        /* Wait for the game to begin */
        telemetry.addData(">", "Press Play to start autonomous");
        telemetry.update();

        mecanumDrive = new MecanumDrive(hardwareMap);
        outTake = new OutTake(hardwareMap);

        waitForStart();

        mecanumDrive.setLeftStrafeTarget((int)Math.round(COUNTS_PER_INCH * 50));
        MecanumEDrive(DRIVE_SPEED,27,27,4);
        pickSkystone();
        mecanumDrive.setRightStrafeTarget((int)Math.round(COUNTS_PER_INCH * 35));



        if (opModeIsActive()) {
            while (opModeIsActive()) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());

                        // step through the list of recognitions and display boundary info.
                        int i = 0;
                        for (Recognition recognition : updatedRecognitions) {
                            telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                            telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                    recognition.getLeft(), recognition.getTop());
                            telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                    recognition.getRight(), recognition.getBottom());
                        }
                        telemetry.update();
                    }
                }
            }
        }

        if (tfod != null) {
            tfod.shutdown();
        }
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY ;
        parameters.cameraDirection   = CAMERA_CHOICE;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Load the data sets that for the trackable objects. These particular data
        // sets are stored in the 'assets' part of our application.
        targetsSkystone = this.vuforia.loadTrackablesFromAsset("Skystone");
        VuforiaTrackable blueRobot = targetsSkystone.get(0);
        blueRobot.setName("Blue-Robot");
        VuforiaTrackable backSpace = targetsSkystone.get(3);
        backSpace.setName("Back-Space");
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.8;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_STONE, LABEL_SKYSTONE);
    }

    public void MecanumEDrive(double speed, double leftInches, double rightInches, double TimeOutS) {
        int newLeftTarget, newRightTarget;

        if (opModeIsActive()) {
            newLeftTarget = mecanumDrive.getLeftDistance() + (int) (leftInches * COUNTS_PER_INCH);
            newRightTarget = mecanumDrive.getRightDistance() + (int) (rightInches * COUNTS_PER_INCH);
            mecanumDrive.setLeftTargetPosition(newLeftTarget);
            mecanumDrive.setRightTargetPosition(newRightTarget);

            mecanumDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();

            mecanumDrive.setLeftPower(Math.abs(speed));
            mecanumDrive.setRightPower(Math.abs(speed));

            while (opModeIsActive() &&
                    (runtime.seconds() < TimeOutS) &&
                    (mecanumDrive.getLeftDrive().isBusy() && mecanumDrive.getRightDrive().isBusy())) {

                telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d", mecanumDrive.getLeftDistance(), mecanumDrive.getRightDistance());
                telemetry.update();
            }
        }
    }
    private void pick(){
        outTake.Do(3);                 //sube servos
        outTake.Do(1);                //1, 2 Sacar, meter sistemas lineales
        sleep(1000);             //espacio para que se muevan los sistemas lineales
        outTake.Do(4);                // baja servos
        outTake.Do(6);                //agarra skystone
        outTake.Do(3);                //sube otravez
    }

    private void let(){
        outTake.Do(4);
    }
    private void pickSkystone(){
        int x = checkSkystone();
        if(x>0){
            pick();
            mecanumDrive.setLeftStrafeTarget((int)Math.round(COUNTS_PER_INCH * 70));
            let();
            mecanumDrive.setRightStrafeTarget((int)Math.round(COUNTS_PER_INCH * (70 + DISTANCE_SKYSTONE_TO_ANOTHER)));
            pick();
            mecanumDrive.setLeftStrafeTarget((int)Math.round(COUNTS_PER_INCH * (70 + DISTANCE_SKYSTONE_TO_ANOTHER)));
            let();
        }
        else{
            mecanumDrive.setRightStrafeTarget((int) Math.round(COUNTS_PER_INCH * DISTANCE_STONE_TO_ANOTHER));
            x = checkSkystone();
            if(x>0){
                pick();
                mecanumDrive.setLeftStrafeTarget((int) Math.round(COUNTS_PER_INCH * 70 + DISTANCE_STONE_TO_ANOTHER));
                let();
                mecanumDrive.setRightStrafeTarget((int) Math.round(COUNTS_PER_INCH * 70 + DISTANCE_SKYSTONE_TO_ANOTHER + DISTANCE_STONE_TO_ANOTHER));
                pick();
                mecanumDrive.setLeftStrafeTarget((int) Math.round(COUNTS_PER_INCH * 70 + DISTANCE_SKYSTONE_TO_ANOTHER + DISTANCE_STONE_TO_ANOTHER));
                let();
            }
            else{
                mecanumDrive.setRightStrafeTarget((int) Math.round(COUNTS_PER_INCH * DISTANCE_STONE_TO_ANOTHER));
                pick();
                mecanumDrive.setLeftStrafeTarget((int) Math.round(COUNTS_PER_INCH * 70 + DISTANCE_STONE_TO_ANOTHER + DISTANCE_STONE_TO_ANOTHER));
                let();
                mecanumDrive.setRightStrafeTarget((int) Math.round(COUNTS_PER_INCH * 70 + DISTANCE_SKYSTONE_TO_ANOTHER + DISTANCE_STONE_TO_ANOTHER + DISTANCE_STONE_TO_ANOTHER));
                pick();
                mecanumDrive.setLeftStrafeTarget((int) Math.round(COUNTS_PER_INCH * 70 + DISTANCE_SKYSTONE_TO_ANOTHER + DISTANCE_STONE_TO_ANOTHER + DISTANCE_STONE_TO_ANOTHER));
                let();
            }
        }
    }


    private int checkSkystone() {
        IsTargetVisible = false;
        runtime.reset();
        int SkystoneX = -1;
        while (opModeIsActive() && !IsTargetVisible && runtime.time() < 2) {
            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    if (updatedRecognitions.size() >= 1) {
                        for (Recognition recognition : updatedRecognitions) {
                            if (recognition.getLabel().equals(LABEL_SKYSTONE)) {
                                SkystoneX = (int) recognition.getLeft();
                                IsTargetVisible = true;
                            }
                        }
                        telemetry.addData("Position:", SkystoneX);
                        telemetry.update();
                    }
                }
            }
        }
        return SkystoneX;
    }

    private void checkPosition(List<VuforiaTrackable> allTrackables){
        boolean targetIsVisible = false;
        while (opModeIsActive() && !targetIsVisible) {
            for (VuforiaTrackable trackable : allTrackables) {
                if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                    telemetry.addData("Visible Target", trackable.getName());
                    targetIsVisible = true;
                    break;
                }
            }
            if (!targetIsVisible) {
                telemetry.addData("Visible Target", "none");
            }
        }
        telemetry.update();
    }
}


