// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.climber.Elevator;
import frc.robot.subsystems.Drivetrain;
import frc.robot.RobotContainer;
import frc.robot.Constants.ClimberConstants;


public class Robot extends TimedRobot {
  public static SendableChooser<Command> autoChooser;
  public static Command m_autonomousCommand;

  // TODO: refractor code so that this doesn't have to be public static, and follows the intended use of robotContainer
  public static RobotContainer robotContainer;

  @Override
  public void robotInit() { 
    CommandScheduler.getInstance().cancelAll();
    
    robotContainer = new RobotContainer();
    
    Log.initAndLog("/home/lvuser/logs/", "Test", 0.02, robotContainer);
    
    robotContainer.elevator.elevator.setSelectedSensorPosition(0);
    robotContainer.elevator.elevator.setNeutralMode(NeutralMode.Brake);
    robotContainer.armTrapezoid.arm.setNeutralMode(NeutralMode.Brake);
  }
  
  @Override
  public void teleopInit() { 
    robotContainer.elevator.elevator.setSelectedSensorPosition(0);
    robotContainer.elevator.elevator.setNeutralMode(NeutralMode.Brake);
    //robotContainer.drivetrain.compressor.enableDigital();
    robotContainer.everybotClimber.climberMaster.setSelectedSensorPosition(0);
  }


  @Override
  public void teleopPeriodic() { 
    robotContainer.reportToSmartDashboard();
    robotContainer.drivetrain.rightMaster.setNeutralMode(NeutralMode.Coast);
    robotContainer.drivetrain.rightSlave.setNeutralMode(NeutralMode.Coast);
    robotContainer.drivetrain.leftMaster.setNeutralMode(NeutralMode.Coast);
    robotContainer.drivetrain.leftSlave.setNeutralMode(NeutralMode.Coast);
    robotContainer.configureButtonBindings();
  }


  @Override
  public void autonomousInit() {
    CommandGroupBase command = robotContainer.autoChooser.getSelected();

    if (command != null) {
      command.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {
    // Arm.rotateArmToAngle(64, 5);
    CommandScheduler.getInstance().run();


    // -21560 to reach mid 
    // -2675 to go down & latch
  }

  @Override
  public void disabledInit() {
    CommandScheduler.getInstance().cancelAll();
  }
}