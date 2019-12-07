package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems_2019.Elevator;
import org.firstinspires.ftc.teamcode.Subsystems_2019.Intake;
import org.firstinspires.ftc.teamcode.Subsystems_2019.MecanumDrive;
import org.firstinspires.ftc.teamcode.Subsystems_2019.OutTake;
import org.firstinspires.ftc.teamcode.Subsystems_2019.Foundation;

@TeleOp(name="OpMode", group="Iterative Opmode")
public class Robot extends OpMode{

    private ElapsedTime runtime = new ElapsedTime();
        Elevator elevator;
        MecanumDrive mecanumDrive;
        OutTake outTake;
        Intake intake;
        Foundation foundation;
        double power;
        int Action;

        @Override
        public void init(){
            elevator = new Elevator(hardwareMap);
            mecanumDrive = new MecanumDrive(hardwareMap);
            outTake = new OutTake(hardwareMap);
            intake = new Intake(hardwareMap);
            foundation = new Foundation(hardwareMap);
        }
        @Override
        public void init_loop(){
        }

        @Override
         public void start(){
        }

        @Override
        public void loop(){
            runtime.startTime();
            power = gamepad1.left_stick_button ? 0.5 : 1.0;
            mecanumDrive.setMecanum(power * -gamepad1.right_stick_y, power *
                    (gamepad1.left_bumper ? 1.0 : gamepad1.right_bumper ? -1.0 : 0), power * gamepad1.right_stick_x);
            elevator.set(gamepad2.a ? 1.0 : gamepad2.b ? -1.0 : 0);
            intake.set(gamepad2.left_bumper ? 1.0 : gamepad2.right_bumper ? -1.0 : 0);
            Action = gamepad2.dpad_right ? 1 : gamepad2.x ? 2 : gamepad2.dpad_left ? 3  : gamepad2.dpad_up ? 4 : 0;
             outTake.Do(Action);
            foundation.set(gamepad1.a ?1.0 : gamepad1.b ?-1.0 :0);
            telemetry.addData(
                    "RunTime:", runtime.toString());
        }
}
