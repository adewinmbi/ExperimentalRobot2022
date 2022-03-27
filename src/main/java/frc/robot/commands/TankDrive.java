// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.Drivetrain;

public class TankDrive extends CommandBase {
  private final Drivetrain drivetrain;
  private final DoubleSupplier leftInputSupplier;
  private final DoubleSupplier rightInputSupplier;


  /** Creates a new TankDrive. 
   * 
   * @param drivetrain  the drivetrain subsystem
   * @param leftInputSupplier   the left input from -1 to 1 (e.g.: ps4 controller left stick y)
   * @param rightInputSupplier  the right input from -1 to 1
  */
  public TankDrive(Drivetrain drivetrain, DoubleSupplier leftInputSupplier, DoubleSupplier rightInputSupplier) {
    this.drivetrain = drivetrain;
    this.leftInputSupplier = leftInputSupplier;
    this.rightInputSupplier = rightInputSupplier;
    addRequirements(this.drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double prevLeftOutput = drivetrain.leftMaster.getMotorOutputPercent();
    double prevRightOutput = drivetrain.rightMaster.getMotorOutputPercent();

    double leftInput = leftInputSupplier.getAsDouble();
    double rightInput = rightInputSupplier.getAsDouble();
    
    // Low pass filter, output = (alpha * intended value) + (1-alpha) * previous value
    double leftOutput = (DriveConstants.kDriveAlpha * leftInput) + (DriveConstants.kDriveOneMinusAlpha * prevLeftOutput);
    double rightOutput = (DriveConstants.kDriveAlpha * rightInput) + (DriveConstants.kDriveOneMinusAlpha * prevRightOutput);

    drivetrain.drive(leftOutput, rightOutput);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
