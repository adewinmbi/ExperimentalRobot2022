package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.Constants.ClimberConstants;
import frc.robot.Constants.DriveConstants;

public class Drivetrain extends SubsystemBase {
    public TalonFX rightMaster; // Channel 30 on CAN, 14 on PDP
    public TalonFX leftMaster; // Channel 16 on CAN, 0 on PDP
    public TalonFX rightSlave; // Channel 31 on CAN, 15 on PDP
    public TalonFX leftSlave; // Channel 17 on CAN, 1 on PDP
    public TalonFX rightSlave2;
    public TalonFX leftSlave2;

    public Compressor compressor; // Channel 3 on CAN
    public DoubleSolenoid driveShifter; // Channels 0 and 6
    public DoubleSolenoid climberShifter; // Channels 7 and 6
    public DoubleSolenoid hookShifter; // Channels 2 and 5
    
    // ======================= TELEOP FUNCTIONS ======================= //
    
    public Drivetrain() {
      rightMaster = new TalonFX(DriveConstants.kRightMasterTalonID);
      leftMaster = new TalonFX(DriveConstants.kLeftMasterTalonID);
      rightSlave = new TalonFX(DriveConstants.kRightSlaveTalonID);  
      leftSlave = new TalonFX(DriveConstants.kLeftSlaveTalonID);
      rightSlave2 = new TalonFX(DriveConstants.kRightSlave2TalonID);
      leftSlave2 = new TalonFX(DriveConstants.kLeftSlave2TalonID);

      leftSlave.follow(leftMaster);
      leftSlave2.follow(leftMaster);
      rightSlave.follow(rightMaster);
      rightSlave2.follow(rightMaster);

      // Inverted the right side
      rightMaster.setInverted(true);
      leftSlave.setInverted(InvertType.FollowMaster);
      leftSlave2.setInverted(InvertType.FollowMaster);
      rightSlave.setInverted(InvertType.FollowMaster);
      rightSlave2.setInverted(InvertType.FollowMaster);

      // Pneumatics setup
      compressor = new Compressor(3, PneumaticsModuleType.CTREPCM);

      driveShifter = new DoubleSolenoid(3, PneumaticsModuleType.CTREPCM, DriveConstants.kDriveShifterForwardID, DriveConstants.kDriveShifterReverseID);
      // MIGHT NEED TO BE CHANGED
      climberShifter = new DoubleSolenoid(3, PneumaticsModuleType.CTREPCM, DriveConstants.kClimberShifterForwardID, DriveConstants.kClimberShifterReverseID);
      hookShifter = new DoubleSolenoid(3, PneumaticsModuleType.CTREPCM, DriveConstants.kHookShifterForwardID, DriveConstants.kHookShifterReverseID);
    }

    public static double gainInput(double input) {
      input = Math.pow(input, 3);
      return input;
    }

    public void driveControllerMovement() {
    }

    public void setPowerZero() {
      rightMaster.set(ControlMode.PercentOutput, 0);
      leftMaster.set(ControlMode.PercentOutput, 0);
    }

    public void setPower(double rightPower, double leftPower) {
      rightMaster.set(ControlMode.PercentOutput, rightPower);
      leftMaster.set(ControlMode.PercentOutput, leftPower);
    }

    public void reportToSmartDashboard() {
      SmartDashboard.putNumber(" Right Master Current ", rightMaster.getSupplyCurrent());
      SmartDashboard.putNumber(" Right Slave Current ", rightSlave.getSupplyCurrent());
      SmartDashboard.putNumber(" Left Master Current ", leftMaster.getSupplyCurrent());
      SmartDashboard.putNumber(" Left Slave Current ", leftSlave.getSupplyCurrent());
    }

    // ====================== AUTONOMOUS FUNCTIONS ====================== //
    
    // Speed in percentage, waitTime in seconds
    public void drive(double leftSpeed, double rightSpeed, double waitTime) {
      leftMaster.set(ControlMode.PercentOutput, leftSpeed);
      rightMaster.set(ControlMode.PercentOutput, rightSpeed);

      Timer.delay(waitTime);
    }
}
