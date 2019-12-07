package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Subsystems_2019.MecanumDrive;

@Autonomous(name = "Just_Parking", group = "Autonomous")

public class Just_Parking extends LinearOpMode {
    MecanumDrive mecanumDrive;
    private static final double COUNTS_PER_MOTOR_GOBILDA = 383.6;
    private static final double DRIVE_GEAR_REDUCTION = 1.0;
    private static final double WHEEL_DIAMETER_INCHES = 100 / 2.54;
    private static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_GOBILDA * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.14159);

    @Override
    public void runOpMode() {
        mecanumDrive = new MecanumDrive(hardwareMap);

        waitForStart();

        mecanumDrive.setRightStrafeTarget((int)Math.round(COUNTS_PER_INCH * 30));
    }
}
