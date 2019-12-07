package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="MecanumPolar", group="Test")
//@Disabled
public class MecanumPolar extends OpMode{

    DcMotor FrontLeft;
    DcMotor FrontRight;
    DcMotor RearLeft;
    DcMotor RearRight;
    boolean joyPosTele = false;
    boolean joyPolarCoordTele = false;
    boolean wheelScalersTele = false;

    @Override
    public void init() {

        telemetry.addData("Say", "Buenas noches");
        FrontRight = hardwareMap.get(DcMotor.class, "m0");
        FrontLeft = hardwareMap.get(DcMotor.class, "m1");
        RearRight = hardwareMap.get(DcMotor.class, "m2");
        RearLeft = hardwareMap.get(DcMotor.class, "m3");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
    }
    @Override
    public void loop() {

        double speed = 0, angle = 0, dchange = 0;
        double retValues [];

        retValues = cartesianToPolar(gamepad1.left_bumper ? 1.0: gamepad1.right_bumper ?-1.0 :0, gamepad1.right_stick_x, gamepad1.left_stick_y);
        speed = retValues[0];
        angle = retValues[1];
        dchange = retValues[2];
        polarToWheelSpeed(speed, angle, dchange);
        telemetry.update();
    }
    @Override
    public void stop() {
    }

    public double[] cartesianToPolar(double y1, double x1, double x2) {

        double[] retValues = new double []{0.0,0.0,0.0};
        double speed = 0.0, angle=0.0, dchange=0.0;

        speed = Math.sqrt((y1 * y1) + (x1 * x1));
        angle = Math.atan2(x1, -y1);
        dchange = -x2 / 3.33;
        if (joyPosTele) {
            telemetry.addData("X1: ",x1);
            telemetry.addData("Y1: ",y1);
            telemetry.addData("X2: ",x2);
        }

        if (joyPolarCoordTele) {
            telemetry.addData("Velocidad: ", speed);
            telemetry.addData("Angulo: ", angle);
            telemetry.addData("Cambio rotacional", dchange);
        }

        retValues[0] = speed;
        retValues[1] = angle;
        retValues[2] = dchange;
        return retValues;
    }

    public void polarToWheelSpeed(double speed, double angle, double dchange){

//Create Variables used only in this method
        double pos1, pos2, pos3, pos4, maxValue;

//Define unscaled voltage multipliers


        pos1 = speed*Math.sin(angle+(Math.PI/4))+dchange;
        pos2 = speed*Math.cos(angle+(Math.PI/4))-dchange;
        pos3 = speed*Math.cos(angle+(Math.PI/4))+dchange;
        pos4 = speed*Math.sin(angle+(Math.PI/4))-dchange;

        maxValue = Math.abs(pos1);

        if(Math.abs(pos2) > maxValue){maxValue = Math.abs(pos2);}
        if(Math.abs(pos3) > maxValue){maxValue = Math.abs(pos3);}
        if(Math.abs(pos4) > maxValue){maxValue = Math.abs(pos4);}
        if (maxValue <= 1){ maxValue = 1;}

        FrontLeft.setPower(pos1/maxValue);
        FrontRight.setPower(pos2/maxValue);
        RearLeft.setPower(pos3/maxValue);
        RearRight.setPower(pos4/maxValue);

        if (wheelScalersTele) {
            telemetry.addData("Wheel 1 W/ Scale: ",pos1/maxValue);
            telemetry.addData("Wheel 2 W/ Scale: ",pos2/maxValue);
            telemetry.addData("Wheel 3 W/ Scale: ",pos3/maxValue);
            telemetry.addData("Wheel 4 W/ Scale: ",pos4/maxValue);
        }
    }
}

