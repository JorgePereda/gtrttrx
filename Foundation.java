package org.firstinspires.ftc.teamcode.Subsystems_2019;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Foundation {
    private Servo Left;
    private Servo Right;
    public Foundation(HardwareMap hardwareMap){
       Left = hardwareMap.get(Servo.class, "L");
       Right = hardwareMap.get(Servo.class, "R");
    }

    public void set(double position) {
        Left.setPosition(position);
        Right.setPosition(position);
    }
}