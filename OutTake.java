package org.firstinspires.ftc.teamcode.Subsystems_2019;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class OutTake {
    private Servo UD, UD1, CL, TR;                  //UD, UD1: UpDown. CL: CloseServo, TR: TuRn
    private CRServo Sl, Sl2;                        //Sl, Sl2 Sistemas lineales
    public DigitalChannel Limit;

    public OutTake(HardwareMap hardwareMap){
        Sl = hardwareMap.get(CRServo.class, "SL");
        Sl2 = hardwareMap.get(CRServo.class, "SL2");
        UD = hardwareMap.get(Servo.class, "UD");
        UD1 = hardwareMap.get(Servo.class, "UD1");
        CL = hardwareMap.get(Servo.class, "CL");
        TR = hardwareMap.get(Servo.class, "TR");
    }

    public void init(){
        Sl.setDirection(CRServo.Direction.FORWARD);
        Sl2.setDirection(CRServo.Direction.REVERSE);
    }

    public void Do(int Case){
        switch (Case){              /*Case es un numero entero que representa lo que queremos que suceda con nuestro sistema, debido a
                                       la cantidad de servos que tenemos */
            case 1:
                Sl.setPower(1.0);
                Sl2.setPower(1.0);
                break;
            case 2:
                Sl.setPower(-1.0);
                Sl2.setPower(-1.0);
                break;
            case 3:
                UD.setPosition(1.0);
                UD1.setPosition(1.0);
                break;
            case 4:
                UD.setPosition(0.0);
                UD1.setPosition(0.0);
                break;
            case 5:
                if(Limit.getState() == true) CL.setPosition(1.0);
                break;
            case 6:
            CL.setPosition(1.0);
            break;
            case 7:
                TR.setPosition(1.0);
                break;
            case 8:
                TR.setPosition(0.0);
            default:
                CL.setPosition(0.0);
                Sl.setPower(0.0);
                Sl2.setPower(0.0);
                break;
        }
    }

    }

