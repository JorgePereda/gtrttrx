package org.firstinspires.ftc.teamcode.Subsystems_2019;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OrientationSensor;

public class MecanumDrive {

    static boolean fourWD = true;
    DcMotor FrontLeft;
    DcMotor FrontRight;
    DcMotor RearLeft;
    DcMotor RearRight;

    public MecanumDrive(HardwareMap hardwareMap) {
        FrontLeft = hardwareMap.get(DcMotor.class, "m0");
        FrontRight = hardwareMap.get(DcMotor.class, "m1");
        if (fourWD) {
            RearRight = hardwareMap.get(DcMotor.class, "m2");
            RearLeft = hardwareMap.get(DcMotor.class, "m3");
        }
        init();
    }

    public void setMode(DcMotor.RunMode RunMode){
        FrontRight.setMode(RunMode);
        FrontLeft.setMode(RunMode);
        if(fourWD){
            RearRight.setMode(RunMode);
            RearLeft.setMode(RunMode);
        }
    }

    public void setLeftTargetPosition(int Position){
        FrontLeft.setTargetPosition(-Position);
        if(fourWD) RearLeft.setTargetPosition(-Position);
    }

    public void setRightTargetPosition(int Position){
        FrontRight.setTargetPosition(Position);
        if(fourWD) RearRight.setTargetPosition(Position);
    }

    public void init(){
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    public void setMecanum(double drive, double strafe, double turn){
        FrontLeft.setPower(drive + strafe + turn);
        FrontRight.setPower(drive - strafe - turn);
        RearLeft.setPower(drive - strafe + turn);
        RearRight.setPower(drive + strafe - turn);
    }

    public void setLeftPower(double power){
        power = power > 1.0 ? 1 : power < -1.0 ? -1 : power;
        FrontLeft.setPower(power);
        if(fourWD)
            RearLeft.setPower(power);
    }
    public void setRightPower(double power){
        power = power > 1.0 ? 1 : power < -1.0 ? -1 : power;
        FrontRight.setPower(power);
        if(fourWD)
            RearRight.setPower(power);
    }

    public void setRightStrafeTarget(int TargetPosition){
        FrontLeft.setTargetPosition(TargetPosition);
        FrontRight.setTargetPosition(-TargetPosition);
        RearLeft.setTargetPosition(-TargetPosition);
        RearRight.setTargetPosition(TargetPosition);
    }

    public void setLeftStrafeTarget(int TargetPosition){
        FrontLeft.setTargetPosition(-TargetPosition);
        FrontRight.setTargetPosition(TargetPosition);
        RearLeft.setTargetPosition(TargetPosition);
        RearRight.setTargetPosition(-TargetPosition);
    }
    public double getLeftSpeed(){ return FrontLeft.getPower(); }

    public double getRightSpeed(){ return FrontRight.getPower(); }

    public int getLeftDistance(){ return FrontLeft.getCurrentPosition(); }

    public int getRightDistance(){ return FrontRight.getCurrentPosition(); }

    public DcMotor getLeftDrive(){ return FrontLeft; }

    public DcMotor getRightDrive(){ return FrontRight; }

}
