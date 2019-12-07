package org.firstinspires.ftc.teamcode.Subsystems_2019;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Elevator {
    DcMotor Ele1;
    DcMotor Ele2;
    public Elevator(HardwareMap hardwareMap){
      Ele1 = hardwareMap.get(DcMotor.class, "e1");
      Ele2 = hardwareMap.get(DcMotor.class, "e2");
      init();
    }

    public void init(){
        Ele1.setDirection(DcMotorSimple.Direction.FORWARD);
        Ele2.setDirection(DcMotorSimple.Direction.REVERSE);
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setMode(DcMotor.RunMode Mode){
        Ele1.setMode(Mode);
        Ele2.setMode(Mode);
    }

    public void set(double Power){
        Ele1.setPower(Power);
        Ele2.setPower(Power);
    }
}
