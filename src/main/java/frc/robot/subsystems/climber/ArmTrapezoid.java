package frc.robot.subsystems.climber;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.TrapezoidProfileSubsystem;
import frc.robot.Constants.ClimberConstants;

public class ArmTrapezoid {

    public TalonSRX arm;
    
    public ArmTrapezoid() {
        arm = new TalonSRX(ClimberConstants.kArmTalonID);
        arm.setInverted(true);
        arm.configMotionAcceleration(ClimberConstants.kArmMotionAcceleration);
        arm.configMotionCruiseVelocity(ClimberConstants.kArmCruiseVelocity);
        arm.configNeutralDeadband(ClimberConstants.kArmDeadband);
        arm.config_kP(0, ClimberConstants.kArmkP);
        arm.config_kD(0, ClimberConstants.kArmkD);
        arm.config_kF(0, ClimberConstants.kArmkF);
    }

    public void setPositionMotionMagic(double ticks) {
        arm.set(ControlMode.MotionMagic, ticks, 
            DemandType.ArbitraryFeedForward, FF());
    }

    public void resetClimbEncoder() {
        arm.setSelectedSensorPosition(0);
    }  

    public void reportToSmartDashboard() {
        SmartDashboard.putNumber(" Climber Position ", arm.getSelectedSensorPosition());
        SmartDashboard.putNumber(" Climber Voltage ", arm.getMotorOutputVoltage());
        SmartDashboard.putNumber(" Climber Voltage ", arm.getSupplyCurrent());
    }

    public double FF() {
        return ClimberConstants.kArmGravityFF * Math.cos(degreesToRadians(ticksToAngle()));
    }

    public double getPosition() {
        return arm.getSelectedSensorPosition();
    }
    
    
    public double ticksToAngle() {
        return 90 - ((arm.getSelectedSensorPosition() - ClimberConstants.kArmAngleOffset)
             * 360 / 4096);
    }

    public double degreesToRadians(double deg) {
        return deg * Math.PI/180;
    }  

}
