// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.ClimberConstants;
import frc.robot.subsystems.climber.ArmTrapezoid;

public class MoveArmTrapezoid extends CommandBase {
  private final DoubleSupplier armInputSupplier;
  private final DoubleSupplier hooksInputSupplier;
  private final ArmTrapezoid armTrapezoid;

  /** Creates a new MoveArmTrapezoid. */
  public MoveArmTrapezoid(DoubleSupplier armInputSupplier, DoubleSupplier hooksInputSupplier, ArmTrapezoid armTrapezoid) {
    this.armInputSupplier = armInputSupplier;
    this.hooksInputSupplier = hooksInputSupplier;
    this.armTrapezoid = armTrapezoid;
    addRequirements(this.armTrapezoid);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double armInput = -armInputSupplier.getAsDouble();
    armTrapezoid.arm.set(ControlMode.PercentOutput, armInput * 0.25, DemandType.ArbitraryFeedForward, -1 * armTrapezoid.FF());

    double hooksInput = hooksInputSupplier.getAsDouble();
    if (hooksInput > ClimberConstants.kOperatorDeadband) {
      armTrapezoid.enableClimberHooks();
    } else {
      armTrapezoid.disableClimberHooks();
    }
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
