package org.firstinspires.ftc.teamcode.Subsystems_2019;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {
    DcMotor In1, In2;
    Servo IS1, IS2;

    public Intake(HardwareMap hardwareMap){
        In1 = hardwareMap.get(DcMotor.class, "In1");
        In2 = hardwareMap.get(DcMotor.class, "In2");
        IS1 = hardwareMap.get(Servo.class, "s1");
        IS2 = hardwareMap.get(Servo.class, "s2");
        init();
    }

    public void init(){
        In1.setDirection(DcMotorSimple.Direction.FORWARD);
        In2.setDirection(DcMotorSimple.Direction.REVERSE);
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setMode(DcMotor.RunMode Mode){
        In1.setMode(Mode);
        In2.setMode(Mode);
    }

    public void set(double Power){
        In1.setPower(Power);
        In2.setPower(Power);
    }

    public void IntakeDown(boolean bool){
        bool = bool;
        if(bool){ IS1.setPosition(1); IS2.setPosition(1);}
        else{ IS1.setPosition(0); IS2.setPosition(0);}
    }
}
